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
        return playerCache.computeIfAbsent(player,p -> factory.get(plugin,player));
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
        playerCache.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        BukkitApiPlayer player = this.playerCache.get(event.getPlayer());
        if(player==null)
            return;

        if(player.getBossBar()!=null)
            player.getBossBar().update();
    }

    @Override
    public void removeCached(@Nullable Player player) {
        if(player==null)
            return;

        BukkitApiPlayer p = playerCache.get(player);
        if(p!=null)
            p.cleanUp();

        playerCache.remove(player);
    }

    public void shutdown() {
        //reload hook
        for(BukkitApiPlayer player : getPlayers()) {
            player.cleanUp();
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
