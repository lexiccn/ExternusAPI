package me.deltaorion.extapi.locale.message;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.locale.translator.Translator;

import java.util.Locale;

@Immutable
public class TranslatableComponent implements MessageComponent {

    private final String location;

    public TranslatableComponent(String location) {
        this.location = location;
    }

    @Override
    public String toString(Locale locale) {
        return Translator.getInstance().translate(location,locale);
    }
}
