package me.deltaorion.extapi.common.sender;

import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

/**
 * Generic sender class for both bukkit and bungee plugins. Provides all methods to interact
 * with a sender.
 */

public interface Sender {

    String getName();

    UUID getUniqueId();

    void sendMessage(String message);

    void sendMessage(int message);

    void sendMessage(boolean message);

    void sendMessage(Object message);

    void sendMessage(double message);

    void sendMessage(float message);

    void sendMessage(Message message, Object... args);

    boolean hasPermission(String permission);

    void performCommand(@NotNull String commandLine);

    boolean isConsole();

    boolean isValid();

    boolean isOP();

    Locale getLocale();

}
