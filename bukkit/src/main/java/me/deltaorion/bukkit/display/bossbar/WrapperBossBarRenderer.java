package me.deltaorion.bukkit.display.bossbar;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WrapperBossBarRenderer implements BossBarRenderer {

    @NotNull private final Player player;
    @NotNull private final BossBar bukkitBossBar;

    public WrapperBossBarRenderer(@NotNull Player player, @NotNull BukkitPlugin plugin) {
        this.player = player;
        this.bukkitBossBar = plugin.getServer().createBossBar("", org.bukkit.boss.BarColor.PINK, org.bukkit.boss.BarStyle.SOLID);
        this.bukkitBossBar.addPlayer(player);
    }

    @Override
    public void setMessage(@NotNull String render) {
        bukkitBossBar.setTitle(render);
    }

    @Override
    public void setProgress(float progress) {
        bukkitBossBar.setProgress(progress);
    }

    @Override
    public void setVisible(boolean visible) {
        bukkitBossBar.setVisible(visible);
        if(!visible) {
            //should stop memory leaks
            bukkitBossBar.removePlayer(player);
        } else {
            bukkitBossBar.addPlayer(player);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void setColor(@NotNull BarColor color) {
        bukkitBossBar.setColor(color.getBukkitColor());
    }

    @Override
    public void setStyle(@NotNull BarStyle style) {
        bukkitBossBar.setStyle(style.getBukkitStyle());
    }

    @Override
    public void setCreateFog(boolean createFog) {
        setFlag(createFog, BarFlag.CREATE_FOG);
    }

    @Override
    public void setDarkenSky(boolean darkenSky) {
        setFlag(darkenSky, BarFlag.DARKEN_SKY);
    }

    @Override
    public void setPlayMusic(boolean playMusic) {
        setFlag(playMusic, BarFlag.PLAY_BOSS_MUSIC);
    }

    private void setFlag(boolean set, BarFlag flag) {
        if(set) {
            bukkitBossBar.addFlag(flag);
        } else {
            bukkitBossBar.removeFlag(flag);
        }
    }
}
