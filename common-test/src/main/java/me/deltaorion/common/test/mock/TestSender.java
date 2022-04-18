package me.deltaorion.common.test.mock;

import me.deltaorion.common.locale.message.Message;
import me.deltaorion.common.plugin.sender.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TestSender implements Sender {

    private final String name;
    private final UUID uuid;
    private final boolean isConsole;
    private final boolean isOp;
    private final Locale locale;
    private final Set<String> permissions;

    public TestSender(String name, UUID uuid, boolean isConsole, boolean isOp, Locale locale, String... perms) {
        this.name = name;
        this.uuid = uuid;
        this.isConsole = isConsole;
        this.isOp = isOp;
        this.locale = locale;
        this.permissions = new HashSet<>(Arrays.asList(perms));
        this.permissions.add("ExtApi.Test.Command");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println("server -> "+getName()+": "+message);
    }

    @Override
    public void sendMessage(int message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(boolean message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(Object message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(double message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(float message) {
        sendMessage(String.valueOf(message));
    }

    @Override
    public void sendMessage(Message message, Object... args) {
        sendMessage(message.toString(locale,args));
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission) || isOp || isConsole;
    }

    @Override
    public void performCommand(@NotNull String commandLine) {
        System.out.println("Performing Command /"+commandLine);
    }

    @Override
    public boolean isConsole() {
        return isConsole;
    }

    @Override
    public boolean isValid() {
        return isConsole;
    }

    @Override
    public boolean isOP() {
        return isOp;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TestSender))
            return false;

        TestSender sender = (TestSender) o;
        return sender.uuid.equals(this.uuid) && sender.isConsole == this.isConsole;
    }
}
