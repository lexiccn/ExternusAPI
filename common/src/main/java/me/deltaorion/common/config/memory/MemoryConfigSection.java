package me.deltaorion.common.config.memory;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.value.ConfigObjectValue;
import me.deltaorion.common.config.value.ConfigValue;
import me.deltaorion.common.config.value.MemoryValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MemoryConfigSection implements ConfigSection {

    //properties cannot be nested, so this is just fine
    @NotNull private final MemoryConfig root;
    @Nullable private final ConfigSection parent;
    @NotNull private final String path;
    @NotNull protected final ConfigAdapter adapter;

    //private final static String FILE_EXTENSION = ".properties";
    //private final static String UNSUPPORTED_NESTED = FILE_EXTENSION+" files do not support a nested structure which means the requested operation cannot be complete";
    //private final static String UNSUPPORTED_LIST = FILE_EXTENSION + " files do not support list structure";

    public MemoryConfigSection(@NotNull ConfigAdapter adapter) {
        if(!(this instanceof MemoryConfig))
            throw new IllegalStateException("Cannot create a root section without being a memory config!");

        Objects.requireNonNull(adapter);
        this.root = (MemoryConfig) this;
        this.path = "";
        this.parent = null;
        this.adapter = adapter;
    }

    public MemoryConfigSection(@NotNull ConfigAdapter adapter, @NotNull String path, @NotNull ConfigSection parent, @NotNull MemoryConfig root) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(parent);
        Objects.requireNonNull(root);
        Objects.requireNonNull(adapter);

        this.root = root;
        this.parent = parent;
        this.path = path;
        this.adapter = adapter;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        if(!deep) {
            if(!root.options().copyDefaults())
                return adapter.getKeys();

            Set<String> keys = new HashSet<>(adapter.getKeys());
            ConfigSection defaults = getDefaultSection();
            if(defaults!=null) {
                keys.addAll(defaults.getKeys(false));
            }
            return keys;
        }

        return Collections.unmodifiableSet(getValues(true).keySet());
    }

    private ConfigSection getDefaultSection() {
        ConfigSection defaults = root.getDefaults();
        if(defaults==null)
            return null;

        if(parent==null) {
            return defaults;
        } else {
            return defaults.getConfigurationSection(path);
        }
    }



    @Override
    public Map<String, Object> getValues(boolean deep) {
        Map<String,Object> values = getValues("",this,new HashMap<>(),deep);
        return Collections.unmodifiableMap(values);
    }

    private Map<String,Object> getValues(String path, ConfigSection section, Map<String,Object> values, boolean deep) {
        for(String key : section.getKeys(false)) {
            final String absKey = path+key;
            values.put(absKey,section.get(key));
            if(deep && section.isConfigurationSection(key)) {
                getValues(absKey + String.valueOf(root.options().pathSeparator()),section.getConfigurationSection(key),values,true);
            }
        }
        return values;
    }

    @Override
    public boolean contains(@NotNull String path) {
        return contains(path,false);
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        if(getValue(path).isObject())
            return true;

        if(ignoreDefault && !root.options().copyDefaults()) {
            return false;
        }

        ConfigSection defaults = getDefaultSection();
        if(defaults==null)
            return false;

        return defaults.contains(path,ignoreDefault);
    }

    @Override
    public boolean isSet(@NotNull String path) {
        return contains(path,true);
    }

    @Override
    public String getCurrentPath() {
        return path;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public ConfigSection getRoot() {
        return root;
    }

    @Nullable
    @Override
    public ConfigSection getParent() {
        return parent;
    }

    private Object getDefault(String path) {
        Objects.requireNonNull(path);
        ConfigSection defaults = getDefaultSection();
        if(defaults==null)
            return null;

        return defaults.get(path);
    }

    @Override
    public Object get(String path) {
        return getValue(path).asObject();
    }

    @Override
    public Object get(String path, Object def) {
        return getValue(path,def).asObject();
    }

    private ConfigValue getValue(String path) {
        return getValue(path,getDefault(path));
    }

    private ConfigValue getValue(String path, Object def) {
        Objects.requireNonNull(path);
        return new MemoryValue(adapter.getValue(this,root,path),new ConfigObjectValue(path, def),root);
    }

    @Override
    public void set(String path, Object value) {
        Objects.requireNonNull(path);
        if(value==null) {
           adapter.remove(path);
        } else {
            adapter.set(path,value);
        }
    }

    @Override
    public ConfigSection createSection(String path) {
        return adapter.createSection(root,this,path);
    }


    @Override
    public ConfigSection createSection(String path, Map<String, Object> map) {
        return adapter.createSection(root,this,path,map);
    }

    @Override
    public String getString(String path) {
        return getValue(path).asString();
    }

    @Override
    public String getString(String path, String def) {
        return getValue(path).asString();
    }

    @Override
    public boolean isString(String path) {
        return getValue(path).isString();
    }

    @Override
    public int getInt(String path) {
        return getValue(path).asInt();
    }

    @Override
    public int getInt(String path, int def) {
        return getValue(path,def).asInt();
    }

    @Override
    public boolean isInt(String path) {
        return getValue(path).isInt();
    }

    @Override
    public boolean getBoolean(String path) {
        return getValue(path).asBoolean();
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return getValue(path,def).asBoolean();
    }

    @Override
    public boolean isBoolean(String path) {
        return getValue(path).isBoolean();
    }

    @Override
    public double getDouble(String path) {
        return getValue(path).asDouble();
    }

    @Override
    public double getDouble(String path, double def) {
        return getValue(path,def).asDouble();
    }

    @Override
    public boolean isDouble(String path) {
        return getValue(path).isDouble();
    }

    @Override
    public long getLong(String path) {
        return getValue(path).asLong();
    }

    @Override
    public long getLong(String path, long def) {
        return getValue(path,def).asLong();
    }

    @Override
    public boolean isLong(String path) {
        return getValue(path).isLong();
    }

    @Override
    public List<?> getList(String path) {
        return getValue(path).asList();
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return getValue(path,def).asList();
    }

    @Override
    public boolean isList(String path) {
        return getValue(path).isList();
    }

    @Override
    public List<String> getStringList(String path) {
        return getValue(path).asStringList();
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return getValue(path).asIntegerList();
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return getValue(path).asBooleanList();
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return getValue(path).asDoubleList();
    }

    @Override
    public List<Float> getFloatList(String path) {
        return getValue(path).asFloatList();
    }

    @Override
    public List<Long> getLongList(String path) {
        return getValue(path).asLongList();
    }

    @Override
    public List<Byte> getByteList(String path) {
        return getValue(path).asByteList();
    }

    @Override
    public List<Character> getCharacterList(String path) {
        return getValue(path).asCharacterList();
    }

    @Override
    public List<Short> getShortList(String path) {
        return getValue(path).asShortList();
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return getValue(path).asMapList();
    }

    @Override
    public ConfigSection getConfigurationSection(String path) {
        return getValue(path).asConfigSection();
    }

    @Override
    public boolean isConfigurationSection(String path) {
        return getValue(path).isConfigSection();
    }

    @Override
    public boolean supportsNesting() {
        return adapter.supportsNesting();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path",path)
                .add("values",getValues(true)).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ConfigSection))
            return false;

        ConfigSection section = (ConfigSection) o;
        if(!section.getCurrentPath().equals(this.getCurrentPath()))
            return false;

        return section.getValues(true).equals(this.getValues(true));
    }

}
