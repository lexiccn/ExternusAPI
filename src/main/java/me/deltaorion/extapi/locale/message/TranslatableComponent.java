package me.deltaorion.extapi.locale.message;

import me.deltaorion.extapi.locale.translator.Translator;
import net.jcip.annotations.Immutable;

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

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TranslatableComponent))
            return false;

        TranslatableComponent component = (TranslatableComponent) o;
        return component.location.equals(this.location);
    }
}
