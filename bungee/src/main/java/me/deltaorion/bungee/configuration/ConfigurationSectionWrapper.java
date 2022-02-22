package me.deltaorion.bungee.configuration;

import me.deltaorion.common.config.ConfigSection;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ConfigurationSectionWrapper implements ConfigSection {

    @NotNull protected final Configuration configuration;

    public ConfigurationSectionWrapper(@NotNull Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        if(!deep)
            return Collections.unmodifiableSet(new HashSet<>(configuration.getKeys()));

        return Collections.unmodifiableSet(getValues(configuration,new HashMap<>(),true).keySet());
    }

    private Map<String,Object> getValues(Configuration section, Map<String,Object> keys, boolean deep) {
        for(String key : section.getKeys()) {
            Object value = section.get(key);
            keys.put(key,value);
            if(value instanceof Configuration && deep) {
                getValues((Configuration) value,keys, true);
            }
        }
        return keys;
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        return getValues(configuration,new HashMap<>(),deep);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return false;
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return false;
    }

    @Override
    public boolean isSet(@NotNull String path) {
    }

    @Override
    public String getCurrentPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ConfigSection getRoot() {
        return null;
    }

    @Override
    public ConfigSection getParent() {
        return null;
    }

    @Override
    public Object get(String path) {
        return null;
    }

    @Override
    public Object get(String path, Object def) {
        return null;
    }

    @Override
    public void set(String path, Object value) {

    }

    @Override
    public ConfigSection createSection(String path) {
        return null;
    }

    @Override
    public ConfigSection createSection(String path, Map<?, ?> map) {
        return null;
    }

    @Override
    public String getString(String path) {
        return null;
    }

    @Override
    public String getString(String path, String def) {
        return null;
    }

    @Override
    public boolean isString(String path) {
        return false;
    }

    @Override
    public int getInt(String path) {
        return 0;
    }

    @Override
    public int getInt(String path, int def) {
        return 0;
    }

    @Override
    public boolean isInt(String path) {
        return false;
    }

    @Override
    public boolean getBoolean(String path) {
        return false;
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return false;
    }

    @Override
    public boolean isBoolean(String path) {
        return false;
    }

    @Override
    public double getDouble(String path) {
        return 0;
    }

    @Override
    public double getDouble(String path, double def) {
        return 0;
    }

    @Override
    public boolean isDouble(String path) {
        return false;
    }

    @Override
    public long getLong(String path) {
        return 0;
    }

    @Override
    public long getLong(String path, long def) {
        return 0;
    }

    @Override
    public boolean isLong(String path) {
        return false;
    }

    @Override
    public List<?> getList(String path) {
        return null;
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return null;
    }

    @Override
    public boolean isList(String path) {
        return false;
    }

    @Override
    public List<String> getStringList(String path) {
        return null;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return null;
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return null;
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return null;
    }

    @Override
    public List<Float> getFloatList(String path) {
        return null;
    }

    @Override
    public List<Long> getLongList(String path) {
        return null;
    }

    @Override
    public List<Byte> getByteList(String path) {
        return null;
    }

    @Override
    public List<Character> getCharacterList(String path) {
        return null;
    }

    @Override
    public List<Short> getShortList(String path) {
        return null;
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return null;
    }

    @Override
    public ConfigSection getConfigurationSection(String path) {
        return null;
    }

    @Override
    public boolean isConfigurationSection(String path) {
        return false;
    }

    @Override
    public ConfigSection getDefaultSection() {
        return null;
    }

    @Override
    public boolean supportsNesting() {
        return true;
    }
}
