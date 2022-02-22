package me.deltaorion.common.config.file;

import me.deltaorion.common.config.FileConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

public abstract class FileConfigLoader implements ConfigLoader {

    @NotNull private final Path configurationPath;
    private FileConfig dataConfig;
    private final File configFile;
    @Nullable private final String defaultPath;
    @Nullable private final ClassLoader classLoader;


    public FileConfigLoader(@Nullable ClassLoader classLoader, @NotNull Path configurationPath, @Nullable String defaultPath) {

        Objects.requireNonNull(configurationPath);

        this.configurationPath = configurationPath;
        this.configFile = configurationPath.toFile();
        this.defaultPath = defaultPath;
        this.classLoader = classLoader;

        if(this.defaultPath!=null)
            saveDefaultConfig();
    }

    public FileConfigLoader(@Nullable ClassLoader classLoader, @NotNull Path configurationPath) {
        this(classLoader,configurationPath,null);
    }

    public FileConfigLoader(@NotNull Path configurationPath) {
        this(null,configurationPath);
    }


    @Override
    public void reloadConfig() {
        try {
            dataConfig = getFromFile(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        InputStream defaultConfig = null;

        if(this.defaultPath!=null)
           defaultConfig = getResourceStream(classLoader, defaultPath);


        if(defaultConfig!=null) {
            try {
                FileConfig def = getFromStream(defaultConfig);
                dataConfig.setDefaults(def);
                defaultConfig.close();
            } catch (IOException e) {
                System.out.println("An Error occurred when loading " + defaultConfig);
            }
        }
    }

    @NotNull
    public abstract FileConfig getFromStream(@NotNull InputStream inputStream) throws IOException;

    public abstract FileConfig getFromFile(@NotNull File file) throws IOException;

    @Override
    public FileConfig getConfig() {
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
