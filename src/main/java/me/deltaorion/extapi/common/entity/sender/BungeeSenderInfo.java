package me.deltaorion.extapi.common.entity.sender;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.common.server.EServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeSenderInfo implements SenderInfo {

    private final CommandSender sender;
    private final EPlugin plugin;

    public BungeeSenderInfo(CommandSender sender, EPlugin plugin) {
        this.sender = sender;
        this.plugin = plugin;
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
        return plugin.getEServer();
    }

    @Override
    public void dispatchCommand(String commandLine) {

    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isConsole() {
        if(sender instanceof ProxiedPlayer) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isOP() {
        return false;
    }

    public CommandSender getSender() {
        return sender;
    }

}
