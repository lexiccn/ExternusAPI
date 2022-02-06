package me.deltaorion.extapi.common.sender;

import com.google.common.base.Objects;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.message.Message;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

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
    public void performCommand(@NotNull String commandLine) {
        Validate.notNull(commandLine);
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

    public String toString() {
        return Objects.toStringHelper(this)
                .add("Wrapper",senderInfo).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof SimpleSender))
            return false;

        SimpleSender sender = (SimpleSender) o;
        return sender.senderInfo.equals(senderInfo);
    }
}
