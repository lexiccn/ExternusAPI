package me.deltaorion.common.locale.translator;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static me.deltaorion.common.plugin.server.EServer.DEFAULT_LOCALE;

@ThreadSafe
public class Translator {

    private final ConcurrentMap<Locale,ConcurrentMap<String,String>> translationLibrary;
    @GuardedBy("this") private static volatile Translator instance;

    private Translator() {
        this.translationLibrary = new ConcurrentHashMap<>();
    }


    public static Translator getInstance() {
        Translator singleton = instance;
        if(singleton==null) {
            synchronized (Translator.class) {
                singleton = instance;
                if(singleton==null)
                    instance = new Translator();
                    singleton = instance;
            }
        }
        return singleton;
    }


    public String getDefaultTranslation(String location) {
        return translate(location,DEFAULT_LOCALE);
    }

    /**
     * Raw Translation. This does not factor in color codes.
     *
     * @param location
     * @param locale
     * @return
     */
    @NotNull
    public String translate(@NotNull String location, @Nullable Locale locale) {

        Objects.requireNonNull(location);

        if(locale==null) {
            locale = DEFAULT_LOCALE;
        }

        if(!translationLibrary.containsKey(locale)) {
            Locale similar = new Locale(locale.getLanguage(),"","");
            if(!translationLibrary.containsKey(similar)) {
                locale = DEFAULT_LOCALE;
            } else {
                locale = similar;
            }
        }

        //try their locale
        ConcurrentMap<String,String> translation = translationLibrary.get(locale);
        if(translation!=null) {
            String result = translation.get(location);
            if(result!=null)
                return result;
        }

        //try the default locale
        if(locale!=DEFAULT_LOCALE) {
            locale = DEFAULT_LOCALE;
            translation = translationLibrary.get(locale);
            //translation = translationLibrary.get(locale);
            if(translation!=null) {
                String result = translation.get(location);
                if(result!=null)
                    return result;
            }
        }
        //no results, return the location
        return location;
    }


    public void addTranslation(Locale locale, String location, String result) {
        ConcurrentMap<String, String> translationFile = translationLibrary.computeIfAbsent(locale, k -> new ConcurrentHashMap<>());
        translationFile.put(location,result);
    }

    @Nullable
    public static Locale parseLocale(final @NotNull String string) {
        final String[] segments = string.split("_", 3); // language_country_variant
        final int length = segments.length;
        if (length == 1) {
            return new Locale(string); // language
        } else if (length == 2) {
            return new Locale(segments[0], segments[1]); // language + country
        } else if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]); // language + country + variant
        }
        return null;
    }

    public static String localeDisplayName(Locale locale) {
        if (locale.getLanguage().equals("zh")) {
            if (locale.getCountry().equals("CN")) {
                return "简体中文"; // Chinese (Simplified)
            } else if (locale.getCountry().equals("TW")) {
                return "繁體中文"; // Chinese (Traditional)
            }
            return locale.getDisplayCountry(locale) + locale.getDisplayLanguage(locale);
        }

        if (locale.getLanguage().equals("en") && locale.getCountry().equals("PT")) {
            return "Pirate";
        }

        return locale.getDisplayLanguage(locale);
    }
}
