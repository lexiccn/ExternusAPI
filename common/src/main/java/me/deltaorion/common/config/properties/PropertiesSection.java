package me.deltaorion.common.config.properties;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.value.ConfigObjectValue;
import me.deltaorion.common.config.value.ConfigValue;
import me.deltaorion.common.config.value.MemoryValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PropertiesSection implements ConfigSection {

    //properties cannot be nested, so this is just fine
    @NotNull protected final Properties properties;
    @NotNull private final MemoryConfig root;

    private final static String FILE_EXTENSION = ".properties";
    private final static String UNSUPPORTED_NESTED = FILE_EXTENSION+" files do not support a nested structure which means the requested operation cannot be complete";
    private final static String UNSUPPORTED_LIST = FILE_EXTENSION + " files do not support list structure";

    public PropertiesSection(@NotNull Properties properties) {
        if(!(this instanceof MemoryConfig))
            throw new IllegalStateException("Cannot create a root section without being a memory config!");

        this.root = (MemoryConfig) this;
        this.properties = properties;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        if(root.options().copyDefaults()) {
            Set<String> keys = new HashSet<>(this.properties.stringPropertyNames());
            if(root.getDefaults()!=null)
                keys.addAll(root.getDefaults().getKeys(deep));
            return Collections.unmodifiableSet(keys);
        } else {
            return properties.stringPropertyNames();
        }
    }



    @Override
    public Map<String, Object> getValues(boolean deep) {
        Map<String,Object> values = getThisValues();
        if(root.options().copyDefaults()) {
            MemoryConfig defaults = root.getDefaults();
            if(defaults!=null)
                values.putAll(defaults.getValues(deep));
        }

        return Collections.unmodifiableMap(values);
    }

    private Map<String, Object> getThisValues() {
        Map<String,Object> values = new HashMap<>();
        for(String property : properties.stringPropertyNames()) {
            values.put(property,properties.getProperty(property));
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

        MemoryConfig defaults = root.getDefaults();
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
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public ConfigSection getRoot() {
        return root;
    }

    @Override
    public ConfigSection getParent() {
        return null;
    }

    private Object getDefault(String path) {
        Objects.requireNonNull(path);
        MemoryConfig defaults = root.getDefaults();
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
        String value = properties.getProperty(path);
        return new MemoryValue(new PropertiesValue(value),new ConfigObjectValue(def),root);
    }

    @Override
    public void set(String path, Object value) {
        Objects.requireNonNull(path);
        if(value==null) {
            properties.remove(path);
        } else {
            properties.setProperty(path, String.valueOf(value));
        }
    }

    @Override
    public ConfigSection createSection(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_NESTED);
    }


    @Override
    public ConfigSection createSection(String path, Map<?, ?> map) {
        throw new UnsupportedOperationException(UNSUPPORTED_NESTED);
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

    //remembering all properties are strings
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
        return getValue(path).asLong();
    }

    @Override
    public boolean isLong(String path) {
        return getValue(path).isLong();
    }

    @Override
    public List<?> getList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public boolean isList(String path) {
        return false;
    }

    @Override
    public List<String> getStringList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Float> getFloatList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Long> getLongList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Byte> getByteList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Character> getCharacterList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Short> getShortList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_LIST);
    }

    @Override
    public ConfigSection getConfigurationSection(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_NESTED);
    }

    @Override
    public boolean isConfigurationSection(String path) {
        throw new UnsupportedOperationException(UNSUPPORTED_NESTED);
    }

    @Override
    public ConfigSection getDefaultSection() {
        throw new UnsupportedOperationException(UNSUPPORTED_NESTED);
    }

    @Override
    public boolean supportsNesting() {
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("properties",properties).toString();
    }

    @Override
    public boolean equals(Object o) {
        Properties p;
        if(o instanceof  Properties) {
            p = (Properties) o;
        } else if(o instanceof PropertiesSection) {
            p = ((PropertiesSection) o).properties;
        } else {
            return false;
        }

        return p.equals(properties);
    }
}
