package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
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
        this.minecraftVersion = VersionFactory.parse(proxyServer.getVersion());
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
    public int getMaxPlayer() {
        return proxyServer.getConfig().getPlayerLimit();
    }


    @Override
    public boolean isPlayerOnline(UUID uuid) {
        return proxyServer.getPlayer(uuid) != null;
    }
}
