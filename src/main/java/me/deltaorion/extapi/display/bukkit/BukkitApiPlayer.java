package me.deltaorion.extapi.display.bukkit;

import com.google.common.base.Preconditions;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.display.actionbar.ActionBar;
import me.deltaorion.extapi.display.actionbar.ActionBarManager;
import me.deltaorion.extapi.display.actionbar.RejectionPolicy;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.scoreboard.EScoreboard;
import me.deltaorion.extapi.display.scoreboard.ScoreboardFactory;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * A Bukkit API Player is a player wrapper that provides additional functionality to a player for the Display API. One can
 *   - add a scoreboard with {@link #setScoreboard(String, int)}
 *   - set the bossbar {@link #setBossBar(BossBar)}
 *   - set the actionbar {@link #getActionBarManager()}
 *
 * The API player only holds a strong reference to the player. It is advised not to keep ones own reference to a Bukkit API player
 * as it can potentially create memory leaks. Instead one should grab it from the BukkitPlayerManager
 *
 * References to BukkitApiPlayers are kept in {@link BukkitPlayerManager}. One should not create their own BukkitApiPlayers.
 *
 * The API player is merely an extension of a {@link Player}. Hence it only represents a player who is online.
 */
public class BukkitApiPlayer {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Player player;
    @Nullable private EScoreboard scoreboard;
    @NotNull private final ActionBarManager actionBarManager;
    @NotNull private final ScoreboardFactory factory;
    @Nullable private BossBar bossBar;

    @NotNull private final UUID uuid;

    /**
     * @param plugin The plugin which this APi player is hosted on
     * @param player The player to wrap
     * @param settings Any additional configuration settings for this bukkit api player.
     */
    public BukkitApiPlayer(@NotNull BukkitPlugin plugin, @NotNull Player player, APIPlayerSettings settings) {
        this.player = player;
        this.plugin = plugin;
        this.scoreboard = null;
        this.actionBarManager = new ActionBarManager(plugin,this, settings.getActionBarFactory());
        this.factory = settings.getScoreboardFactory();
        this.uuid = player.getUniqueId();
    }

    @NotNull
    public EScoreboard setScoreboard(@NotNull String name) {
        this.scoreboard = factory.get(plugin,player,name);
        return this.scoreboard;
    }

    @NotNull
    public EScoreboard setScoreboard(@NotNull String name, int lines) {
        this.scoreboard = factory.get(plugin,player,name,lines);
        return this.scoreboard;
    }

    public void removeScoreboard() {
        this.scoreboard = null;
        player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
    }

    /**
     * @return the scoreboard currently being run. If null then the player is viewing no scoreboard.
     */
    @Nullable
    public EScoreboard getScoreboard() {
        return this.scoreboard;
    }

    /**
     * @return The player that this is wrapped around.
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * @return The player's unique id. Even if the original reference is lost this will not return null.
     */
    @NotNull
    public UUID getUniqueID() {
        return uuid;
    }

    /**
     * @return  The name of the player.
     */
    @NotNull
    public String getName() {
        return player.getName();
    }

    /**
     * @return The localisation of the player. If the original reference is lost or the language cannot be parsed
     * then this will return the default locale. Otherwise it will return their locale.
     */
    @NotNull
    public Locale getLocale() {
        Player player = getPlayer();
        Locale locale = Translator.parseLocale(player.spigot().getLocale());
        if(locale==null)
            return EServer.DEFAULT_LOCALE;

        return locale;
    }


    /**
     * @return The action bar manager. This will allow you to manage the currently rendered action bar. You can send an action bar
     * with {@link ActionBarManager#send(ActionBar, RejectionPolicy, Object...)}
     */
    @NotNull
    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    /**
     * @return The currently rendered boss bar. If the player has no viewed boss bar then this will return null.
     */
    @Nullable
    public BossBar getBossBar() {
        return bossBar;
    }

    /**
     * @param bossBar Sets the bossbar of the player. This does not normally need to be done as creating
     * a new bossbar should automatically do this.
     */

    public void setBossBar(@Nullable BossBar bossBar) {
        if(bossBar!=null)
            Preconditions.checkArgument(bossBar.getPlayer().equals(this));

        if(Objects.equals(bossBar,this.bossBar))
            return;

        if(this.bossBar!=null) {
            this.bossBar.setVisible(false);
        }
        this.bossBar = bossBar;
    }

    /**
     * Removes all display items. This must ensure that there are no references kept to this BukkitApiPlayer by any objects it is
     * hosting.
     */
    public void clearAll() {
        setBossBar(null);
        actionBarManager.clear();
        removeScoreboard();
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
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Unique ID",getUniqueID())
                .add("Name",getName())
                .toString();
    }
}
