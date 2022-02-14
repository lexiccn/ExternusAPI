package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SimpleBukkitPlayerManager implements Listener, BukkitPlayerManager {

    @NotNull private final ConcurrentMap<Player,BukkitApiPlayer> playerCache;
    @NotNull private final BukkitPlugin plugin;
    @NotNull private ApiPlayerFactory factory;

    public SimpleBukkitPlayerManager(@NotNull BukkitPlugin plugin) {
        this.plugin = plugin;
        playerCache = new ConcurrentHashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        this.factory = ApiPlayerFactory.DEFAULT;
    }

    @NotNull @Override
    public BukkitApiPlayer getPlayer(@NotNull final Player player) {
        return new WrappedApiPlayer(playerCache.computeIfAbsent(player,p -> factory.get(plugin,player)));
    }

    @Nullable @Override
    public BukkitApiPlayer getPlayer(@NotNull UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if(player==null)
            return null;

        return getPlayer(player);
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return playerCache.containsKey(player);
    }

    @NotNull @Override
    public Collection<BukkitApiPlayer> getPlayers() {
        return Collections.unmodifiableCollection(playerCache.values());
    }

    //safest to remove on quit rather than hold a weak reference, otherwise if anywhere, stuffs up and holds
    //a reference then this will start leaking
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        removeCached(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        if(plugin.getEServer().getServerVersion().getMajor()>8)
            return;
        BukkitApiPlayer player = this.playerCache.get(event.getPlayer());
        if(player==null)
            return;

        if(player.getBossBar()!=null)
            player.getBossBar().update();
    }

    @Override
    public void removeCached(@NotNull Player player) {
        BukkitApiPlayer p = playerCache.remove(player);
        //when removing ensure to clear all display items to make sure this is garbage collected.
        if(p!=null)
            p.disconnect();
    }

    public void shutdown() {
        //reload hook
        for(BukkitApiPlayer player : getPlayers()) {
            player.disconnect();
        }
    }

    @NotNull @Override
    public Iterator<BukkitApiPlayer> iterator() {
        return getPlayers().iterator();
    }

    @Override
    public void setFactory(@NotNull ApiPlayerFactory factory) {
        this.factory = factory;
    }

}
