package me.deltaorion.bukkit.test.animation;

import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.plugin.EPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CobbleLineAnimation implements AnimationRenderer<PolarVector, Location> {

    private final EPlugin plugin;
    private final List<LocationSet> toSet = new ArrayList<>();

    public CobbleLineAnimation(EPlugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    private PolarVector previous = null;

    @Override
    public void render(@NotNull RunningAnimation<Location> animation , @NotNull MinecraftFrame<PolarVector> frame, @NotNull Location screen) {
        if (frame.getObject() == null)
            throw new NullPointerException();

        PolarVector vector = frame.getObject();
        if (previous != null) {
            draw(previous, screen, Material.AIR);
        }
        draw(vector, screen, Material.COBBLESTONE);
        this.previous = frame.getObject();
        completeDraw();

    }

    private void completeDraw() {
        List<LocationSet> deepCopy = new ArrayList<>(toSet);
        if(Bukkit.isPrimaryThread()) {
            for (LocationSet set : deepCopy) {
                set.location.getWorld().getBlockAt(set.location).setType(set.material);
            }
        } else {
            plugin.getScheduler().scheduleSyncDelayedTask(new Runnable() {
                @Override
                public void run() {
                    for (LocationSet set : deepCopy) {
                        set.location.getWorld().getBlockAt(set.location).setType(set.material);
                    }
                }
            });
        }
        toSet.clear();
    }

    private void draw(PolarVector vector, Location screen, Material material) {
        Vector2d p1 = new Vector2d(screen.getBlockX(), screen.getBlockY());
        Vector2d p2 = new Vector2d(screen.getBlockX() + vector.getCartesianX(), screen.getBlockY() + vector.getCartesianY());

        draw(p1, p2, screen.getWorld(), material, screen.getBlockZ());
    }

    private void draw(Vector2d p1, Vector2d p2, World world, Material material, int z) {
        drawLine((int) p1.x,(int) p1.y,(int) p2.x,(int) p2.y, material, world, z);
    }

    private void drawLine(int x0, int y0, int x1, int y1, Material material, World world, int z) {
        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;  /* error value e_xy */
        while (true) {  /* loop */
            plot(x0, y0,material,world,z);
            if (x0 == x1 && y0 == y1) break;
            int e2 = 2 * err;
            if (e2 >= dy) { /* e_xy+e_x > 0 */
                if (x0 == x1)
                    break;
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) { /* e_xy+e_y < 0 */
                if (y0 == y1)
                    break;
                err += dx;
                y0 += sy;
            }
        }
    }

    private void plot(int x, int y, Material material, World world, int z) {
        toSet.add(new LocationSet(material, new Location(world, x, y, z)));
    }


    @NotNull
    @Override
    public AnimationRenderer<PolarVector, Location> getNewRenderer() {
        return new CobbleLineAnimation(plugin);
    }

    @Override
    public boolean beforeCompletion(@NotNull RunningAnimation<Location> animation) {
        for(Location screen : animation.getScreens()) {
            if(previous!=null) {
                draw(previous, screen, Material.AIR);
            }
        }

        try {
            completeDraw();
        } catch (IllegalPluginAccessException ignored) {

        }
        previous=null;
        return true;
    }

    private static class LocationSet {
        @Nullable
        private final Material material;
        @NotNull
        private final Location location;

        private LocationSet(@Nullable Material material, @NotNull Location location) {
            this.material = material;
            this.location = location;
        }
    }
}
