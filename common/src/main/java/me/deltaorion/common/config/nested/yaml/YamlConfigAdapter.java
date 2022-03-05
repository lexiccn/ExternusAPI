package me.deltaorion.common.config.nested.yaml;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.nested.NestedObjectSection;
import me.deltaorion.common.config.value.ConfigObjectValue;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.*;

/**
 * @author org.bukkit, deltaorion
 */
public class YamlConfigAdapter implements ConfigAdapter {

    private final DumperOptions yamlDumperOptions;
    private final LoaderOptions yamlLoaderOptions;
    private final YamlConstructor constructor;
    private final Representer representer;
    private final Yaml yaml;
    private final YamlOptions options;
    
    private final ConfigSection adapterFor;
    private NestedObjectSection root;

    public YamlConfigAdapter(@NotNull ConfigSection adapterFor) {
        this(adapterFor,new YamlOptions());
    }

    public YamlConfigAdapter(@NotNull ConfigSection adapterFor, YamlOptions options) {
        this(adapterFor,options,new NestedObjectSection(adapterFor));
    }

    private YamlConfigAdapter(@NotNull ConfigSection adapterFor, @NotNull YamlOptions options, @NotNull NestedObjectSection section) {
        this.adapterFor = adapterFor;
        this.options = options;

        this.root = section;

        yamlDumperOptions = new DumperOptions();
        yamlDumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlLoaderOptions = new LoaderOptions();
        yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE); // SPIGOT-5881: Not ideal, but was default pre SnakeYAML 1.26
        this.constructor = new YamlConstructor();
        this.representer = new Representer();

        yaml = new /*BukkitYaml*/Yaml(constructor, representer, yamlDumperOptions, yamlLoaderOptions);
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return root.getKeys();
    }

    @NotNull
    @Override
    public ConfigValue getValue(@NotNull String path) {
        Object value = root.get(path);
        if(value instanceof NestedObjectSection) {
            NestedObjectSection section = (NestedObjectSection) value;
            return new ConfigObjectValue(wrapMemorySection(section));
        } else {
            return new ConfigObjectValue(value);
        }
    }

    private ConfigSection wrapMemorySection(NestedObjectSection section) {
        if(section.getParent()==null) {
            return adapterFor;
        }
        return new ConfigSection(new YamlConfigAdapter(adapterFor,options,section),section.getName(),wrapMemorySection(section.getParent()),adapterFor.getRoot());
    }

    @Override
    public void set(@NotNull String path, Object value) {
        root.set(path,value);
    }

    @Override
    public void remove(@NotNull String path) {
        root.set(path,null);
    }

    @NotNull
    @Override
    public ConfigSection createSection(@NotNull String path) {
        return createSection(path,Collections.emptyMap());
    }

    @NotNull
    @Override
    public ConfigSection createSection(@NotNull String path, Map<String, Object> map) {
        return wrapMemorySection(root.createSection(path,map));
    }

    @Override
    public boolean supportsNesting() {
        return true;
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        Objects.requireNonNull(file);
        save(new FileWriter(file));
    }

    @Override
    public void save(@NotNull Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        if(!(adapterFor instanceof FileConfig))
            throw new IllegalStateException("Cannot save a non-root config!");

        FileConfig config = (FileConfig) adapterFor;
        yamlDumperOptions.setIndent(options.getIndent());
        yamlDumperOptions.setWidth(options.getWidth());
        yamlDumperOptions.setProcessComments(config.options().parseComments());

        MappingNode node = toNodeTree(root);

        node.setBlockComments(getCommentLines(saveHeader(config.options().getHeader()), CommentType.BLOCK));
        node.setEndComments(getCommentLines(config.options().getFooter(), CommentType.BLOCK));

        if (node.getBlockComments().isEmpty() && node.getEndComments().isEmpty() && node.getValue().isEmpty()) {
            writer.write("");
        } else {
            if (node.getValue().isEmpty()) {
                node.setFlowStyle(DumperOptions.FlowStyle.FLOW);
            }
            yaml.serialize(node, writer);
        }
    }

    private List<String> saveHeader(List<String> header) {
        LinkedList<String> list = new LinkedList<>(header);

        if (!list.isEmpty()) {
            list.add(null);
        }

        return list;
    }
    private MappingNode toNodeTree(NestedObjectSection section) {
        List<NodeTuple> nodeTuples = new ArrayList<>();
        for (Map.Entry<String, Object> entry : section.getValues().entrySet()) {
            Node key = representer.represent(entry.getKey());
            Node value;
            if (entry.getValue() instanceof NestedObjectSection) {
                value = toNodeTree((NestedObjectSection) entry.getValue());
            } else {
                value = representer.represent(entry.getValue());
            }
            key.setBlockComments(getCommentLines(section.getComments(entry.getKey()), CommentType.BLOCK));
            if (value instanceof MappingNode || value instanceof SequenceNode) {
                key.setInLineComments(getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
            } else {
                value.setInLineComments(getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
            }

            nodeTuples.add(new NodeTuple(key, value));
        }

        return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
    }

    private List<CommentLine> getCommentLines(List<String> comments, CommentType commentType) {
        List<CommentLine> lines = new ArrayList<CommentLine>();
        for (String comment : comments) {
            if (comment == null) {
                lines.add(new CommentLine(null, null, "", CommentType.BLANK_LINE));
            } else {
                String line = comment;
                line = line.isEmpty() ? line : " " + line;
                lines.add(new CommentLine(null, null, line, commentType));
            }
        }
        return lines;
    }

    @Override
    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
        Objects.requireNonNull(reader);
        if(!(adapterFor instanceof FileConfig))
            throw new IllegalStateException("Cannot save a non-root config!");

        FileConfig config = (FileConfig) adapterFor;
        yamlLoaderOptions.setProcessComments(config.options().parseComments());

        MappingNode node;
        try {
            node = (MappingNode) yaml.compose(reader);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        root = new NestedObjectSection(adapterFor);

        if (node != null) {
            adjustNodeComments(node);
            config.options().setHeader(loadHeader(getCommentLines(node.getBlockComments())));
            config.options().setFooter(getCommentLines(node.getEndComments()));
            fromNodeTree(node,root);
        }
    }

    @Override
    public boolean supportsCommentPreservation() {
        return true;
    }

    @Override
    public void setComments(String path, List<String> comments) {
        root.setComments(path,comments);
    }

    @Override
    public void setInlineComments(String path, List<String> comments) {
        root.setInlineComments(path,comments);
    }

    @Override
    public List<String> getInlineComments(String path) {
        return root.getInlineComments(path);
    }

    @Override
    public List<String> getComments(String path) {
        return root.getComments(path);
    }

    private void fromNodeTree(@NotNull MappingNode input, NestedObjectSection section) {
        for (NodeTuple nodeTuple : input.getValue()) {
            Node key = nodeTuple.getKeyNode();
            String keyString = String.valueOf(constructor.construct(key));
            Node value = nodeTuple.getValueNode();

            while (value instanceof AnchorNode) {
                value = ((AnchorNode) value).getRealNode();
            }

            if (value instanceof MappingNode) {
                fromNodeTree((MappingNode) value, section.createSection(keyString));
            } else {
                section.set(keyString,constructor.construct(value));
            }

            section.setComments(keyString, getCommentLines(key.getBlockComments()));
            if (value instanceof MappingNode || value instanceof SequenceNode) {
                section.setInlineComments(keyString, getCommentLines(key.getInLineComments()));
            } else {
                section.setInlineComments(keyString, getCommentLines(value.getInLineComments()));
            }
        }
    }

    private List<String> loadHeader(List<String> header) {
        LinkedList<String> list = new LinkedList<>(header);

        if (!list.isEmpty()) {
            list.removeLast();
        }

        while (!list.isEmpty() && list.peek() == null) {
            list.remove();
        }

        return list;
    }

    private List<String> getCommentLines(List<CommentLine> comments) {
        List<String> lines = new ArrayList<>();
        if (comments != null) {
            for (CommentLine comment : comments) {
                if (comment.getCommentType() == CommentType.BLANK_LINE) {
                    lines.add(null);
                } else {
                    String line = comment.getValue();
                    line = line.startsWith(" ") ? line.substring(1) : line;
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    private void adjustNodeComments(MappingNode node) {
        if (node.getBlockComments() == null && !node.getValue().isEmpty()) {
            Node firstNode = node.getValue().get(0).getKeyNode();
            List<CommentLine> lines = firstNode.getBlockComments();
            if (lines != null) {
                int index = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).getCommentType() == CommentType.BLANK_LINE) {
                        index = i;
                    }
                }
                if (index != -1) {
                    node.setBlockComments(lines.subList(0, index + 1));
                    firstNode.setBlockComments(lines.subList(index + 1, lines.size()));
                }
            }
        }
    }
}
