package me.deltaorion.extapi.locale.translator;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.EServer;

import java.util.Locale;

public class PluginTranslator {

    private final TranslationManager manager;
    private final EPlugin plugin;

    public PluginTranslator(EPlugin plugin, String defaultTranslation) {
        System.out.println("Initialising Plugin Translator");
        manager = new TranslationManager(plugin.getDataDirectory().resolve("translations"),
                plugin.getResourceURL(defaultTranslation));
        this.plugin = plugin;
    }

    public void reload() {
        manager.reload();
    }

    public String getDefaultTranslation(String location) {
        return Translator.translate(location, EServer.DEFAULT_LOCALE);
    }

    public String getTranslation(String location, Locale locale) {
        return Translator.translate(location,locale);
    }
}
