package me.deltaorion.bukkit.display.scoreboard;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.plugin.version.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ScoreboardFactory {

    EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, int lines, @NotNull MinecraftVersion version);

    EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, @NotNull MinecraftVersion version);

    ScoreboardFactory WRAPPER = new ScoreboardFactory() {
        @Override
        public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, int lines, @NotNull MinecraftVersion version) {
            return new WrapperScoreboard(player,name,plugin,lines);
        }

        @Override
        public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, @NotNull MinecraftVersion version) {
            return new WrapperScoreboard(player,name,plugin);
        }
    };
}
