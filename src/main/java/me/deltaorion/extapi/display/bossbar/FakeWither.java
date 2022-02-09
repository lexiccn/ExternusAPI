package me.deltaorion.extapi.display.bossbar;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.deltaorion.extapi.protocol.WrapperPlayServerEntityDestroy;
import me.deltaorion.extapi.protocol.WrapperPlayServerEntityMetadata;
import me.deltaorion.extapi.protocol.WrapperPlayServerEntityTeleport;
import me.deltaorion.extapi.protocol.WrapperPlayServerSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

//https://gist.github.com/aadnk/9373802
public class FakeWither {
    public static final byte INVISIBLE = 0x20;
    // Just a guess
    public static final int MAX_HEALTH = 300;
    public static final int MIN_HEALTH = 1;
    // Next entity ID
    private static final AtomicInteger NEXT_ID = new AtomicInteger(200000);

    private static final int METADATA_WITHER_HEALTH = 6; // 1.5.2 -> Change to 16

    // Metadata indices
    private static final int METADATA_FLAGS = 0;
    private static final int METADATA_NAME = 2;        // 1.5.2 -> Change to 5
    private static final int METADATA_SHOW_NAME = 3;   // 1.5.2 -> Change to 6
    private static final int METADATA_WITHER_INVULNERABLE = 20;

    // Unique ID
    private final int id = NEXT_ID.getAndIncrement();
    // Default health
    private int health = MAX_HEALTH;

    private boolean visible;
    @Nullable private String customName;
    private boolean created;

    @NotNull private Location location;
    @NotNull private final WeakReference<Player> player;

    public FakeWither(@NotNull Player player, @NotNull Location location) {
        this.location = location;
        this.player = new WeakReference<>(player);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        // Update the health of the entity
        if (created) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            watcher.setObject(METADATA_WITHER_HEALTH, (float) health);
            watcher.setObject(METADATA_WITHER_INVULNERABLE,1000);
            sendMetadata(watcher);
        }
        this.health = health;
    }

    public void setVisible(boolean visible) {
        // Make visible or invisible
        if (created) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            watcher.setObject(METADATA_FLAGS, visible ? (byte)0 : INVISIBLE);
            sendMetadata(watcher);
        }
        this.visible = visible;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setCustomName(String name) {
        if (created) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            if (name != null) {
                watcher.setObject(METADATA_NAME, name);
                watcher.setObject(METADATA_SHOW_NAME, (byte) 1);
            } else {
                // Hide custom name
                watcher.setObject(METADATA_SHOW_NAME, (byte) 0);
            }

            // Only players nearby when this is sent will see this name
            sendMetadata(watcher);
        }
        this.customName = name;
    }

    public void teleport(Location location) {
        if(created) {
            sendTeleport(location);
        }
        this.location = location;
    }

    private void sendTeleport(Location location) {
        Player player = getPlayer();
        if(player==null)
            return;
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
        packet.setEntityID(id);
        packet.setX(location.getX());
        packet.setY(location.getY());
        packet.setZ(location.getZ());
        packet.setPitch(location.getPitch());
        packet.setYaw(location.getYaw());
        packet.sendPacket(player);
    }

    private Player getPlayer() {
        return player.get();
    }

    @NotNull
    public Location getLocation() {
        return location;
    }

    private void sendMetadata(WrappedDataWatcher watcher) {
        Player player = getPlayer();
        if(player==null)
            return;
        WrapperPlayServerEntityMetadata update = new WrapperPlayServerEntityMetadata();
        update.setEntityId(id);
        update.setEntityMetadata(watcher.getWatchableObjects());
        update.sendPacket(player);
    }

    public int getId() {
        return id;
    }

    public boolean isSpawned() {
        return created;
    }

    public void spawn() {
        if(created)
            return;

        Player player = getPlayer();
        if(player==null)
            return;

        WrapperPlayServerSpawnEntityLiving spawnMob = new WrapperPlayServerSpawnEntityLiving();
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(METADATA_FLAGS, visible ? (byte)0 : INVISIBLE);
        watcher.setObject(METADATA_WITHER_HEALTH, (float) health); // 1.5.2 -> Change to (int)
        watcher.setObject(METADATA_WITHER_INVULNERABLE,1000);

        if (customName != null) {
            watcher.setObject(METADATA_NAME, customName);
            watcher.setObject(METADATA_SHOW_NAME, (byte) 1);
        }

        spawnMob.setEntityID(id);
        spawnMob.setType(EntityType.WITHER);
        spawnMob.setX(location.getX());
        spawnMob.setY(location.getY());
        spawnMob.setZ(location.getZ());
        spawnMob.setMetadata(watcher);
        spawnMob.sendPacket(player);
        created = true;
    }

    public void destroy() {
        if (!created)
            return;

        Player player = getPlayer();
        if(player==null)
            return;

        WrapperPlayServerEntityDestroy destroyMe = new WrapperPlayServerEntityDestroy();
        destroyMe.setEntities(new int[] { id });

        destroyMe.sendPacket(player);
        created = false;
    }

}
