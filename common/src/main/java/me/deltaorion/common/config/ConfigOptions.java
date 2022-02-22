package me.deltaorion.common.config;

/**
 * @author org.bukkit, DeltaOrion
 */
public interface ConfigOptions {

    /**
     * Returns the {@link FileConfig} that this object is responsible for.
     *
     * @return Parent configuration
     */
    public MemoryConfig configuration();

    /**
     * Gets the char that will be used to separate {@link
     * FileConfig}s
     * <p>
     * This value does not affect how the {@link FileConfig} is stored,
     * only in how you access the data. The default value is '.'.
     *
     * @return Path separator
     */
    public char pathSeparator();

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
    public ConfigOptions pathSeparator(char value);

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
    public boolean copyDefaults();

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
    public ConfigOptions copyDefaults(boolean value);

}
