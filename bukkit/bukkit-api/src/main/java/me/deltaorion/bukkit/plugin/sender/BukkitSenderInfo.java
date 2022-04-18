package me.deltaorion.bukkit.plugin.sender;
import com.google.common.base.MoreObjects;
import me.deltaorion.common.plugin.sender.SenderInfo;
import me.deltaorion.common.plugin.EServer;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

public abstract class BukkitSenderInfo implements SenderInfo {
    protected final CommandSender sender;
    protected final Server server;
    protected final EServer eServer;

    public BukkitSenderInfo(@NotNull EServer eServer, @NotNull Server server, @NotNull CommandSender sender) {

        Validate.notNull(server);
        Validate.notNull(sender);
        Validate.notNull(eServer);

        this.sender = sender;
        this.server = server;
        this.eServer = eServer;
    }


    @NotNull
    @Override
    public String getName() {
        return sender.getName();
    }

    @NotNull
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

    @NotNull
    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public void dispatchCommand(String commandLine) {
        server.dispatchCommand(sender,commandLine);
    }

    @NotNull
    @Override
    public abstract Locale getLocale();

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uuid",getUniqueId())
                .add("name",getName())
                .add("console",isConsole())
                .add("op",isOP())
                .add("locale",getLocale()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BukkitSenderInfo))
            return false;

        BukkitSenderInfo senderInfo = (BukkitSenderInfo) o;
        return senderInfo.getUniqueId().equals(this.getUniqueId()) && senderInfo.isConsole() == this.isConsole();
    }
}
