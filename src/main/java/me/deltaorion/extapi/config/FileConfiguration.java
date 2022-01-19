package me.deltaorion.extapi.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * This class is a wrapper for a FileConfiguration and does the File Management of the Configuration class. It assumes
 * that the resource is located in an objects class path. It will then save the resource at the specified path if
 * it does not exist.
 */

public class FileConfiguration implements Configuration {

    @NotNull private final Path configurationPath;
    private org.bukkit.configuration.file.FileConfiguration dataConfig;
    private final File configFile;
    @Nullable private final String defaultPath;
    @Nullable private final Class<?> classLoader;


    public FileConfiguration(@Nullable Class<?> classLoader, @NotNull Path configurationPath, @Nullable String defaultPath) {

        Validate.notNull(configurationPath);

        if(defaultPath!=null)
            if(!defaultPath.endsWith(EXTENSION))
                defaultPath = defaultPath + EXTENSION;

        this.configurationPath = configurationPath;
        this.configFile = configurationPath.toFile();
        this.defaultPath = defaultPath;
        this.classLoader = classLoader;

        if(this.defaultPath!=null)
            saveDefaultConfig();
    }

    public FileConfiguration(@Nullable Class<?> classLoader, @NotNull Path configurationPath) {
        this(classLoader,configurationPath,null);
    }

    public FileConfiguration(@NotNull Path configurationPath) {
        this(null,configurationPath);
    }


    @Override
    public void reloadConfig() {
        dataConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultConfig = null;

        if(this.defaultPath!=null)
           defaultConfig = getResourceStream(classLoader, defaultPath);


        if(defaultConfig!=null) {
            YamlConfiguration def = null;
            try {
                def = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfig));
                dataConfig.setDefaults(def);
                defaultConfig.close();
            } catch (IOException e) {
                System.out.println("An Error occurred when loading " + defaultConfig);
            }
        } else {
        }
    }

    @Override
    public org.bukkit.configuration.file.FileConfiguration getConfig() {
        if(dataConfig==null) {
            reloadConfig();
        }
        return dataConfig;
    }

    @Override
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



    private void saveDefaultConfig() {

        if(!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }

        if(!configFile.exists()) {
            saveResource(defaultPath);
        }
    }

    private void saveResource(String resourceName) {

        InputStream defaultStream = getResourceStream(classLoader,resourceName);

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
