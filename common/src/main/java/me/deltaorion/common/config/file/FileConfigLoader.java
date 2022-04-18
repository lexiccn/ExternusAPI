package me.deltaorion.common.config.file;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * This class is a wrapper for a {@link FileConfig} acting as a file management system for the configuration file. This class
 * assumes that the config is initially located in a file in some classloaders resource folder. This will be considered the default config.
 * A file (if it does not exist) will be created and all of the defaults will be copied to that file.
 */

public class FileConfigLoader implements ConfigLoader {

    @NotNull private final Path configurationPath;
    private FileConfig dataConfig;
    private final File configFile;
    @NotNull private final String defaultPath;
    @NotNull private final ClassLoader classLoader;
    @NotNull private final AdapterFactory adapter;


    public FileConfigLoader(@NotNull AdapterFactory adapter, @NotNull ClassLoader classLoader, @NotNull Path configurationDirectory, @NotNull String configName) {
        Objects.requireNonNull(configurationDirectory);
        Objects.requireNonNull(adapter);

        this.configurationPath = configurationDirectory;
        this.configFile = configurationDirectory.resolve(configName).toFile();
        this.defaultPath = configName;
        this.classLoader = classLoader;
        this.adapter = adapter;
    }

    @Override
    public void reloadConfig() throws InvalidConfigurationException,IOException {
        try {
            dataConfig = FileConfig.loadConfiguration(adapter,configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        InputStream defaultConfig = null;

        defaultConfig = classLoader.getResourceAsStream(defaultPath);

        if(defaultConfig==null)
            throw new IllegalStateException("Cannot load the config as the default config does not exist");

        FileConfig def = FileConfig.loadConfiguration(adapter,defaultConfig);
        dataConfig.setDefaults(def);
        defaultConfig.close();
    }


    @Override
    public FileConfig getConfig() {
        if(dataConfig==null) {
            try {
                reloadConfig();
            } catch (InvalidConfigurationException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return dataConfig;
    }

    public void saveConfig() {
        if(dataConfig==null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDefaultConfig() {

        if(!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        if(!configFile.exists()) {
            saveResource(defaultPath);
        }
    }

    private void saveResource(String resourceName) {

        InputStream defaultStream = classLoader.getResourceAsStream(resourceName);

        if(defaultStream==null)
            throw new IllegalArgumentException("Cannot save '"+configurationPath.toAbsolutePath().toString()+"' as the default config '" + resourceName + "' does not exist!");

        File outDirectory = configFile.getParentFile();
        File outFile = configFile;

        if(!outDirectory.exists())
            outDirectory.mkdirs();

        try {
            if (!outFile.exists()) {
                Files.copy(
                        defaultStream,
                        outFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException ex) {
            System.out.println( "Could not save " + outFile.getName() + " to " + outFile);
            ex.printStackTrace();
        } finally {
            try {
                defaultStream.close();
            } catch (IOException ignored) {

            }
        }
    }
}
