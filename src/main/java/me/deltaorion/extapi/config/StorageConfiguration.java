package me.deltaorion.extapi.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

public class StorageConfiguration implements Configuration {

    private final static String EXTENSION = ".yml";

    private final Path configurationPath;
    private File configFile;
    private FileConfiguration dataConfig;
    private URL defaultConfig;

    public StorageConfiguration(Path configurationPath, URL defaultConfig) {
        this.configurationPath = configurationPath;
        this.configFile = configurationPath.toFile();
        this.defaultConfig = defaultConfig;
        saveDefaultConfig();
    }

    public StorageConfiguration(Path configurationPath) {
        this.configurationPath = configurationPath;
        this.defaultConfig = null;
        this.configFile = configurationPath.toFile();
        saveDefaultConfig();
    }

    public StorageConfiguration(Path configurationDirectory, String fileName, URL defaultConfig)  {
        this(configurationDirectory.resolve(fileName + EXTENSION),defaultConfig);
    }

    public StorageConfiguration(Path configurationDirectory, String fileName) {
        this(configurationDirectory,fileName,null);
    }



    @Override
    public void reloadConfig() {
        dataConfig = YamlConfiguration.loadConfiguration(configFile);

        if(defaultConfig!=null) {
            YamlConfiguration def = null;
            try {
                def = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfig.openStream()));
                dataConfig.setDefaults(def);
            } catch (IOException e) {
                System.out.println("An Error occurred when loading "+defaultConfig);
            }
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
            saveResource(defaultConfig);
        }
    }

    private void saveResource(URL defaultStream) {
        if(defaultStream==null)
            throw new IllegalArgumentException("Config File '"+configurationPath.toAbsolutePath().toString()+"' could not be found and there is no default config specified.");

        File outDirectory = configFile.getParentFile();
        File outFile = configFile;

        if(!outDirectory.exists())
            outDirectory.mkdirs();

        try {
            InputStream inputStream = defaultStream.openStream();
            if (!outFile.exists()) {
                Files.copy(
                        inputStream,
                        outFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException ex) {
            System.out.println( "Could not save " + outFile.getName() + " to " + outFile);
            ex.printStackTrace();
        }


    }
}
