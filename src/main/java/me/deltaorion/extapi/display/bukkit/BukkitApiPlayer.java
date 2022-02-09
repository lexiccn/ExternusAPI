package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.display.actionbar.ActionBarManager;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.scoreboard.EScoreboard;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class BukkitApiPlayer {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final WeakReference<Player> player;
    @Nullable private EScoreboard scoreboard;
    @NotNull private final ActionBarManager actionBarManager;
    @Nullable private BossBar bossBar;

    public BukkitApiPlayer(@NotNull BukkitPlugin plugin, @NotNull Player player, APIPlayerSettings settings) {
        this.player = new WeakReference<>(player);
        this.plugin = plugin;
        this.scoreboard = null;
        this.actionBarManager = new ActionBarManager(plugin,this, settings.getFactory());
    }

    public void setScoreboard(@Nullable EScoreboard scoreboard) {
        this.scoreboard = scoreboard;
        Player player = getPlayer();
        if(player==null)
            return;

        if(scoreboard==null) {
            player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
        }
    }

    @Nullable
    public EScoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Nullable
    public Player getPlayer() {
        return player.get();
    }

    @Nullable
    public UUID getUniqueID() {
        Player player = getPlayer();
        if(player==null)
            return null;

        return player.getUniqueId();
    }

    @Nullable
    public String getName() {
        Player player = getPlayer();
        if(player==null)
            return null;

        return player.getName();
    }

    @NotNull
    public Locale getLocale() {
        Player player = getPlayer();
        if(player==null)
            return EServer.DEFAULT_LOCALE;

        Locale locale = Translator.parseLocale(player.spigot().getLocale());
        if(locale==null)
            return EServer.DEFAULT_LOCALE;

        return locale;
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

    @NotNull
    public ActionBarManager getActionBarManager() {
        return actionBarManager;
    }

    @Nullable
    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(@Nullable BossBar bossBar) {
        if(Objects.equals(bossBar,this.bossBar))
            return;

        if(this.bossBar!=null) {
            this.bossBar.setVisible(false);
        }
        this.bossBar = bossBar;
    }

    public void cleanUp() {
        setBossBar(null);
        actionBarManager.clear();
        setScoreboard(null);
    }
}
