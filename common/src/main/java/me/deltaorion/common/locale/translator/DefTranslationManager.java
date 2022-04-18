package me.deltaorion.common.locale.translator;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.ConfigAdapter;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.file.FileConfigLoader;
import me.deltaorion.common.config.properties.PropertiesAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;

public class DefTranslationManager extends TranslationManager {

    private final String defaultLocation;
    private final ClassLoader loader;
    private final Locale defLocale;
    private final AdapterFactory adapter;

    public DefTranslationManager(ClassLoader loader, String defLocation, Locale defLocale, AdapterFactory adapter) {
        this.defaultLocation = defLocation;
        this.loader = loader;
        this.defLocale = defLocale;
        this.adapter = adapter;
    }

    @Override
    public void reload() {
        loadDefaultLocale();
    }

    private void loadDefaultLocale() {
        InputStream stream = loader.getResourceAsStream(defaultLocation);
        if(stream==null)
            throw new IllegalArgumentException("Default Locale Location not found '"+ defaultLocation +"'");

        try {
            FileConfig configuration = FileConfig.loadConfiguration(adapter,stream);
            addTranslations(defLocale,configuration);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDefaultConfig(Path translationDirectory) {
        ConfigLoader def = new FileConfigLoader(adapter,loader,translationDirectory,defLocale.toString()+"."+adapter.getFileExtension());
        def.saveDefaultConfig();
    }
}
