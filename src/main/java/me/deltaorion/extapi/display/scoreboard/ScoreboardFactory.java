package me.deltaorion.extapi.display.scoreboard;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ScoreboardFactory {

    public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, int lines);

    public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player,@NotNull String name);

    static ScoreboardFactory WRAPPER = new ScoreboardFactory() {
        @Override
        public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, int lines) {
            return new WrapperScoreboard(player,name,plugin,lines);
        }

        @Override
        public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name) {
            return new WrapperScoreboard(player,name,plugin);
        }
    };
}
