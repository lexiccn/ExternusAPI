package me.deltaorion.common.locale.message;

import me.deltaorion.common.plugin.server.EServer;
import net.jcip.annotations.Immutable;

import java.util.Locale;

@Immutable
public interface MessageComponent {

    public String toString(Locale locale);

    default String getDefault() {
        return toString(EServer.DEFAULT_LOCALE);
    }
}
