package me.deltaorion.common.plugin.sender;

import me.deltaorion.common.plugin.EServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public interface SenderInfo {

    @NotNull
    String getName();

    @NotNull
    UUID getUniqueId();

    boolean isConsole();

    boolean isOP();

    boolean hasPermission(String permission);

    void sendMessage(@Nullable String message);

    @NotNull
    EServer getEServer();

    void dispatchCommand(@Nullable String commandLine);

    @NotNull
    Locale getLocale();
}
