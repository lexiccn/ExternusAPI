package me.deltaorion.extapi.common.entity.sender;

import me.deltaorion.extapi.common.server.EServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Locale;
import java.util.UUID;

public class BungeeSenderInfo implements SenderInfo {

    private final CommandSender sender;
    private final EServer eServer;
    private final ProxyServer server;

    public BungeeSenderInfo(CommandSender sender, EServer server) {
        this.sender = sender;
        this.eServer = server;
        if(!(server.getServerObject() instanceof ProxyServer)) {
            throw new IllegalArgumentException("Bungee Senders must use a bungee plugin");
        }
        this.server = (ProxyServer) server.getServerObject();
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        if(sender instanceof ProxiedPlayer) {
            return ((ProxiedPlayer) sender).getUniqueId();
        } else {
            return EServer.CONSOLE_UUID;
        }
    }


    @Override
    public void sendMessage(String message) {
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public void dispatchCommand(String commandLine) {
        server.getPluginManager().dispatchCommand(sender,commandLine);
    }

    @Override
    public Locale getLocale() {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            return player.getLocale();
        } else {
            return EServer.DEFAULT_LOCALE;
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isConsole() {
        return !(sender instanceof ProxiedPlayer);
    }

    @Override
    public boolean isOP() {
        return false;
    }

    public CommandSender getSender() {
        return sender;
    }

}
