package me.deltaorion.bukkit.display.bossbar;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.plugin.version.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BossBarRendererFactory {

    public BossBarRenderer get(@NotNull BukkitPlugin plugin, @NotNull Player bukkitPlayer, @NotNull MinecraftVersion version);

    static BossBarRendererFactory WITHER_ENTITY() {
        return new BossBarRendererFactory() {
            @Override
            public BossBarRenderer get(@NotNull BukkitPlugin plugin, @NotNull Player bukkitPlayer, @NotNull MinecraftVersion version) {
                return new EntityBossBarRenderer(plugin,bukkitPlayer);
            }
        };
    }

    static BossBarRendererFactory FROM_VERSION() {
        return new BossBarRendererFactory() {
            @Override
            public BossBarRenderer get(@NotNull BukkitPlugin plugin,  @NotNull Player bukkitPlayer, @NotNull MinecraftVersion version) {
                if(version.getMajor()==8) {
                    return new EntityBossBarRenderer(plugin, bukkitPlayer);
                } else if(version.getMajor()>=9 && version.getMajor()<12) {
                    return new PacketBossBarRenderer(plugin,bukkitPlayer);
                } else {
                    return new WrapperBossBarRenderer(bukkitPlayer,plugin);
                }
            }
        };
    }
}
