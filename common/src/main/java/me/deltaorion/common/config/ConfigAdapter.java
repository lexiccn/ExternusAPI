package me.deltaorion.common.config;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.options.FileConfigOptions;
import me.deltaorion.common.config.ConfigValue;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A config adapter provides basic mechanisms for reading and writing to any configuration type such as Yaml, Json or Properties. Each
 * config type may or may not support nesting and may not may not support comments, or the preservation of comments each time the config is
 * loaded.
 *
 * A configuration maps keys to values. Each key has a corresponding value given to it and that value can be represented as a data type. In a
 * nested configuration it is possible for a key to map to another key. To identify nested paths a separator value is used with each string between
 * the separators identifying the next section.
 *
 * For nest configuration sections the config adapter should act as a config section, and not simply as the root config.
 *
 * Some subclasses are
 *   - {@link me.deltaorion.common.config.yaml.YamlConfigAdapter}
 *   - {@link me.deltaorion.common.config.properties.PropertiesAdapter}
 */
public interface ConfigAdapter {

    /**
     * Return all the shallow keys in the config section. That means it does NOT perform a search on any keys that any of the keys may have.
     *
     * @return The shallow keys in the config section
     */
    @NotNull
    Set<String> getKeys();

    /**
     * Returns the value mapped to the key. If the value is a {@link ConfigSection} then it must be represented as a ConfigSection. The ConfigValue
     * must conform to the specifications of the {@link ConfigValue}.
     *
     * @param path The path
     * @return A new config value.
     */
    @NotNull
    ConfigValue getValue(@NotNull String path);

    /**
     * Sets a new value mapped to the node. This will not save the value unless directly saved using {@link #save(Writer)}. Any time this
     * value is read using {@link #getValue(String)} it should return the new value.
     *
     * @param path The path, locating the value
     * @param value The new value to be set.
     */
    void set(@NotNull String path, Object value);

    /**
     * Removes a value from the specified path. This will not save unless directly saved using {@link #save(Writer)}. Any time this value
     * is read using {@link #getValue(String)} it should return null unless set again.
     *
     * @param path The location of the value to be removed.
     */
    void remove(@NotNull String path);

    /**
     * Creates a new config section at the located path. The new config section should have no values. This will set the
     * path given to a new blank config section. This will have different effects depending on what happens.
     *
     *   - This method override any existing config sections. For example if the path nest.a.b.c existed
     *   and you created at nest. Then nest.a the new path would be nest.a
     *   - If set to a non-nested node this will be the equivalent to setting the node to null. For example if you set
     *     'a'
     *
     * @param path The path of the new config section.
     * @return the new config section created in that area.
     * @throws IllegalArgumentException if the config section is attempting to overwrite. That is the path entered is empty.
     */
    @NotNull
    ConfigSection createSection(@NotNull String path);

    /**
     * Creates a new config section at the located path. The new config section will have the values stated in the map. This will set the
     * path given to a new blank config section. This will have different effects depending on what happens.
     *
     *   - This method override any existing config sections. For example if the path nest.a.b.c existed
     *   and you created at nest. Then nest.a the new path would be nest.a
     *   - If set to a non-nested node this will be the equivalent to setting the node to null. For example if you set
     *     'a'
     *
     * @param path The path of the new config section.
     * @return the new config section created in that area.
     * @throws IllegalArgumentException if the config section is attempting to overwrite. That is the path entered is empty.
     */
    @NotNull
    ConfigSection createSection(@NotNull String path, Map<String ,Object> map);

    /**
     * @return Whether this config type supports nested values.
     */
    boolean supportsNesting();

    /**
     * Saves the config to the specified file location. This will overwrite the given file. If {@link #supportsCommentPreservation()} is false or
     * {@link FileConfigOptions#parseComments() is false then any comments will be removed.
     *
     * @param file The file to save to
     * @throws IOException If an error occurs
     */
    void save(@NotNull File file) throws IOException;

    /**
     * Saves the config to the specified file location. This will overwrite the given file. If {@link #supportsCommentPreservation()} is false or
     * {@link FileConfigOptions#parseComments() is false then any comments will be removed.
     *
     * @param file The file to save to
     * @throws IOException If an error occurs
     */
    void save(@NotNull Writer writer) throws IOException;

    /**
     * Loads the config from the specified reader. This should take the values and transform it into an appropiate format for reading. Comments may
     * or may not be parsed depending on the settings.
     *
     * @param inputStream the inputstream to retrieve the config from
     * @throws IOException If an error occurs
     * @throws InvalidConfigurationException If a syntax error occurs while reading the config.
     */
    void load(@NotNull InputStream inputStream) throws IOException, InvalidConfigurationException;

    /**
     * @return Whether the config adapter supports comment preservation
     */
    boolean supportsCommentPreservation();

    void setComments(String path, List<String> comments);

    void setInlineComments(String path, List<String> comments);

    List<String> getInlineComments(String path);

    List<String> getComments(String path);
}
