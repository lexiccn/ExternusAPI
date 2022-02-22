package me.deltaorion.common.config;

import me.deltaorion.common.config.file.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author org.bukkit, DeltaOrion
 */
public interface FileConfig extends MemoryConfig {

    /**
     * Gets the {@link ConfigOptions} for this {@link ConfigLoader}.
     * <p>
     * All setters through this method are chainable.
     *
     * @return Options for this configuration
     */
    public ConfigOptions options();

    /**
     * Saves the configuration to the given file
     *
     * @param file The file to save to
     * @throws IOException If an error occurs while saving the file
     */
    public void save(File file) throws IOException;

    public void save(Writer writer) throws IOException;

    /**
     * Loads a configuration from the given input stream
     *
     * @param reader
     * @throws IOException
     */
    public void load(Reader reader) throws IOException;
}

