package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesConfigAdapter implements ConfigAdapter {

    @NotNull private final Properties properties;
    @NotNull private final String FILE_EXTENSION = ".properties";
    private final String NO_NESTING_SUPPORT = FILE_EXTENSION+" does not support a nested config. The requested operation cannot be done";
    @NotNull private final ConfigSection adapterFor;

    public PropertiesConfigAdapter(@NotNull ConfigSection adapterFor) {
        this.properties = new Properties();
        this.adapterFor = adapterFor;
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return properties.stringPropertyNames();
    }

    @NotNull
    @Override
    public ConfigValue getValue(@NotNull String path) {
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
    public ConfigSection createSection(@NotNull String path) {
        throw new UnsupportedOperationException(NO_NESTING_SUPPORT);
    }

    @NotNull
    @Override
    public ConfigSection createSection(@NotNull String path, Map<String ,Object> map) {
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

    @Override
    public boolean supportsCommentPreservation() {
        return false;
    }

    @Override
    public void setComments(String path, List<String> comments) {
        throw new UnsupportedOperationException("Adapter does not support comments");
    }

    @Override
    public void setInlineComments(String path, List<String> comments) {
        throw new UnsupportedOperationException("Adapter does not support comments");
    }

    @Override
    public List<String> getInlineComments(String path) {
        throw new UnsupportedOperationException("Adapter does not support comments");
    }

    @Override
    public List<String> getComments(String path) {
        throw new UnsupportedOperationException("Adapter does not support comments");
    }
}
