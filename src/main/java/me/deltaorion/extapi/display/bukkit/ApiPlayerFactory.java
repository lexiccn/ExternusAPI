package me.deltaorion.extapi.display.bukkit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ApiPlayerFactory {

    public BukkitApiPlayer get(@NotNull BukkitPlugin plugin, @NotNull Player player);

    static ApiPlayerFactory DEFAULT = new ApiPlayerFactory() {
        @Override
        public BukkitApiPlayer get(@NotNull BukkitPlugin plugin, @NotNull Player player) {
            return new BukkitApiPlayer(plugin,player,new APIPlayerSettings());
        }
    };
}
