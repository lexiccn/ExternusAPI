package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfigAdapter implements ConfigAdapter {

    @NotNull private final Properties properties;
    @NotNull private final String FILE_EXTENSION = ".properties";
    private final String NO_NESTING_SUPPORT = FILE_EXTENSION+" does not support a nested config. The requested operation cannot be done";

    public PropertiesConfigAdapter() {
        this.properties = new Properties();
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return properties.stringPropertyNames();
    }

    @NotNull
    @Override
    public ConfigValue getValue(ConfigSection parent, @NotNull MemoryConfig root, @NotNull String path) {
        return new PropertiesValue(properties.getProperty(path));
    }

    @Override
    public void set(@NotNull String path, Object value) {
        properties.setProperty(path,String.valueOf(value));
    }

    @Override
    public void remove(@NotNull String path) {
        properties.remove(path);
    }

    @NotNull
    @Override
    public ConfigSection createSection(MemoryConfig root, ConfigSection parent , String path) {
        throw new UnsupportedOperationException(NO_NESTING_SUPPORT);
    }

    @NotNull
    @Override
    public ConfigSection createSection(MemoryConfig root, ConfigSection parent , String path, Map<String, Object> map) {
        throw new UnsupportedOperationException(NO_NESTING_SUPPORT);
    }

    @Override
    public boolean supportsNesting() {
        return false;
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        properties.store(new FileWriter(file),"");
    }

    @Override
    public void save(@NotNull Writer writer) throws IOException {
        properties.store(writer,"");
    }

    @Override
    public void load(@NotNull Reader reader) throws IOException {
        properties.load(reader);
    }
}
