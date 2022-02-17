package me.deltaorion.extapi.display.scoreboard;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ScoreboardFactory {

    public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, int lines, @NotNull MinecraftVersion version);

    public EScoreboard get(@NotNull BukkitPlugin plugin, @NotNull Player player,@NotNull String name, @NotNull MinecraftVersion version);

    static ScoreboardFactory WRAPPER = new ScoreboardFactory() {
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
