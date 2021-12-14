package me.deltaorion.extapi.common.entity.sender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class BukkitSenderInfo implements SenderInfo {
    private final CommandSender sender;
    private final Server server;
    private final EServer eServer;

    public BukkitSenderInfo(EServer server, CommandSender sender) {
        this.sender = sender;
        this.eServer = server;

        if(!((server.getServerObject()) instanceof Server))
            throw new IllegalArgumentException("Must use bukkit plugin for bukkit senders");

        this.server = (Server) server.getServerObject();
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
        }
    }

    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public void dispatchCommand(String commandLine) {
        server.dispatchCommand(sender,commandLine);
    }

    @Override
    public Locale getLocale() {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            System.out.println(player.spigot().getLocale());
            return Translator.parseLocale(player.spigot().getLocale());
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
