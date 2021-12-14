package me.deltaorion.extapi.locale.translator;

import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.StorageConfiguration;
import me.deltaorion.extapi.config.URLConfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationManager {

    private final Path translationDirectory;
    private final URL defaultLocaleURL;
    private final Locale DEFAULT_LOCALE = EServer.DEFAULT_LOCALE;

    public TranslationManager(Path translationDirectory, URL defaultLocale) {
        this.translationDirectory = translationDirectory;
        this.defaultLocaleURL = defaultLocale;

        reload();
    }

    private void loadDefaultLocale() {
        System.out.println("Loading Default File");
        if(defaultLocaleURL != null) {
            //assumed to be english
            System.out.println("Loading the Default Locale File");
            Configuration configuration = new URLConfiguration(defaultLocaleURL);
            addTranslations(DEFAULT_LOCALE,configuration);
        }
    }

    public void loadCustomTranslations() {
        System.out.println("Loading Custom");
        List<Path> translationFiles;
        if(!translationDirectory.toFile().exists()) {
            translationDirectory.toFile().mkdirs();
        }

        try(Stream<Path> stream = Files.list(translationDirectory)) {
            translationFiles = stream.filter(path -> path.getFileName().toString().endsWith(".yml")).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(Path path : translationFiles) {
            Locale locale = getLocale(path);
            if(locale == null) {
                System.out.println("Unable to load locale '"+path.getFileName().toString()+"'");
                continue;
            }

            try {
                Configuration library = getTranslation(path);
                addTranslations(locale,library);

            } catch (IllegalArgumentException | IOException e) {
                System.out.println("Unable to load locale '"+path.getFileName().toString()+"'");
            }
        }

    }

    private void addTranslations(Locale locale, Configuration configuration) {
        configuration.getConfig().getKeys(true).forEach(key -> {
            Translator.addTranslation(locale,key,configuration.getConfig().getString(key));
        });
    }

    private Configuration getTranslation(Path path) throws IOException {
        return new StorageConfiguration(path);
    }

    private Locale getLocale(Path path) {
        String fileName = path.getFileName().toString();
        String localeString = fileName.substring(0, fileName.length() - ".yml".length());
        return Translator.parseLocale(localeString);
    }

    public void reload() {
        loadDefaultLocale();
        loadCustomTranslations();
    }

}
