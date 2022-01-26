package me.deltaorion.extapi.locale.translator;

import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.FileConfiguration;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationManager {

    @NotNull private final Path translationDirectory;
    @NotNull private final String defaultLocaleLocation;
    @Nullable private final Class<?> classLoader;
    private final Locale DEFAULT_LOCALE = EServer.DEFAULT_LOCALE;

    public TranslationManager(@NotNull Path translationDirectory, @NotNull String defaultLocaleLocation, Class<?> classLoader) {
        Validate.notNull(translationDirectory);
        Validate.notNull(defaultLocaleLocation);

        this.translationDirectory = translationDirectory;
        this.defaultLocaleLocation = defaultLocaleLocation;
        this.classLoader = classLoader;
    }

    public TranslationManager(@NotNull Path translationDirectory, @NotNull String defaultLocaleLocation) {
        this(translationDirectory,defaultLocaleLocation,null);
    }



    private void loadDefaultLocale() {
        Configuration configuration = new FileConfiguration(classLoader,translationDirectory.resolve(defaultLocaleLocation),defaultLocaleLocation);
        addTranslations(DEFAULT_LOCALE,configuration);
    }

    private void loadCustomTranslations() {
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
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"' as the locale name could not be read");
                continue;
            }

            try {
                Configuration library = getTranslation(path);
                addTranslations(locale,library);
            } catch (IllegalArgumentException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"'");
            } catch (IOException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"' due to IO Exception");
            }
        }
    }

    private void addTranslations(Locale locale, Configuration configuration) {
        configuration.getConfig().getRoot().options().copyDefaults(true);
        if(configuration.getConfig().getDefaults()!=null) {
            configuration.getConfig().setDefaults(configuration.getConfig().getDefaults());
        }
        configuration.getConfig().getKeys(true).forEach(key -> {
            if(!configuration.getConfig().isConfigurationSection(key)) {
                Translator.getInstance().addTranslation(locale, key, configuration.getConfig().getString(key));
            }
        });
    }

    private Configuration getTranslation(Path path) throws IOException {
        return new FileConfiguration(classLoader,path);
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
