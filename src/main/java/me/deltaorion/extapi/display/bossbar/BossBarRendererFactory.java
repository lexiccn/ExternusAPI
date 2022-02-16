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

    static BossBarRendererFactory FROM_VERSION(@NotNull MinecraftVersion version) {
        return new BossBarRendererFactory() {
            @Override
            public BossBarRenderer get(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player) {
                if(version.getMajor()==8) {
                    return new EntityBossBarRenderer(plugin, player);
                } else if(version.getMajor()>=9 && version.getMajor()<12) {
                    return new PacketBossBarRenderer(plugin,player.getPlayer());
                } else {
                    return new WrapperBossBarRenderer(player.getPlayer(),plugin);
                }
            }
        };
    }
}
