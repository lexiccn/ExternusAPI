package me.deltaorion.common.config.file;

import me.deltaorion.common.config.FileConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

/**
 * A configuration is a file management system and wrapper for a Configuration. This does not provide systems to interact
 * with config files. For that the Bukkit class {@link FileConfigLoader} is used. Each subclass provides its own method in storing and managing
 * both the file seen in the their file system.
 *
 * Subclasses
 *    - FileConfiguration
 */

public interface ConfigLoader {

    /**
     * Reads the config file from file system and loads all values into memory. If a default config has been specified then it will attempt
     * to load defaults from that.
     */
    public void reloadConfig();

    /**
     * Retrieves an object representing the configuration. If the config has not been loaded yet, then it will load it from the file system/default config
     * into memory
     *
     * @return An object representing a config.
     */
    public FileConfig getConfig();

    /**
     * Saves the config to the file system.
     */

    public void saveConfig();

    @Nullable
    default InputStream getResourceStream(@Nullable ClassLoader loader,@NotNull String path) {
        if(loader == null) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        } else {
            return loader.getResourceAsStream(path);
        }
    }
}
