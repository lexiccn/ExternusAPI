package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.entity.sender.BungeeSenderInfo;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.entity.sender.SimpleSender;
import me.deltaorion.extapi.common.plugin.BungeePluginWrapper;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeServer implements EServer {

    private final ProxyServer proxyServer;
    private final MinecraftVersion minecraftVersion;

    public BungeeServer(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
        if(this.proxyServer!=null) {
            this.minecraftVersion = VersionFactory.parse(proxyServer.getVersion());
        } else {
            this.minecraftVersion = null;
        }
    }

    @Override
    public MinecraftVersion getVersion() {
        return minecraftVersion;
    }

    @Override
    public String getBrand() {
        return proxyServer.getName();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        for(ProxiedPlayer player : proxyServer.getPlayers()) {
            players.add(player.getUniqueId());
        }

        return players;
    }

    @Override
    public List<Sender> getOnlineSenders() {
        List<Sender> players = new ArrayList<>();
        for(ProxiedPlayer player : proxyServer.getPlayers()) {
            players.add(new SimpleSender(new BungeeSenderInfo(player,this)));
        }
        return players;
    }

    @Override
    public Sender getConsoleSender() {
        return new SimpleSender(new BungeeSenderInfo(proxyServer.getConsole(),this));
    }

    @Override
    public int getMaxPlayer() {
        return proxyServer.getConfig().getPlayerLimit();
    }


    @Override
    public boolean isPlayerOnline(UUID uuid) {
        return proxyServer.getPlayer(uuid) != null;
    }

    @Override
    public EPlugin getPlugin(String name) {
        return new BungeePluginWrapper(proxyServer.getPluginManager().getPlugin(name));
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return proxyServer.getPluginManager().getPlugin(name) != null;
    }

    @Override
    public Object getServerObject() {
        return proxyServer;
    }

    @Override
    public String translateColorCodes(String raw) {
        return ChatColor.translateAlternateColorCodes('&',raw);
    }
}
