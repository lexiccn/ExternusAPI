package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityBossBarRenderer implements BossBarRenderer {

    @NotNull private final Player player;
    private static final int ENTITY_DISTANCE = 32;
    @NotNull private final FakeWither wither;

    public EntityBossBarRenderer(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player) {
        if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
            throw new MissingDependencyException("Cannot make a BossBar as '"+BukkitAPIDepends.PROTOCOL_LIB.getName()+"' is not installed!");
        this.player = player.getPlayer();
        Location initialLocation = makeLocation();
        this.wither = new FakeWither(this.player,initialLocation);
    }

    private int getHealth(float progress) {
        return (int) Math.max(FakeWither.MIN_HEALTH,FakeWither.MAX_HEALTH*progress);
    }

    @NotNull
    private Location makeLocation() {
        Location base = player.getLocation();
        return base.getDirection().multiply(ENTITY_DISTANCE).add(base.toVector()).toLocation(player.getWorld());
    }

    @Override
    public void setMessage(@NotNull String render) {
        this.wither.setCustomName(render);
    }

    @Override
    public void setProgress(float progress) {
        this.wither.setHealth(getHealth(progress));
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible) {
            this.wither.spawn();
        } else {
            this.wither.destroy();
        }
    }

    @Override
    public void update() {
        if(!this.wither.isSpawned())
            return;

        wither.teleport(makeLocation());
    }
}
