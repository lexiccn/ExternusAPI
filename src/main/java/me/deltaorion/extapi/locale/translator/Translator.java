package me.deltaorion.extapi.locale.translator;

import me.deltaorion.extapi.config.Configuration;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static me.deltaorion.extapi.common.server.EServer.DEFAULT_LOCALE;

public class Translator {

    private final static Map<Locale,Map<String,String>> translationLibrary;

    static  {
        translationLibrary = new HashMap<>();
    }


    public static String getDefaultTranslation(String location) {
        return translate(location,DEFAULT_LOCALE);
    }

    /**
     * Raw Translation. This does not factor in color codes.
     *
     * @param location
     * @param locale
     * @return
     */
    public static String translate(String location, Locale locale) {

        Validate.notNull(location);

        if(locale==null) {
            locale = DEFAULT_LOCALE;
        }

        if(!translationLibrary.containsKey(locale)) {
            locale = DEFAULT_LOCALE;
        }
        //try their locale
        Map<String,String> translation = translationLibrary.get(locale);
        if(translation!=null) {
            if (translation.containsKey(location)) {
                return translation.get(location);
            }
        }

        //try the default locale
        if(locale!=DEFAULT_LOCALE) {
            locale = DEFAULT_LOCALE;
            translation = translationLibrary.get(locale);
            //translation = translationLibrary.get(locale);
            if(translation!=null) {
                if (translation.containsKey(location)) {
                    return translation.get(location);
                }
            }
        }
        //no results, return the location
        return location;
    }

    public static void addTranslation(Locale locale, String location, String result) {
        Map<String,String> translationFile = translationLibrary.get(locale);
        if(translationFile==null) {
            translationFile = new HashMap<>();
            translationLibrary.put(locale,translationFile);
        }

        translationFile.put(location,result);
    }

    private static String convertObject(Object object) {
        if(object==null)
            return "null";

        return object.toString();
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
