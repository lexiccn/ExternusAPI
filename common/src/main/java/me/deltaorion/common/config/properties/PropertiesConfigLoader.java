package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.file.FileConfigLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class PropertiesConfigLoader extends FileConfigLoader {

    public PropertiesConfigLoader(@Nullable ClassLoader classLoader, @NotNull Path configurationPath, @Nullable String defaultPath) {
        super(classLoader, configurationPath, defaultPath);
    }

    public PropertiesConfigLoader(@Nullable ClassLoader classLoader, @NotNull Path configurationPath) {
        super(classLoader, configurationPath);
    }

    public PropertiesConfigLoader(@NotNull Path configurationPath) {
        super(configurationPath);
    }

    @NotNull
    @Override
    public FileConfig getFromStream(@NotNull InputStream inputStream) throws IOException {
        return PropertiesFileConfig.loadConfiguration(inputStream);
    }

    @Override
    public FileConfig getFromFile(@NotNull File file) throws IOException {
        return PropertiesFileConfig.loadConfiguration(new FileReader(file));
    }
}
