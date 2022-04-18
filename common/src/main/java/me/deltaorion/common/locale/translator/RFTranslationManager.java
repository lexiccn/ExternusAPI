package me.deltaorion.common.locale.translator;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RFTranslationManager extends TranslationManager {

    @NotNull private final Path translationDirectory;
    @NotNull private final AdapterFactory adapterFactory;

    public RFTranslationManager(@NotNull Path translationDirectory, @NotNull AdapterFactory factory) {
        Objects.requireNonNull(translationDirectory);
        this.translationDirectory = translationDirectory;
        this.adapterFactory = factory;
    }

    private void loadCustomTranslations() {
        List<Path> translationFiles;
        if(!translationDirectory.toFile().exists()) {
            translationDirectory.toFile().mkdirs();
        }

        try(Stream<Path> stream = Files.list(translationDirectory)) {
            translationFiles = stream.filter(path -> path.getFileName().toString().endsWith("." + adapterFactory.getFileExtension())).collect(Collectors.toList());
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
                FileConfig library = FileConfig.loadConfiguration(new PropertiesAdapter(),path.toFile());
                addTranslations(locale,library);
            } catch (IllegalArgumentException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"'");
            } catch (IOException e) {
                System.err.println("Unable to load locale '"+path.getFileName().toString()+"' due to IO Exception");
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    private Locale getLocale(Path path) {
        String fileName = path.getFileName().toString();
        String localeString = fileName.substring(0, fileName.length() - (1+adapterFactory.getFileExtension().length()));
        return Translator.parseLocale(localeString);
    }

    @Override
    public void reload() {
        loadCustomTranslations();
    }

}
