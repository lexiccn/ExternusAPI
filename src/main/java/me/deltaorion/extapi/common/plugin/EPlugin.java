package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.depend.DependencyManager;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public interface EPlugin extends DependencyManager {

    EServer getEServer();

    SchedulerAdapter getScheduler();

    public Sender wrapSender(Object commandSender);

    /**
     * Gets the data folder of the plugin. This is usually /server/plugins/plugin-name
     *
     * @return The data folder of the plugin
     */

    Path getDataDirectory();

    /**
     * Returns a resource stream from the plugins /resource folder. This allows a person using this API
     * to access any files inside the /resource such as config.yml or plugin.yml.
     *
     * @param path The name of the file inside the resources folder
     * @return A inputsteam from the specified file.
     */

    InputStream getResourceStream(String path);

    URL getResourceURL(String path);

    void saveResource(String resourcePath, boolean replace);

    /**
     * Retrieves the plugin logger. This allows messages to be logged to the server console with varying degrees of severity.
     * Logged messages include the plugin name and the date they were logged.
     * Messages can be logged on 3 degrees of severity, info, warn or severe.
     *
     * @return The plugin logger.
     */

    PluginLogger getPluginLogger();

    /**
     * Checks whether this plugin, the one that is wrapped is enabled or not.
     *
     * @return
     */
    public boolean isPluginEnabled();

    /**
     * Returns an object containing the actual plugin. To gain access to most functionality one should cast it to the
     * appropiate plugin class. For example if the plugin stored was a java plugin to get the plugin object one should
     * cast it to a {@link org.bukkit.plugin.java.JavaPlugin}
     *
     * @return The plugin object wrapped in this class.
     */

    public Object getPluginObject();

    /**
     * Disables this plugin. That being the plugin that is wrapped.
     *
     */

    void disablePlugin();

    PluginTranslator getTranslator();

}
