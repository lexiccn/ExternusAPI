package me.deltaorion.bukkit.plugin.server;
import com.google.common.base.MoreObjects;
import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.plugin.plugin.BukkitPluginWrapper;
import me.deltaorion.common.plugin.sender.SenderFactory;
import me.deltaorion.bukkit.plugin.sender.BukkitSenderInfo;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.sender.SimpleSender;
import me.deltaorion.common.plugin.plugin.EPlugin;
import me.deltaorion.bukkit.plugin.sender.BukkitSenderInfo_12;
import me.deltaorion.bukkit.plugin.sender.BukkitSenderInfo_8_11;
import me.deltaorion.common.plugin.server.EServer;
import me.deltaorion.common.plugin.version.MinecraftVersion;
import me.deltaorion.common.plugin.version.VersionFactory;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.Function;

public class BukkitServer implements EServer {

    @NotNull private final Server server;
    @NotNull private final MinecraftVersion minecraftVersion;
    @NotNull private volatile SenderFactory senderFactory;

    public final static int MILLIS_PER_TICK = 50;
    public final static TemporalUnit TICK_UNIT = new BukkitTick();

    public BukkitServer(@NotNull Server server) {
        this.server = server;
        this.minecraftVersion = Objects.requireNonNull(VersionFactory.parse(server.getBukkitVersion()),String.format("Cannot parse Minecraft Version '%s'",server.getBukkitVersion()));
        this.senderFactory = getBukkit;
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
            senders.add(wrapSender(player));
        }
        return Collections.unmodifiableList(senders);
    }

    @Override
    public Sender getConsoleSender() {
        return wrapSender(server.getConsoleSender());
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
        return MoreObjects.toStringHelper(this)
                .add("name",server.getServerName())
                .add("minecraft version",minecraftVersion)
                .add("server version",server.getVersion()).toString();
    }

    @NotNull
    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        return senderFactory.get(commandSender,minecraftVersion);
    }

    @Override
    public void setSenderFactory(@NotNull SenderFactory factory) {
        Objects.requireNonNull(factory);
        this.senderFactory = factory;
    }

    private final SenderFactory getBukkit = new SenderFactory() {
        @NotNull
        @Override
        public Sender get(@NotNull Object commandSender, @NotNull MinecraftVersion version) {
            if(!(commandSender instanceof CommandSender))
                throw new IllegalArgumentException("Must wrap a bukkit command sender");

            CommandSender sender = (CommandSender) commandSender;
            return new SimpleSender(getSenderInfo(sender));
        }
    };

    @NotNull
    private BukkitSenderInfo getSenderInfo(@NotNull CommandSender sender) {
        if(minecraftVersion.getMajor()>=8 && minecraftVersion.getMajor()<=11) {
            return new BukkitSenderInfo_8_11(this,server,sender);
        } else if(minecraftVersion.getMajor()>=12) {
            return new BukkitSenderInfo_12(this,server,sender);
        }
        throw new UnsupportedVersionException("Cannot find a suitable Bukkit Sender wrapper for this version!");
    }

}
