package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

public class PropertiesFileConfig extends PropertiesMemoryConfig implements FileConfig {
    private PropertiesFileConfig() {
        super(new Properties());
    }

    @Override
    public void save(File file) throws IOException {
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

    public static PropertiesFileConfig loadConfiguration(@NotNull InputStream stream) throws IOException {
        PropertiesFileConfig config = new PropertiesFileConfig();
        config.load(new InputStreamReader(stream));
        return config;
    }

    public static PropertiesFileConfig loadConfiguration(@NotNull Reader reader) throws IOException {
        PropertiesFileConfig config = new PropertiesFileConfig();
        config.load(reader);
        return config;
    }
}
