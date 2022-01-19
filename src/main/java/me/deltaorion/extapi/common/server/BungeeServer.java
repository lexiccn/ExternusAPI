package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.sender.BungeeSenderInfo;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.SimpleSender;
import me.deltaorion.extapi.common.plugin.BungeePluginWrapper;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BungeeServer implements EServer {

    @NotNull private final ProxyServer proxyServer;
    @NotNull private final MinecraftVersion minecraftVersion;

    public BungeeServer(@NotNull ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        this.minecraftVersion = Objects.requireNonNull(VersionFactory.parse(proxyServer.getVersion()),String.format("Cannot parse proxy version in format '%s'",proxyServer.getVersion()));
    }

    @Override
    public MinecraftVersion getServerVersion() {
        return minecraftVersion;
    }

    @Override
    public String getServerBrand() {
        return proxyServer.getName();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        for(ProxiedPlayer player : proxyServer.getPlayers()) {
            players.add(player.getUniqueId());
        }

        return Collections.unmodifiableList(players);
    }

    @Override
    public List<Sender> getOnlineSenders() {
        List<Sender> players = new ArrayList<>();
        for(ProxiedPlayer player : proxyServer.getPlayers()) {
            players.add(new SimpleSender(new BungeeSenderInfo(player,proxyServer,this)));
        }
        return Collections.unmodifiableList(players);
    }

    @Override
    public Sender getConsoleSender() {
        return new SimpleSender(new BungeeSenderInfo(proxyServer.getConsole(),proxyServer,this));
    }

    @Override
    public int getMaxPlayer() {
        return proxyServer.getConfig().getPlayerLimit();
    }


    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid) {
        return proxyServer.getPlayer(uuid) != null;
    }

    @Override
    public EPlugin getPlugin(@NotNull String name) {
        Validate.notNull(name);
        Plugin plugin = proxyServer.getPluginManager().getPlugin(name);
        if(plugin==null)
            return null;

        return BungeePluginWrapper.of(plugin);
    }

    @Nullable
    @Override
    public Object getPluginObject(@NotNull String name) {
        return proxyServer.getPluginManager().getPlugin(name);
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        Validate.notNull(name);
        return proxyServer.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public String translateColorCodes(@NotNull String raw) {
        Validate.notNull(raw);
        return ChatColor.translateAlternateColorCodes('&',raw);
    }
}
