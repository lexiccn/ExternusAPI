package me.deltaorion.extapi.common.entity.sender;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.common.server.EServer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitSenderInfo implements SenderInfo {
    private final CommandSender sender;
    private final EPlugin plugin;
    private final Server server;

    public BukkitSenderInfo(EPlugin plugin, CommandSender sender) {
        this.sender = sender;
        this.plugin = plugin;

        if(!((plugin.getEServer().getServerObject()) instanceof Server))
            throw new IllegalArgumentException("Must use bukkit plugin for bukkit senders");

        this.server = (Server) plugin.getEServer().getServerObject();
    }


    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public UUID getUniqueId() {
        if(isConsole()) {
            return EServer.CONSOLE_UUID;
        } else {
            Player player = (Player) sender;
            return player.getUniqueId();
        }
    }

    @Override
    public void sendMessage(String message) {
        if(sender instanceof Player || sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
            sender.sendMessage(message);
        } else {
            plugin.getScheduler().scheduleSyncDelayedTask( () -> sender.sendMessage(message));
        }
    }

    @Override
    public EServer getEServer() {
        return plugin.getEServer();
    }

    @Override
    public void dispatchCommand(String commandLine) {
        server.dispatchCommand(sender,commandLine);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean isConsole() {
        return !(sender instanceof Player);
    }

    @Override
    public boolean isOP() {
        return sender.isOp();
    }

    public CommandSender getSender() {
        return sender;
    }
}
