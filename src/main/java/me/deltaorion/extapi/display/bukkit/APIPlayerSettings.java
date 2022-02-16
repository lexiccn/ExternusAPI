package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.display.actionbar.ActionBarFactories;
import me.deltaorion.extapi.display.actionbar.ActionBarFactory;
import me.deltaorion.extapi.display.actionbar.renderer.PacketActionBarRenderer;
import me.deltaorion.extapi.display.bossbar.BossBarRendererFactory;
import me.deltaorion.extapi.display.scoreboard.ScoreboardFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class APIPlayerSettings {

    @NotNull private ActionBarFactory actionBarFactory;
    @NotNull private ScoreboardFactory scoreboardFactory;

    public APIPlayerSettings() {
        this.actionBarFactory = ActionBarFactories.SCHEDULE_FROM_VERSION();
        this.scoreboardFactory = ScoreboardFactory.WRAPPER;
    }

    public APIPlayerSettings setActionBarFactory(@NotNull ActionBarFactory factory) {
        this.actionBarFactory = Objects.requireNonNull(factory);
        return this;
    }

    @NotNull
    public ActionBarFactory getActionBarFactory() {
        return actionBarFactory;
    }

    public APIPlayerSettings setScoreboardFactory(@NotNull ScoreboardFactory factory) {
        this.scoreboardFactory = Objects.requireNonNull(factory);
        return this;
    }

    @NotNull
    public ScoreboardFactory getScoreboardFactory() {
        return scoreboardFactory;
    }
}
