package me.deltaorion.extapi.common.sender;

import me.deltaorion.extapi.locale.message.Message;
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
        System.out.println(message);
    }

    @Override
    public void sendMessage(int message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(boolean message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(Object message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(double message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(float message) {
        System.out.println(message);
    }

    @Override
    public void sendMessage(Message message, Object... args) {
        System.out.println(message.toString(locale,args));
    }

    @Override
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
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
}
