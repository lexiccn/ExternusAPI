package me.deltaorion.common.locale.translator;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.file.ConfigLoader;

import java.util.Locale;

public abstract class TranslationManager {

    public abstract void reload();

    protected void addTranslations(Locale locale, FileConfig configuration) {
        configuration.options().copyDefaults(true);
        if(configuration.getDefaults()!=null) {
            configuration.setDefaults(configuration.getDefaults());
        }

        for(String key : configuration.getKeys(true)) {
            Translator.getInstance().addTranslation(locale, key, configuration.getString(key));
        }
    }
}
