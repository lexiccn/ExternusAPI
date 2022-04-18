package me.deltaorion.common.config.yaml;

import com.amihaiemil.eoyaml.*;
import com.amihaiemil.eoyaml.exceptions.YamlIndentationException;
import com.google.common.base.Splitter;
import me.deltaorion.common.config.*;
import me.deltaorion.common.config.nested.ConfigObjectValue;
import me.deltaorion.common.config.nested.NestedObjectSection;
import me.deltaorion.common.config.options.FileConfigOptions;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * @author org.bukkit, deltaorion
 */
public class YamlConfigAdapter implements ConfigAdapter {

    private final ConfigSection adapterFor;
    private final NestedObjectSection root;

    public YamlConfigAdapter(@NotNull ConfigSection adapterFor) {
        this.root = new NestedObjectSection(adapterFor);
        this.adapterFor = adapterFor;
    }

    private YamlConfigAdapter(@NotNull ConfigSection adapterFor, @NotNull NestedObjectSection section) {
        this.adapterFor = adapterFor;
        this.root = section;
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
        return new ConfigSection(new YamlConfigAdapter(adapterFor,section),section.getName(),wrapMemorySection(section.getParent()),adapterFor.getRoot());
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
        YamlMappingBuilder mapping = Yaml.createYamlMappingBuilder();
        mapping = saveMapping(config,mapping,root);
        YamlMapping complete = mapping.build(config.options().getHeader());
        YamlPrinter printer = Yaml.createYamlPrinter(writer);
        printer.print(complete);
    }

    private YamlMappingBuilder saveMapping(FileConfig config, YamlMappingBuilder mapping, NestedObjectSection root) {
        for(String key : root.getKeys()) {
            Object value = root.get(key);
            if(value instanceof NestedObjectSection) {
                NestedObjectSection next = (NestedObjectSection) value;
                YamlMappingBuilder node = Yaml.createYamlMappingBuilder();
                node = saveMapping(config,node,next);
                YamlNode built;
                if(config.options().parseComments()) {
                    built = node.build(root.getComments(key));
                } else {
                    built = node.build();
                }
                mapping = mapping.add(key,built);
            } else if(value instanceof List<?>) {
                mapping = saveSequence(config,mapping,root,key);
            } else {
                 mapping =saveScalar(config,mapping,root,key);
            }
        }
        return mapping;
    }

    private YamlMappingBuilder saveScalar(FileConfig config, YamlMappingBuilder root, NestedObjectSection section, String key) {
        YamlScalarBuilder builder = Yaml.createYamlScalarBuilder();
        Object value = section.get(key);
        builder = builder.addLine(String.valueOf(value));
        YamlNode build;
        if(config.options().parseComments()) {
            StringBuilder commentMerged = new StringBuilder();
            section.getInlineComments(key).forEach(commentMerged::append);
            build = builder.buildPlainScalar(section.getComments(key),commentMerged.toString());
        } else {
            build = builder.buildPlainScalar();
        }
        return root.add(key,build);
    }

    private YamlMappingBuilder saveSequence(FileConfig config, YamlMappingBuilder root, NestedObjectSection section , String key) {
        YamlSequenceBuilder sequence = Yaml.createYamlSequenceBuilder();
        Object value = section.get(key);
        if(!(value instanceof List<?>))
            throw new IllegalArgumentException("Cannot make a sequence of a non-list");

        List<?> val = (List<?>) value;
        for(Object o : val) {
            sequence = sequence.add(String.valueOf(o));
        }

        YamlNode build;
        if(config.options().parseComments()) {
            build = sequence.build(section.getComments(key));
        } else {
            build = sequence.build();
        }

        return root.add(key,sequence.build());
    }


    @Override
    public void load(@NotNull InputStream inputStream) throws IOException, InvalidConfigurationException {
        Objects.requireNonNull(inputStream);
        if(!(adapterFor instanceof FileConfig))
            throw new IllegalStateException("Config Adapter is not a file config!");

        FileConfig config = (FileConfig) adapterFor;
        try {
            YamlMapping yaml = Yaml.createYamlInput(inputStream,false).readYamlMapping();
            readFromNode(config,root,yaml);
            readHeader(config,yaml);
        } catch (YamlIndentationException e) {
            throw new InvalidConfigurationException(e);
        }

    }

    private void readHeader(FileConfig config, YamlMapping yaml) {
        FileConfigOptions options = config.options();
        if(!options.parseComments())
            return;

        options.setHeader(parseComments(yaml.comment().value()));
        options.setFooter(new ArrayList<>());
    }

    private List<String> parseComments(String commentLiteral) {
        Splitter splitter = Splitter.on(System.lineSeparator());
        List<String> list = new ArrayList<>();
        for(String str : splitter.splitToList(commentLiteral)) {
            if(str.length()>0) {
                list.add(str);
            }
        }
        return list;
    }


    private void readFromNode(FileConfig config, NestedObjectSection root, YamlMapping mapping) {
        for(YamlNode key : mapping.keys()) {
            YamlNode value = mapping.value(key);
            if(value.type() == Node.MAPPING) {
                NestedObjectSection sect = root.createSection(key.asScalar().value());
                if(config.options().parseComments()) {
                    root.setComments(key.asScalar().value(), parseComments(value.comment().value()));
                }
                readFromNode(config,sect,value.asMapping());
            } else {
                processNode(root,key,value,config.options().parseComments());
            }
        }
    }

    private void processNode(NestedObjectSection root, YamlNode key, YamlNode value, boolean doComments) {
        if(value.type() == Node.SCALAR) {
            processScalar(root,key.asScalar(),value.asScalar(),doComments);
        } else if(value.type() == Node.SEQUENCE) {
            processSequence(root,key.asScalar(),value.asSequence(),doComments);
        } else {
            throw new UnsupportedOperationException("Unknown node type '"+value.type()+"'");
        }

    }

    private void processSequence(NestedObjectSection root, Scalar key, YamlSequence value, boolean doComments) {
        ConfigValue val = new EOYamlValue(value);
        root.set(key.value(),val.asObject());
        if(!doComments)
            return;
        root.setComments(key.value(),parseComments(value.comment().value()));
    }

    private void processScalar(NestedObjectSection root, Scalar key, Scalar value, boolean doComments) {
        ConfigValue val = new EOYamlValue(value);
        root.set(key.value(),val.asObject());

        if(!doComments)
            return;
        ScalarComment comment = (ScalarComment) value.comment();
        root.setComments(key.value(),parseComments(comment.above().value()));
        root.setInlineComments(key.value(),parseComments(comment.inline().value()));
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
}
