package me.deltaorion.common.locale.translator;

import me.deltaorion.common.plugin.plugin.EPlugin;
import me.deltaorion.common.plugin.server.EServer;

import java.util.Locale;

public class PluginTranslator {

    private final TranslationManager managerDef;
    private final TranslationManager managerCustom;
    private final EPlugin plugin;

    public PluginTranslator(EPlugin plugin, String defaultTranslation) {
        managerDef = new TranslationManager(plugin.getDataDirectory().resolve("translations"),
                defaultTranslation, getClass().getClassLoader());
        managerCustom = new TranslationManager(plugin.getDataDirectory().resolve("translations"),
                defaultTranslation,plugin.getClass().getClassLoader());
        reload();
        this.plugin = plugin;
    }

    public void reload() {
        managerDef.reload();
        managerCustom.reload();
    }

    public String getDefaultTranslation(String location) {
        return Translator.getInstance().translate(location, EServer.DEFAULT_LOCALE);
    }

    public String getTranslation(String location, Locale locale) {
        return Translator.getInstance().translate(location,locale);
    }
}
