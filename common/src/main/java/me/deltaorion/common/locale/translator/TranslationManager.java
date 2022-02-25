package me.deltaorion.common.locale.translator;

import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.file.FileConfigLoader;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.config.properties.PropertiesConfigAdapter;
import me.deltaorion.common.plugin.server.EServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationManager {

    @NotNull private final Path translationDirectory;
    @NotNull private final String defaultLocaleLocation;
    @Nullable private final ClassLoader classLoader;
    private final Locale DEFAULT_LOCALE = EServer.DEFAULT_LOCALE;

    @NotNull private final static String FILE_EXTENSION = ".properties";

    public TranslationManager(@NotNull Path translationDirectory, @NotNull String defaultLocaleLocation, @Nullable ClassLoader loader) {
        Objects.requireNonNull(translationDirectory);
        Objects.requireNonNull(defaultLocaleLocation);

        this.translationDirectory = translationDirectory;
        this.defaultLocaleLocation = defaultLocaleLocation;
        this.classLoader = loader;
    }

    public TranslationManager(@NotNull Path translationDirectory, @NotNull String defaultLocaleLocation) {
        this(translationDirectory,defaultLocaleLocation,null);
    }



    private void loadDefaultLocale() {
        ConfigLoader configuration = new FileConfigLoader(classLoader,translationDirectory.resolve(defaultLocaleLocation),defaultLocaleLocation, new PropertiesAdapter());
        configuration.getConfig().mergeDefaults();
        configuration.saveConfig();
        addTranslations(DEFAULT_LOCALE,configuration);
    }

    private void loadCustomTranslations() {
        List<Path> translationFiles;
        if(!translationDirectory.toFile().exists()) {
            translationDirectory.toFile().mkdirs();
        }

        try(Stream<Path> stream = Files.list(translationDirectory)) {
            translationFiles = stream.filter(path -> path.getFileName().toString().endsWith(FILE_EXTENSION)).collect(Collectors.toList());
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
                ConfigLoader library = getTranslation(path);
                addTranslations(locale,library);
            } catch (IllegalArgumentException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"'");
            } catch (IOException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"' due to IO Exception");
            }
        }
    }

    private void addTranslations(Locale locale, ConfigLoader configuration) {
        configuration.getConfig().options().copyDefaults(true);
        if(configuration.getConfig().getDefaults()!=null) {
            configuration.getConfig().setDefaults(configuration.getConfig().getDefaults());
        }

        for(String key : configuration.getConfig().getKeys(true)) {
            Translator.getInstance().addTranslation(locale, key, configuration.getConfig().getString(key));
        }
    }

    private ConfigLoader getTranslation(Path path) throws IOException {
        return new FileConfigLoader(classLoader,path,new PropertiesAdapter());
    }

    private Locale getLocale(Path path) {
        String fileName = path.getFileName().toString();
        String localeString = fileName.substring(0, fileName.length() - FILE_EXTENSION.length());
        return Translator.parseLocale(localeString);
    }

    public void reload() {
        loadDefaultLocale();
        loadCustomTranslations();
    }

}
