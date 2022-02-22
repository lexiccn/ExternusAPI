package me.deltaorion.bukkit.configuration;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigSection;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ConfigSectionWrapper implements ConfigSection {

    @NotNull private final ConfigurationSection section;

    public ConfigSectionWrapper(@NotNull ConfigurationSection section) {
        this.section = section;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return section.getKeys(deep);
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        return section.getValues(deep);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return section.contains(path);
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return section.contains(path,ignoreDefault);
    }

    @Override
    public boolean isSet(@NotNull String path) {
        return section.isSet(path);
    }

    @Override
    public String getCurrentPath() {
        return section.getCurrentPath();
    }

    @Override
    public String getName() {
        return section.getName();
    }

    @Override
    public ConfigSection getRoot() {
        return wrapSection(section.getRoot());
    }

    @Nullable
    private ConfigSection wrapSection(ConfigurationSection s) {
        if(s==null)
            return null;

        return new ConfigSectionWrapper(s);
    }

    @Override
    public ConfigSection getParent() {
        return wrapSection(section.getParent());
    }

    @Override
    public Object get(String path) {
        return section.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        return section.get(path,def);
    }

    @Override
    public void set(String path, Object value) {
        section.set(path,value);
    }

    @Override
    public ConfigSection createSection(String path) {
        return wrapSection(section.createSection(path));
    }

    @Override
    public ConfigSection createSection(String path, Map<?, ?> map) {
        return wrapSection(section.createSection(path,map));
    }

    @Override
    public String getString(String path) {
        return getString(path);
    }

    @Override
    public String getString(String path, String def) {
        return section.getString(path,def);
    }

    @Override
    public boolean isString(String path) {
        return section.isString(path);
    }

    @Override
    public int getInt(String path) {
        return section.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return section.getInt(path,def);
    }

    @Override
    public boolean isInt(String path) {
        return section.isInt(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return section.getBoolean(path);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return section.getBoolean(path,def);
    }

    @Override
    public boolean isBoolean(String path) {
        return section.isBoolean(path);
    }

    @Override
    public double getDouble(String path) {
        return section.getDouble(path);
    }

    @Override
    public double getDouble(String path, double def) {
        return section.getDouble(path,def);
    }

    @Override
    public boolean isDouble(String path) {
        return section.isDouble(path);
    }

    @Override
    public long getLong(String path) {
        return section.getLong(path);
    }

    @Override
    public long getLong(String path, long def) {
        return section.getLong(path,def);
    }

    @Override
    public boolean isLong(String path) {
        return section.isLong(path);
    }

    @Override
    public List<?> getList(String path) {
        return section.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return section.getList(path,def);
    }

    @Override
    public boolean isList(String path) {
        return section.isList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return section.getStringList(path);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return section.getIntegerList(path);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return section.getBooleanList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return section.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return section.getFloatList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        return section.getLongList(path);
    }

    @Override
    public List<Byte> getByteList(String path) {
        return section.getByteList(path);
    }

    @Override
    public List<Character> getCharacterList(String path) {
        return section.getCharacterList(path);
    }

    @Override
    public List<Short> getShortList(String path) {
        return section.getShortList(path);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return section.getMapList(path);
    }

    @Override
    public ConfigSection getConfigurationSection(String path) {
        return wrapSection(section.getConfigurationSection(path));
    }

    @Override
    public boolean isConfigurationSection(String path) {
        return section.isConfigurationSection(path);
    }

    @Override
    public ConfigSection getDefaultSection() {
        return wrapSection(section.getDefaultSection());
    }

    @Override
    public boolean supportsNesting() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        ConfigurationSection otherSection = null;
        if(o instanceof ConfigSectionWrapper) {
            otherSection = ((ConfigSectionWrapper) o).section;
        } else if(o instanceof ConfigurationSection) {
            otherSection = (ConfigurationSection) o;
        } else {
            return false;
        }
        return Objects.equals(otherSection,this.section);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wrapped",this.section)
                .toString();
    }
}
