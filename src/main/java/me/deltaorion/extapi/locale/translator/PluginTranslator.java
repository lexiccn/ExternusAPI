package me.deltaorion.extapi.locale.translator;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.EServer;

import java.util.Locale;

public class PluginTranslator {

    private final TranslationManager managerDef;
    private final TranslationManager managerCustom;
    private final EPlugin plugin;

    public PluginTranslator(EPlugin plugin, String defaultTranslation) {
        plugin.getPluginLogger().info("Initialising Plugin Translator");
        managerDef = new TranslationManager(plugin.getDataDirectory().resolve("translations"),
                defaultTranslation,getClass());
        managerCustom = new TranslationManager(plugin.getDataDirectory().resolve("translations"),
                defaultTranslation,plugin.getClass());
        managerDef.reload();
        managerCustom.reload();
        this.plugin = plugin;
    }

    public void reload() {
        plugin.getPluginLogger().info("Reloading Translations and Messages");
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
