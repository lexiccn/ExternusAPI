package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.actionbar.ActionBar;
import me.deltaorion.extapi.display.actionbar.ActionBarManager;
import me.deltaorion.extapi.display.actionbar.RejectionPolicy;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.scoreboard.EScoreboard;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public interface BukkitApiPlayer {

    /**
     * Creates a scoreboard for this player and sets it. The scoreboard is returned and then can be modified
     * accordingly. The scoreboard will have the maximum amount of lines
     *
     * @param name The name of the scoreboard. This is used to identify the scoreboard
     * @return A new scoreboard
     */
    @Nullable
    EScoreboard setScoreboard(@NotNull String name);

    /**
     * Creates a scoreboard for this player and sets it. The scoreboard is returned and then can be modified
     * accordingly.
     *
     * @param name The name of the scoreboard. This is used to identify the scoreboard
     * @param lines The immutable amount of lines for the scoreboard
     * @return A new scoreboard
     */
    @Nullable
    EScoreboard setScoreboard(@NotNull String name, int lines);

    /**
     * Removes the current scoreboard that the player has. The player will no longer have that scoreboard
     * and it will no longer be visible to the player.
     */
    void removeScoreboard();

    /**
     * @return the scoreboard currently being run. If null then the player is viewing no scoreboard.
     */
    @Nullable
    EScoreboard getScoreboard();

    /**
     * @return The player that this is wrapped around.
     */
    @NotNull
    Player getPlayer();

    /**
     * @return The player's unique id. Even if the original reference is lost this will not return null.
     */
    @NotNull
    UUID getUniqueID();

    /**
     * @return  The name of the player.
     */
    @NotNull
    String getName();

    /**
     * @return The localisation of the player. If the original reference is lost or the language cannot be parsed
     * then this will return the default locale. Otherwise it will return their locale.
     */
    @NotNull
    Locale getLocale();

    /**
     * @return The action bar manager. This will allow you to manage the currently rendered action bar. You can send an action bar
     * with {@link ActionBarManager#send(ActionBar, RejectionPolicy, Object...)}
     */
    @NotNull
    ActionBarManager getActionBarManager();

    /**
     * @return The currently rendered boss bar. If the player has no viewed boss bar then this will return null.
     */
    @Nullable
    BossBar getBossBar();

    /**
     * Creates a new {@link BossBar} and sets the players current BossBar to this BossBar. It will then return the players new BossBar.
     * If the player has disconnected this will return Null. Although a player can 'technically' have more than one BossBar the API limits
     * this to effectively 1.
     *
     * @param name The name of the BossBar. This is not the message to be displayed but a unique identifying name
     * @return The new BossBar, or null if the player has been disconnected
     */
    @Nullable
    BossBar setBossBar(@NotNull String name);

    /**
     * Creates a new {@link BossBar} and sets the players current BossBar to this BossBar. It will then return the players new BossBar.
     * If the player has disconnected this will return Null. Although a player can 'technically' have more than one BossBar the API limits
     * to effectively 1.
     *
     * @param name The name of the BossBar. This is not the message to be displayed but a unique identifying name
     * @param message The message to be displayed on the bossbar
     * @return The new BossBar, or null if the player has been disconnected
     */
    @Nullable
    BossBar setBossBar(@NotNull String name, @NotNull Message message);

    /**
     * Removes the players current BossBar.
     */
    void removeBossBar();

    /**
     * 'Disconnects' the player. This should be called once the player is no longer online. Once called
     *    - all display items will be removed
     *    - no new display items can be added.
     *    - Any display item can still be removed however.
     */
    void disconnect();
}
