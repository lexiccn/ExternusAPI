package me.deltaorion.extapi.common.sender;
import com.google.common.base.Objects;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.Translator;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

public class BukkitSenderInfo implements SenderInfo {
    private final CommandSender sender;
    private final Server server;
    private final EServer eServer;

    public BukkitSenderInfo(@NotNull EServer eServer, @NotNull Server server, @NotNull CommandSender sender) {

        Validate.notNull(server);
        Validate.notNull(sender);
        Validate.notNull(eServer);

        this.sender = sender;
        this.server = server;
        this.eServer = eServer;
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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("uuid",getUniqueId())
                .add("name",getName())
                .add("console",isConsole())
                .add("op",isOP())
                .add("locale",getLocale()).toString();
    }
}
