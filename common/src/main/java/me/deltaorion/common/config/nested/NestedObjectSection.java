package me.deltaorion.common.config.nested;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import me.deltaorion.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author org.bukkit, DeltaOrion
 */
public class NestedObjectSection {

    @NotNull protected Map<String, SectionPathData> map;
    private final ConfigSection adapterFor;
    @NotNull private final String path;
    @Nullable private final NestedObjectSection parent;

    public NestedObjectSection(@NotNull ConfigSection adapterFor) {
        this(adapterFor,"");
    }

    private NestedObjectSection(@NotNull ConfigSection adapterFor, @NotNull String path) {
        this(adapterFor,path,null);
    }

    public NestedObjectSection(ConfigSection adapterFor, @NotNull String path, @Nullable NestedObjectSection parent) {
        this.map = new LinkedHashMap<>();
        this.adapterFor = adapterFor;
        this.path = path;
        this.parent = parent;
    }


    @NotNull
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public Map<String,Object> getValues() {
        Map<String,Object> values = new HashMap<>();
        for(String key : getKeys()) {
            SectionPathData data = getSectionPathData(key);
            values.put(key,data == null ? null : data.getData());
        }
        return Collections.unmodifiableMap(values);
    }

    public Object get(@NotNull String path) {
        SectionPathData data = getSectionPathData(path);
        return data == null ? null : data.getData();
    }

    @Nullable
    private SectionPathData getSectionPathData(@NotNull String path) {
        Objects.requireNonNull(path, "Path cannot be null");

        if (path.length() == 0) {
            return new SectionPathData(this);
        }

        final char separator = adapterFor.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        Map<String,SectionPathData> root = map;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            final String currentPath = path.substring(i2, i1);
            SectionPathData data = root.get(currentPath);
            Object value = data == null ? null : data.getData();
            if (!(value instanceof NestedObjectSection)) {
                return null;
            }
            root = ((NestedObjectSection) value).map;
        }

        String key = path.substring(i2);
        return root.get(key);
    }

    public void set(@NotNull String path, Object value) {
        if(path.length()==0)
            throw new IllegalArgumentException("Cannot set the root path!");


        final char separator = adapterFor.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        NestedObjectSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            Object subSection = section.get(node);
            if (!(subSection instanceof NestedObjectSection)) {
                if (value == null) {
                    // no need to create missing sub-sections if we want to remove the value:
                    return;
                }
                section = section.createSection(node);
            } else {
                section = (NestedObjectSection) subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            if (value == null) {
                map.remove(key);
            } else {
                SectionPathData entry = map.get(key);
                if (entry == null) {
                    map.put(key, new SectionPathData(value));
                } else {
                    entry.setData(value);
                }
            }
        } else {
            section.set(key, value);
        }
    }

    public NestedObjectSection createSection(@NotNull String path) {
        return createSection(path,Collections.emptyMap());
    }

    public NestedObjectSection createSection(String path, Map<String,Object> map) {
        Preconditions.checkArgument(path.length()!=0, "Cannot create section at empty path");

        final char separator = adapterFor.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        NestedObjectSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            Object subSection = section.get(node);
            if(!(subSection instanceof NestedObjectSection)) {
                section = section.createSection(node);
            } else {
                section = (NestedObjectSection) subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            NestedObjectSection result = new NestedObjectSection(adapterFor, key,section);
            this.map.put(key, new SectionPathData(result));
            for(Map.Entry<String,Object> entry : map.entrySet()) {
                result.set(entry.getKey(),entry.getValue());
            }
            return result;
        }
        return section.createSection(key,map);
    }

    public void setComments(String path, List<String> comments) {
        SectionPathData data = getSectionPathData(path);
        if(data==null)
            return;

        data.setComments(comments);
    }

    public void setInlineComments(String path, List<String> comments) {
        SectionPathData data = getSectionPathData(path);
        if(data==null)
            return;

        data.setInlineComments(comments);
    }

    public List<String> getInlineComments(String path) {
        SectionPathData data = getSectionPathData(path);
        if(data==null)
            return Collections.emptyList();

        return data.getInlineComments();
    }

    public List<String> getComments(String path) {
        SectionPathData data = getSectionPathData(path);
        if(data==null)
            return Collections.emptyList();

        return data.getComments();
    }

    @Nullable
    public NestedObjectSection getParent() {
        return parent;
    }

    public String getPath() {
        String path = this.path;
        NestedObjectSection root = parent;
        while(root!=null) {
            path = parent.getName() + adapterFor.options().pathSeparator() + path;
            root = root.getParent();
        }
        return path;
    }

    public String getName() {
        return path;
    }
}
