package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.jetbrains.annotations.NotNull;

public interface BossBarRendererFactory {

    public BossBarRenderer get(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player);

    static BossBarRendererFactory WITHER_ENTITY() {
        return new BossBarRendererFactory() {
            @Override
            public BossBarRenderer get(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player) {
                return new EntityBossBarRenderer(plugin,player);
            }
        };
    }

    static BossBarRendererFactory fromVersion(MinecraftVersion version) {
        return WITHER_ENTITY();
    }
}
