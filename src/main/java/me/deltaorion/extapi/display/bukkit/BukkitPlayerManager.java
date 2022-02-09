package me.deltaorion.extapi.display.bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * The Bukkit Player manager should store a cache of {@link BukkitApiPlayer}. API Players can be creatred with
 * {@link #getPlayer(Player)}. This class should link a player to their respective bukkit api player. Note this does
 * not link UUID to the bukkit api player because the API Player only represents a player who is online. If the underlying
 * player goes offline, then the API Player instance is useless and should be removed.
 */
public interface BukkitPlayerManager extends Iterable<BukkitApiPlayer> {

    /**
     * If an API player exists then it will return it. Otherwise it will make a new one and return it.
     * API Players created here, when they leave the server will automatically be removed when they quit. This
     * removal will also clear any display items that are being shown to them (you cant display stuff to
     * someone who isn't online).
     *
     * @param player The player to wrap
     * @return The existing Bukkit APi player if it exists or a new one if it does not.
     */
    @NotNull
    BukkitApiPlayer getPlayer(@NotNull final Player player);

    @Nullable
    BukkitApiPlayer getPlayer(@NotNull UUID uuid);

    /**
     *
     * @param player The player to check with
     * @return whether the player manager holds an API Player
     */
    boolean hasPlayer(@NotNull Player player);

    /**
     * @return An immutable collection of all online api players in the cache.
     */
    Collection<BukkitApiPlayer> getPlayers();

    /**
     * Removes a cached player. If the player is not in the cache then this will do nothing. Otherwise they will be removed. Any
     * displayed items will no longer be shown to the player.
     *
     * @param player The player to remove
     */
    void removeCached(@NotNull Player player);

    /**
     * Changes the factory. The ApiPlayerFactory is used to generate new {@link BukkitApiPlayer} everytime {@link #getPlayer(Player)} makes
     * a player. Changing this will allow you to generate custom API Players.
     *
     * @param factory The new factory to set
     */
    void setFactory(@NotNull ApiPlayerFactory factory);
}
