package me.deltaorion.extapi.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This class acts as a wrapper for a file configuration. It assumes that the file is located in the resource folder
 * and that it should only be in the resource folder, never to be saved to the file system.
 */

public class ResourceConfiguration implements Configuration {

    private final String resourcePath;
    private FileConfiguration dataConfig;
    private final ClassLoader classLoader;

    public ResourceConfiguration(@NotNull String resourcePath, @Nullable ClassLoader classLoader) {
        Validate.notNull(resourcePath);
        this.resourcePath = resourcePath;
        this.classLoader = classLoader;
    }

    @Override
    public void reloadConfig() {

        InputStream inputStream = getResourceStream(classLoader,resourcePath);
        Validate.notNull(inputStream,"Default resource config at '"+resourcePath+"' could not be found!");

        try {
            dataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error Loading '"+resourcePath+"'");
        }
    }

    @Override
    public FileConfiguration getConfig() {
        if(dataConfig==null) {
            reloadConfig();
        }
        return dataConfig;
    }

    @Override
    public void saveConfig() {
        throw new UnsupportedOperationException("Cannot save URL Configurations");
    }
}
