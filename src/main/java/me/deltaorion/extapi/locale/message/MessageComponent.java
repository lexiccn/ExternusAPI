package me.deltaorion.extapi.locale.message;

import me.deltaorion.extapi.common.server.EServer;

import java.util.Locale;

public interface MessageComponent {

    public String toString(Locale locale);


    default String getDefault() {
        return toString(EServer.DEFAULT_LOCALE);
    }
}
