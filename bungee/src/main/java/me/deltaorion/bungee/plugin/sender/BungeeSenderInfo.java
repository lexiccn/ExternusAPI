package me.deltaorion.bungee.plugin.sender;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.plugin.sender.SenderInfo;
import me.deltaorion.common.plugin.server.EServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.UUID;

public class BungeeSenderInfo implements SenderInfo {

    private final CommandSender sender;
    private final EServer eServer;
    private final ProxyServer server;

    public BungeeSenderInfo(CommandSender sender, ProxyServer server, EServer eServer) {
        this.sender = sender;
        this.eServer = eServer;
        this.server = server;
    }

    @NotNull
    @Override
    public String getName() {
        return sender.getName();
    }

    @NotNull
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

    @NotNull
    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public void dispatchCommand(String commandLine) {
        server.getPluginManager().dispatchCommand(sender,commandLine);
    }

    @NotNull
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
        return isConsole();
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
        if(!(o instanceof BungeeSenderInfo))
            return false;

        BungeeSenderInfo senderInfo = (BungeeSenderInfo) o;
        return senderInfo.getUniqueId().equals(this.getUniqueId()) && senderInfo.isConsole() == this.isConsole();
    }

}
