package me.deltaorion.extapi.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BukkitPlayerManager implements Listener, Iterable<BukkitApiPlayer> {

    @NotNull private final ConcurrentMap<Player,BukkitApiPlayer> playerCache;
    @NotNull private final BukkitPlugin plugin;

    public BukkitPlayerManager(@NotNull BukkitPlugin plugin) {
        this.plugin = plugin;
        this.playerCache = new ConcurrentHashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @NotNull
    public BukkitApiPlayer getPlayer(@NotNull Player player) {
        return playerCache.computeIfAbsent(player,p -> new BukkitApiPlayer(plugin,player));
    }

    public boolean hasPlayer(@NotNull Player player) {
        return playerCache.containsKey(player);
    }

    @NotNull
    public Collection<BukkitApiPlayer> getPlayers() {
        return Collections.unmodifiableCollection(playerCache.values());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        playerCache.remove(event.getPlayer());
    }

    @NotNull @Override
    public Iterator<BukkitApiPlayer> iterator() {
        return getPlayers().iterator();
    }
}
