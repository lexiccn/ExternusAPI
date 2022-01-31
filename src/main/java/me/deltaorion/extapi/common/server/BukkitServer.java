package me.deltaorion.extapi.common.server;
import me.deltaorion.extapi.common.sender.BukkitSenderInfo;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.SimpleSender;
import me.deltaorion.extapi.common.plugin.BukkitPluginWrapper;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BukkitServer implements EServer {

    @NotNull private final Server server;
    @NotNull private final MinecraftVersion minecraftVersion;
    public final static int MILLIS_PER_TICK = 50;

    public BukkitServer(@NotNull Server server) {
        this.server = server;
        this.minecraftVersion = Objects.requireNonNull(VersionFactory.parse(server.getBukkitVersion()),String.format("Cannot parse Minecraft Version '%s'",server.getBukkitVersion()));
    }

    @Override
    public MinecraftVersion getServerVersion() {
        return this.minecraftVersion;
    }

    @Override
    public String getServerBrand() {
        return server.getName();
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        for(Player player : server.getOnlinePlayers()) {
            players.add(player.getUniqueId());
        }
        return Collections.unmodifiableList(players);
    }

    @Override
    public List<Sender> getOnlineSenders() {
        List<Sender> senders = new ArrayList<>();
        for(Player player : server.getOnlinePlayers()) {
            senders.add(new SimpleSender(new BukkitSenderInfo(this,server,player)));
        }
        return Collections.unmodifiableList(senders);
    }

    @Override
    public Sender getConsoleSender() {
        return new SimpleSender(new BukkitSenderInfo(this,server, server.getConsoleSender()));
    }

    @Override
    public int getMaxPlayer() {
        return server.getMaxPlayers();
    }

    @Override
    public boolean isPlayerOnline(@NotNull UUID uuid) {
        if(uuid.equals(EServer.CONSOLE_UUID))
            return false;

        return server.getPlayer(uuid).isOnline();
    }

    @Override
    public EPlugin getPlugin(@NotNull String name) {
        Validate.notNull(name);
        Plugin plugin = server.getPluginManager().getPlugin(name);
        if(plugin==null)
            return null;

        return new BukkitPluginWrapper(plugin);
    }

    @Nullable
    @Override
    public Object getPluginObject(@NotNull String name) {
        return server.getPluginManager().getPlugin(name);
    }

    @Override
    public boolean isPluginEnabled(@NotNull String name) {
        return server.getPluginManager().isPluginEnabled(name);
    }

    @Override
    public String translateColorCodes(@NotNull String raw) {
        return ChatColor.translateAlternateColorCodes('&',raw);
    }

    @Override
    public String getServerName() {
        return server.getServerName();
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("name",server.getServerName())
                .add("minecraft version",minecraftVersion)
                .add("server version",server.getVersion()).toString();
    }
}
