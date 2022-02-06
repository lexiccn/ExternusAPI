package me.deltaorion.extapi.locale.message;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.common.server.EServer;

import java.util.Locale;

@Immutable
public interface MessageComponent {

    public String toString(Locale locale);

    default String getDefault() {
        return toString(EServer.DEFAULT_LOCALE);
    }
}
