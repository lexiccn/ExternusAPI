package me.deltaorion.bukkit.display.bukkit;

import com.google.common.base.MoreObjects;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.bukkit.display.actionbar.ActionBar;
import me.deltaorion.bukkit.display.actionbar.ActionBarManager;
import me.deltaorion.bukkit.display.actionbar.RejectionPolicy;
import me.deltaorion.bukkit.display.bossbar.BossBar;
import me.deltaorion.bukkit.display.bossbar.BossBarRendererFactory;
import me.deltaorion.bukkit.display.bossbar.SimpleBossBar;
import me.deltaorion.bukkit.display.scoreboard.EScoreboard;
import me.deltaorion.bukkit.display.scoreboard.ScoreboardFactory;
import me.deltaorion.common.locale.message.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * A Bukkit API Player is a player wrapper that provides additional functionality to a player for the Display API. One can
 *   - add a scoreboard with {@link #setScoreboard(String, int)}
 *   - set the bossbar {@link #setBossBar(String, Message)}
 *   - set the actionbar {@link #getActionBarManager()}
 *
 * The API player only holds a strong reference to the player. It is advised not to keep ones own reference to a Bukkit API player
 * as it can potentially create memory leaks. Instead one should grab it from the BukkitPlayerManager
 *
 * References to BukkitApiPlayers are kept in {@link BukkitPlayerManager}. One should not create their own BukkitApiPlayers.
 *
 * The API player is merely an extension of a {@link Player}. Hence it only represents a player who is online.
 */
public class EApiPlayer implements BukkitApiPlayer {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Player player;
    @Nullable private EScoreboard scoreboard;
    @NotNull private final ActionBarManager actionBarManager;
    @Nullable private BossBar bossBar;
    @NotNull private final Sender sender;

    @NotNull private final ScoreboardFactory scoreboardFactory;
    @NotNull private final BossBarRendererFactory bossBarFactory;

    @NotNull private final UUID uuid;

    @NotNull private final Object scoreboardLock = new Object();
    @NotNull private final Object bossBarLock = new Object();
    private boolean disconnected = false;

    /**
     * @param plugin The plugin which this APi player is hosted on
     * @param player The player to wrap
     * @param settings Any additional configuration settings for this bukkit api player.
     */
    public EApiPlayer(@NotNull BukkitPlugin plugin, @NotNull Player player, APIPlayerSettings settings) {
        this.player = player;
        this.plugin = plugin;
        this.scoreboard = null;
        this.actionBarManager = new ActionBarManager(plugin,this, settings.getActionBarFactory(),player);
        this.scoreboardFactory = settings.getScoreboardFactory();
        this.bossBarFactory = settings.getBossBarRendererFactory();
        this.uuid = player.getUniqueId();
        this.sender = plugin.getEServer().wrapSender(player);
    }

    /**
     * Creates a scoreboard for this player and sets it. The scoreboard is returned and then can be modified
     * accordingly. The scoreboard will have the maximum amount of lines
     *
     * @param name The name of the scoreboard. This is used to identify the scoreboard
     * @return A new scoreboard
     */
    @Override
    @Nullable
    public EScoreboard setScoreboard(@NotNull String name) {
        synchronized (scoreboardLock) {
            if(disconnected)
                return null;
            this.scoreboard = scoreboardFactory.get(plugin, player, name,plugin.getEServer().getServerVersion());
            return this.scoreboard;
        }
    }

    /**
     * Creates a scoreboard for this player and sets it. The scoreboard is returned and then can be modified
     * accordingly.
     *
     * @param name The name of the scoreboard. This is used to identify the scoreboard
     * @param lines The immutable amount of lines for the scoreboard
     * @return A new scoreboard
     */
    @Override
    @Nullable
    public EScoreboard setScoreboard(@NotNull String name, int lines) {
        synchronized (scoreboardLock) {
            if(disconnected)
                return null;
            this.scoreboard = scoreboardFactory.get(plugin, player, name, lines, plugin.getEServer().getServerVersion());
            return this.scoreboard;
        }
    }

    /**
     * Removes the current scoreboard that the player has. The player will no longer have that scoreboard
     * and it will no longer be visible to the player.
     */
    @Override
    public void removeScoreboard() {
        synchronized (scoreboardLock) {
            this.scoreboard = null;
            player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
        }
    }

    /**
     * @return the scoreboard currently being run. If null then the player is viewing no scoreboard.
     */
    @Override
    @Nullable
    public EScoreboard getScoreboard() {
        return this.scoreboard;
    }


    /**
     * @return The player's unique id. Even if the original reference is lost this will not return null.
     */
    @Override
    @NotNull
    public UUID getUniqueID() {
        return uuid;
    }

    /**
     * @return  The name of the player.
     */
    @Override
    @NotNull
    public String getName() {
        return player.getName();
    }

    /**
     * @return The localisation of the player. If the original reference is lost or the language cannot be parsed
     * then this will return the default locale. Otherwise it will return their locale.
     */
    @Override
    @NotNull
    public Locale getLocale() {
        return sender.getLocale();
    }


    /**
     * @return The action bar manager. This will allow you to manage the currently rendered action bar. You can send an action bar
     * with {@link ActionBarManager#send(ActionBar, RejectionPolicy, Object...)}
     */
    @Override
    @NotNull
    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    /**
     * @return The currently rendered boss bar. If the player has no viewed boss bar then this will return null.
     */
    @Override @Nullable
    public BossBar getBossBar() {
        return bossBar;
    }

    @Override @Nullable
    public BossBar setBossBar(@NotNull String name) {
        if(disconnected)
            return null;
        BossBar bossBar = new SimpleBossBar(plugin,player,name, bossBarFactory.get(plugin,player,plugin.getEServer().getServerVersion()));
        return setBossBar(bossBar);
    }

    @Override @Nullable
    public BossBar setBossBar(@NotNull String name, @NotNull Message message) {
        if(disconnected)
            return null;
        BossBar bossBar = new SimpleBossBar(plugin,player,name,message,bossBarFactory.get(plugin,player,plugin.getEServer().getServerVersion()));
        return setBossBar(bossBar);
    }

    @NotNull
    private BossBar setBossBar(@NotNull BossBar bossBar) {
        removeBossBar();
        this.bossBar = bossBar;
        return bossBar;
    }

    @Override
    public void removeBossBar() {
        if(this.bossBar !=null) {
            this.bossBar.setVisible(false);
        }
        this.bossBar = null;
    }

    /**
     * 'Disconnects' the player. This should be called once the player is no longer online. Once called
     *    - all display items will be removed
     *    - no new display items can be added.
     *    - Any display item can still be removed however.
     */
    @Override
    public void disconnect() {
        synchronized (bossBarLock) {
            synchronized (scoreboardLock) {
                if(disconnected)
                    return;
                this.disconnected = true;
            }
        }
        removeBossBar();
        removeScoreboard();
        actionBarManager.shutdown();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BukkitApiPlayer))
            return false;

        BukkitApiPlayer p = (BukkitApiPlayer) o;
        return Objects.equals(this.getUniqueID(),p.getUniqueID());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Unique ID",getUniqueID())
                .add("Name",getName())
                .toString();
    }
}
