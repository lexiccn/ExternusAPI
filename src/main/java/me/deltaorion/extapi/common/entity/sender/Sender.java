package me.deltaorion.extapi.common.entity.sender;

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

    boolean hasPermission(String permission);

    void performCommand(String commandLine);

    boolean isConsole();

    boolean isValid();

    boolean isOP();

}
