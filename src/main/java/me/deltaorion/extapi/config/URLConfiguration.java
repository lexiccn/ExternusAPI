package me.deltaorion.extapi.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URLConfiguration implements Configuration {

    private final URL configurationPath;
    private FileConfiguration dataConfig;

    public URLConfiguration(URL configurationPath) {
        Validate.notNull(configurationPath);

        this.configurationPath = configurationPath;
    }

    @Override
    public void reloadConfig() {
        try {
            dataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(configurationPath.openStream()));
        } catch (IOException e) {
            System.out.println("Error Loading '"+configurationPath+"'");
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
