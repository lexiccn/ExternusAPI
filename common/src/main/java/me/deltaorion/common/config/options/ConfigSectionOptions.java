package me.deltaorion.common.config.options;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

/**
 * @author org.bukkit, DeltaOrion
 */
public class ConfigSectionOptions {

    @NotNull private final MemoryConfig root;
    private boolean copyDefaults;
    private char pathSeparator;

    public ConfigSectionOptions(@NotNull MemoryConfig root) {
        this.root = root;
        this.copyDefaults = false;
        this.pathSeparator = '.';
    }

    /**
     * Returns the {@link FileConfig} that this object is responsible for.
     *
     * @return Parent configuration
     */
    
    public MemoryConfig configuration() {
        return root;
    }

    /**
     * Gets the char that will be used to separate {@link
     * FileConfig}s
     * <p>
     * This value does not affect how the {@link FileConfig} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
    
    public char pathSeparator() {
        return pathSeparator;
    }

    /**
     * Sets the char that will be used to separate {@link
     * me.deltaorion.common.config.ConfigSection}s
     * <p>
     * This value does not affect how the {@link FileConfig} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @param value Path separator
     * @return This object, for chaining
     */
    
    public ConfigSectionOptions pathSeparator(char value) {
        this.pathSeparator = value;
        return this;
    }

    /**
     * Checks if the {@link FileConfig} should copy values from its default
     * {@link FileConfig} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link me.deltaorion.common.config.ConfigSection#contains(java.lang.String)} will always
     * return the same value as {@link
     * FileConfig#isSet(java.lang.String)}. The default value is
     * false.
     *
     * @return Whether or not defaults are directly copied
     */
    
    public boolean copyDefaults() {
        return copyDefaults;
    }

    /**
     * Sets if the {@link FileConfig} should copy values from its default
     * {@link FileConfig} directly.
     * <p>
     * If this is true, all values in the default Configuration will be
     * directly copied, making it impossible to distinguish between values
     * that were set and values that are provided by default. As a result,
     * {@link me.deltaorion.common.config.ConfigSection#contains(String)} will always
     * return the same value as {@link
     * me.deltaorion.common.config.ConfigSection#isSet(java.lang.String)}. The default value is
     * false.
     *
     * @param value Whether or not defaults are directly copied
     * @return This object, for chaining
     */
    
    public ConfigSectionOptions copyDefaults(boolean value) {
        this.copyDefaults = value;
        return this;
    }

    
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("copy-defaults",copyDefaults)
                .add("path-separator",pathSeparator)
                .toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof ConfigSectionOptions))
            return false;

        ConfigSectionOptions options = (ConfigSectionOptions) o;
        return options.copyDefaults() == this.copyDefaults() && options.pathSeparator() == this.pathSeparator();
    }
}
