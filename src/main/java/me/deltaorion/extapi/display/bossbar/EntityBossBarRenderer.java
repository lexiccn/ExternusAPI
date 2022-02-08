package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityBossBarRenderer implements BossBarRenderer {

    private static final int ENTITY_DISTANCE = 32;
    @NotNull private final FakeWither wither;
    @NotNull private final Player player;

    public EntityBossBarRenderer(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, boolean visible, float progress, String render) {
        if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
            throw new MissingDependencyException("Cannot make a BossBar as '"+BukkitAPIDepends.PROTOCOL_LIB.getName()+"' is not installed!");
        this.player = player.getPlayer();
        Location initialLocation = makeLocation(player.getPlayer().getLocation());
        this.wither = new FakeWither(player.getPlayer(),initialLocation);
        setProgress(progress);
        setMessage(render);
        if(visible)
            wither.spawn();
    }

    private int getHealth(float progress) {
        return (int) Math.max(FakeWither.MIN_HEALTH,FakeWither.MAX_HEALTH*progress);
    }

    protected Location makeLocation(Location base) {
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

        wither.teleport(makeLocation(player.getLocation()));
    }
}
