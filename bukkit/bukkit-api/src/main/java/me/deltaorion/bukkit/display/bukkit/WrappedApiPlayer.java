package me.deltaorion.bukkit.display.bukkit;

import com.google.common.base.MoreObjects;
import me.deltaorion.bukkit.display.actionbar.ActionBarManager;
import me.deltaorion.bukkit.display.bossbar.EBossBar;
import me.deltaorion.bukkit.display.scoreboard.EScoreboard;
import me.deltaorion.common.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public class WrappedApiPlayer implements BukkitApiPlayer {

    @NotNull private final BukkitApiPlayer player;

    public WrappedApiPlayer(@NotNull BukkitApiPlayer player) {
        this.player = player;
    }

    @Nullable @Override
    public EScoreboard setScoreboard(@NotNull String name) {
        return player.setScoreboard(name);
    }

    @Nullable
    @Override
    public EScoreboard setScoreboard(@NotNull String name, int lines) {
        return player.setScoreboard(name,lines);
    }

    @Override
    public void removeScoreboard() {
        player.removeScoreboard();
    }

    @Nullable
    @Override
    public EScoreboard getScoreboard() {
        return player.getScoreboard();
    }

    @NotNull
    @Override
    public UUID getUniqueID() {
        return player.getUniqueID();
    }

    @NotNull
    @Override
    public String getName() {
        return player.getName();
    }

    @NotNull
    @Override
    public Locale getLocale() {
        return player.getLocale();
    }

    @NotNull
    @Override
    public ActionBarManager getActionBarManager() {
        return player.getActionBarManager();
    }

    @Nullable
    @Override
    public EBossBar getBossBar() {
        return player.getBossBar();
    }

    @Nullable
    @Override
    public EBossBar setBossBar(@NotNull String name) {
        return player.setBossBar(name);
    }

    @Nullable
    @Override
    public EBossBar setBossBar(@NotNull String name, @NotNull Message message) {
        return player.setBossBar(name,message);
    }

    @Override
    public void removeBossBar() {
        player.removeBossBar();
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Player can only be disconnected upon leaving the server");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wrapped",player).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BukkitApiPlayer))
            return false;

        BukkitApiPlayer p = (BukkitApiPlayer) o;
        return java.util.Objects.equals(this.getUniqueID(),p.getUniqueID());
    }
}
