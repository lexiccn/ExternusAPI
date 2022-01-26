package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.common.depend.DependencyManager;
import me.deltaorion.extapi.locale.translator.PluginTranslator;

//shared api functions
public interface BaseApiPlugin extends EPlugin, DependencyManager, ParserRegistry {

    public PluginTranslator getTranslator();


    /**
     * Plugin Disable Logic. This should be overiden by the plugin and any disable logic should be defined. This will be called on reloads
     * and on server shutdowns.
     */
    public void onPluginDisable();

    /**
     * Plugin Enable Logic. This should be overriden by the plugin extending this. This will be called on
     * reloads and on server startups
     */

    public void onPluginEnable();
}
