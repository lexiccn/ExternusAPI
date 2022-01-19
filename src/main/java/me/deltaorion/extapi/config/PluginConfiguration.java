package me.deltaorion.extapi.config;

import me.deltaorion.extapi.common.plugin.EPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

/**
 * This class is a wrapper for a FileConfiguration that automatically does the file management of the configuration. This class
 * will assume that the default config is located inside of the plugins resource folder at the location 'fileName'. It will then
 * save the default config if it does not exist to the plugin folder in the server file system.
 */

public class PluginConfiguration implements Configuration {

    @NotNull private final Path configurationPath;
    private FileConfiguration dataConfig;
    private final File configFile;
    @NotNull private final EPlugin plugin;
    @NotNull private final String fileName;

    public PluginConfiguration(@NotNull EPlugin plugin, @NotNull String fileName) {

        Validate.notNull(plugin);
        Validate.notNull(fileName);

        if(!fileName.endsWith(EXTENSION))
            fileName = fileName + EXTENSION;

        this.configurationPath = plugin.getDataDirectory().resolve(fileName);
        this.plugin = plugin;
        this.configFile = configurationPath.toFile();
        this.fileName = fileName;

        saveDefaultConfig();
    }




    @Override
    public void reloadConfig() {
        dataConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultConfig = plugin.getResourceStream(fileName);
        if(defaultConfig!=null) {
            YamlConfiguration def = null;
            try {
                def = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfig));
                dataConfig.setDefaults(def);
                defaultConfig.close();
            } catch (IOException e) {
                System.out.println("An Error occurred when loading " + defaultConfig);
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
            plugin.saveResource(configurationPath.toString(),false);
        }
    }
}
