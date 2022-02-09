package me.deltaorion.extapi.display.bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface BukkitPlayerManager extends Iterable<BukkitApiPlayer> {

    BukkitApiPlayer getPlayer(@NotNull final Player player);

    boolean hasPlayer(@NotNull Player player);

    Collection<BukkitApiPlayer> getPlayers();

    void removeCached(@Nullable Player player);

    void setFactory(@NotNull ApiPlayerFactory factory);
}
