package me.deltaorion.extapi.config;

import org.bukkit.configuration.file.FileConfiguration;

public interface Configuration {

    public void reloadConfig();

    public FileConfiguration getConfig();

    public void saveConfig();
}
