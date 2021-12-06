package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.entity.sender.SimpleSender;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitServer implements EServer {

    private final Server server;
    private final MinecraftVersion minecraftVersion;
    public final static int MILLIS_PER_TICK = 50;

    public BukkitServer(Server server) {
        this.server = server;
        this.minecraftVersion = VersionFactory.parse(server.getBukkitVersion());
    }

    @Override
    public MinecraftVersion getVersion() {
        return this.minecraftVersion;
    }

    @Override
    public String getBrand() {
        return server.getName();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        for(Player player : server.getOnlinePlayers()) {
            players.add(player.getUniqueId());
        }
        return players;
    }

    @Override
    public int getMaxPlayer() {
        return server.getMaxPlayers();
    }

    @Override
    public boolean isPlayerOnline(UUID uuid) {
        return server.getPlayer(uuid).isOnline();
    }

    public Server getServer() {
        return this.server;
    }

}
