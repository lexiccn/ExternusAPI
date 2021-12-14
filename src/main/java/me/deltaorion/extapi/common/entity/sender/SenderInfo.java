package me.deltaorion.extapi.common.entity.sender;

import me.deltaorion.extapi.common.server.EServer;

import java.util.Locale;
import java.util.UUID;

public interface SenderInfo {

    String getName();

    UUID getUniqueId();

    boolean isConsole();

    boolean isOP();

    boolean hasPermission(String permission);

    void sendMessage(String message);

    EServer getEServer();

    void dispatchCommand(String commandLine);

    Locale getLocale();
}
