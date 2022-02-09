package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class EntityBossBarRenderer implements BossBarRenderer {

    private static final int ENTITY_DISTANCE = 32;
    @Nullable private final FakeWither wither;
    @NotNull private final WeakReference<Player> player;

    public EntityBossBarRenderer(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player) {
        if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
            throw new MissingDependencyException("Cannot make a BossBar as '"+BukkitAPIDepends.PROTOCOL_LIB.getName()+"' is not installed!");
        Player p = player.getPlayer();
        this.player = new WeakReference<>(p);
        Location initialLocation = makeLocation();
        if(initialLocation==null || p==null) {
            this.wither = null;
            return;
        }
        this.wither = new FakeWither(p,initialLocation);
    }

    private int getHealth(float progress) {
        return (int) Math.max(FakeWither.MIN_HEALTH,FakeWither.MAX_HEALTH*progress);
    }

    @Nullable
    private Location makeLocation() {
        Player player = getPlayer();
        if(player==null)
            return null;
        Location base = player.getLocation();
        return base.getDirection().multiply(ENTITY_DISTANCE).add(base.toVector()).toLocation(player.getWorld());
    }

    @Override
    public void setMessage(@NotNull String render) {
        if(this.wither==null)
            return;
        this.wither.setCustomName(render);
    }

    @Override
    public void setProgress(float progress) {
        if(this.wither==null)
            return;
        this.wither.setHealth(getHealth(progress));
    }

    @Override
    public void setVisible(boolean visible) {
        if(this.wither==null)
            return;
        if(visible) {
            this.wither.spawn();
        } else {
            this.wither.destroy();
        }
    }

    @Nullable
    private Player getPlayer() {
        return this.player.get();
    }

    @Override
    public void update() {
        if(this.wither==null)
            return;
        if(!this.wither.isSpawned())
            return;

        wither.teleport(makeLocation());
    }
}
