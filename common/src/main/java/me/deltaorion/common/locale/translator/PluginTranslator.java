package me.deltaorion.common.locale.translator;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.plugin.EPlugin;

import java.nio.file.Path;
import java.util.Locale;

public class PluginTranslator extends TranslationManager {

    private final RFTranslationManager managerCustom;
    private final DefTranslationManager managerDef;
    private final DefTranslationManager customDef;
    private final Path translationDirectory;

    public PluginTranslator(EPlugin plugin, String defaultTranslation, String translationDirectory, AdapterFactory adapter, Locale defaultLocale) {
        this.translationDirectory = plugin.getDataDirectory().resolve(translationDirectory);

        managerDef = new DefTranslationManager(getClass().getClassLoader(),"me.deltaorion.extapi/en.properties", defaultLocale, new PropertiesAdapter());
        customDef = new DefTranslationManager(plugin.getClass().getClassLoader(),defaultTranslation,defaultLocale,adapter);
        managerCustom = new RFTranslationManager(this.translationDirectory,adapter);
    }

    @Override
    public void reload() {
        managerDef.reload();
        try {
            customDef.reload();
            customDef.saveDefaultConfig(translationDirectory);
        } catch (IllegalArgumentException ignored) {

        }
        managerCustom.reload();
    }

    public Path getTranslationDirectory() {
        return translationDirectory;
    }
}
