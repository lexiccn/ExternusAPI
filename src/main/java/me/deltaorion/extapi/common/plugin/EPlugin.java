package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.EServer;

import java.io.InputStream;
import java.nio.file.Path;

public interface EPlugin {

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

    void saveResource(String resourcePath, boolean replace);

    PluginLogger getPluginLogger();
}
