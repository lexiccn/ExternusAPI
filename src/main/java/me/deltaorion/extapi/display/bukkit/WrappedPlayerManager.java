package me.deltaorion.extapi.display.bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;

public class WrappedPlayerManager implements BukkitPlayerManager {

    private final BukkitPlayerManager wrapped;

    public WrappedPlayerManager(BukkitPlayerManager wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BukkitApiPlayer getPlayer(@NotNull Player player) {
        return wrapped.getPlayer(player);
    }

    @Override
    public boolean hasPlayer(@NotNull Player player) {
        return wrapped.hasPlayer(player);
    }

    @Override
    public Collection<BukkitApiPlayer> getPlayers() {
        return wrapped.getPlayers();
    }

    @Override
    public void removeCached(@Nullable Player player) {
        wrapped.removeCached(player);
    }

    @Override
    public void setFactory(@NotNull ApiPlayerFactory factory) {
        wrapped.setFactory(factory);
    }

    @NotNull
    @Override
    public Iterator<BukkitApiPlayer> iterator() {
        return wrapped.iterator();
    }
}
