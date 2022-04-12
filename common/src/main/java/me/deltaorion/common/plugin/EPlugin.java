package me.deltaorion.common.plugin;

import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;

@NotThreadSafe
public interface EPlugin {

    /**
     * Returns an abstract representation of a minecraft server that the plugin is hosted on.
     *
     * @return An abstract representation of the minecraft server.
     */
    @NotNull
    EServer getEServer();

    /**
     * Allows tasks to be scheduled using the servers cached thread pool. Anything that is scheduled
     * will be scheduled for this plugin.
     *
     * @return the plugin's scheduler.
     */
    @NotNull
    SchedulerAdapter getScheduler();

    /**
     * Gets the data folder of the plugin. This is usually /server/plugins/plugin-name
     *
     * @return The data folder of the plugin
     */

    @NotNull
    Path getDataDirectory();

    /**
     * Returns a resource stream from the plugins /resource folder. This allows a person using this API
     * to access any files inside the /resource such as config.yml or plugin.yml.
     *
     * @param path The name of the file inside the resources folder
     * @return A {@link InputStream} from the specified file or null if it cannot be found
     */

    @Nullable
    InputStream getResourceStream(@NotNull String path);

    /**
     *Saves the raw contents of any resource embedded with a plugin's .jar file assuming it can be found using getResource(String).
     * The resource is saved into the plugin's data folder using the same hierarchy as the .jar file (subdirectories are preserved).
     * @param resourcePath – the embedded resource path to look for within the plugin's .jar file. (No preceding slash).
     * @param replace – if true, the embedded resource will overwrite the contents of an existing file.
            Throws:
     * @throws IllegalArgumentException – if the resource path is null, empty, or points to a nonexistent resource.
     */

    void saveResource(@NotNull String resourcePath, boolean replace);

    /**
     * Retrieves the plugin logger. This allows messages to be logged to the server console with varying degrees of severity.
     * Logged messages include the plugin name and the date they were logged.
     * Messages can be logged on 3 degrees of severity, info, warn or severe.
     *
     * @return The plugin logger.
     */

    @NotNull
    PluginLogger getPluginLogger();

    /**
     * Checks whether this plugin, the one that is wrapped is enabled or not.
     *
     * @return whether this plugin is enabled
     */
    boolean isPluginEnabled();

    /**
     * Disables this plugin. That being the plugin that is wrapped. Note that this operation may
     * or may not be fully supported depending on the plugin platform
     */

    void disablePlugin();

}
