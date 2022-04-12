package me.deltaorion.common.config;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.options.FileConfigOptions;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

/**
 * @author org.bukkit, DeltaOrion
 */
public class FileConfig extends MemoryConfig {
    private FileConfig(@NotNull AdapterFactory adapter) {
        super(adapter);
    }

    /**
     * Saves the configuration to the given file
     *
     *    - All values should be saved in the order in which they appear
     *   - Will save comments if {@link #supportsCommentPreservation()} is true
     *   - Will save nested structures if {@link #supportsNesting()} is true
     *
     * @param file The file to save to
     * @throws IOException If an error occurs while saving the file
     */
    
    public void save(@NotNull File file) throws IOException {
        Objects.requireNonNull(file);
        adapter.save(file);
    }

    /**
     * Saves the configuration to the given writer.
     *
     *   - All values should be saved in the order in which they appear
     *   - Will save comments if {@link #supportsCommentPreservation()} is true
     *   - Will save nested structures if {@link #supportsNesting()} is true
     *
     * @param writer the writer to save using
     * @throws IOException If an error occurs while saving the file
     */
    
    public void save(@NotNull Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        adapter.save(writer);
    }

    /**
     * Loads a configuration from the given reader.
     *
     *    - all values should be loaded in order in which they appear in the file
     *    - Should load comments in {@link #supportsCommentPreservation()} is true
     *    - Should load all nested structures if {@link #supportsNesting()} is true
     *
     * @param inputStream the reader to load from
     * @throws IOException If an error occurs while reading the IOStream
     * @throws InvalidConfigurationException if there is a syntax error in the configuration
     */
    
    public void load(@NotNull InputStream inputStream) throws IOException, InvalidConfigurationException {
        Objects.requireNonNull(inputStream);
        adapter.load(inputStream);
    }

    /**
     * Loads a configuration from the given input stream
     *
     *    - all values should be loaded in order in which they appear in the file
     *    - Should load comments in {@link #supportsCommentPreservation()} is true
     *    - Should load all nested structures if {@link #supportsNesting()} is true
     *
     *
     * @param adapter Which configuration type is being loaded
     * @param stream The input stream containing the configuration data
     * @return A new memory file config with all of the configuration data loaded
     * @throws IOException If an error occurs while loading the config from the input stream
     * @throws InvalidConfigurationException If there is a syntactical error in the configuration.
     */
    @NotNull
    public static FileConfig loadConfiguration(@NotNull AdapterFactory adapter, @NotNull InputStream stream) throws IOException, InvalidConfigurationException {
        FileConfig config = new FileConfig(adapter);
        config.load(stream);
        return config;
    }

    @NotNull
    public static FileConfig loadConfiguration(@NotNull AdapterFactory adapter, @NotNull File file) throws IOException, InvalidConfigurationException {
        FileConfig config = new FileConfig(adapter);
        config.load(new FileInputStream(file));
        return config;
    }

    
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("adapter",adapter)
                .add("values",getValues(true))
                .toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof FileConfig))
            return false;

        FileConfig config = (FileConfig) o;
        return config.getValues(true).equals(this.getValues(true));
    }

    /**
     * Gets the {@link FileConfigOptions} for this {@link ConfigLoader}.
     * <p>
     * All setters through this method are chainable.
     *
     * @return Options for this configuration
     */
    @NotNull
    public FileConfigOptions options() {
        return options;
    }
}