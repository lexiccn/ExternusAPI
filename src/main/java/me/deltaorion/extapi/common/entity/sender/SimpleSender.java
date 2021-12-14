package me.deltaorion.extapi.common.entity.sender;

import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.message.Message;

import java.util.Locale;
import java.util.UUID;

public class SimpleSender implements Sender {

    private final SenderInfo senderInfo;

    public SimpleSender(SenderInfo senderInfo) {
        this.senderInfo = senderInfo;
    }

    @Override
    public String getName() {
        if(senderInfo.isConsole()) {
            return EServer.CONSOLE_NAME;
        } else {
            return senderInfo.getName();
        }
    }

    @Override
    public UUID getUniqueId() {
        return senderInfo.getUniqueId();
    }

    @Override
    public void sendMessage(String message) {
        senderInfo.sendMessage(message);
    }

    @Override
    public void sendMessage(int message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(boolean message) {
        senderInfo.sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(Object message) {
        senderInfo.sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(double message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(float message) {
        senderInfo.sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(Message message, Object... args) {
        senderInfo.sendMessage(message.toString(getLocale(),args));
    }

    @Override
    public boolean hasPermission(String permission) {
        return senderInfo.hasPermission(permission) || senderInfo.isConsole();
    }

    @Override
    public void performCommand(String commandLine) {
        senderInfo.dispatchCommand(commandLine);
    }

    @Override
    public boolean isConsole() {
        return senderInfo.isConsole();
    }

    @Override
    public boolean isValid() {
        return senderInfo.getEServer().isPlayerOnline(getUniqueId()) || senderInfo.isConsole();
    }

    @Override
    public boolean isOP() {
        return senderInfo.isOP();
    }

    @Override
    public Locale getLocale() {
        return senderInfo.getLocale();
    }
}
