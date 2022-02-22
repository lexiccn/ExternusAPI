package me.deltaorion.bukkit.test.bukkit;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class TestLivingEntity {

    private final PlayerInventoryMock inventory = new PlayerInventoryMock(null);
    private final MockEntityEquipment equipment = new MockEntityEquipment(inventory);

    public LivingEntity asLivingEntity() {
        return (LivingEntity) Proxy.newProxyInstance(LivingEntity.class.getClassLoader(),
                new Class[]{LivingEntity.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if(method.getName().equals("getEquipment")) {
                            return equipment.asEquipment();
                        } else if(method.getName().equals("hashCode")) {
                            return TestLivingEntity.this.hashCode();
                        } else if(method.getName().equals("equals")) {
                            return TestLivingEntity.this.equals(args[0]);
                        } else if(method.getName().equals("toString")) {
                            return TestLivingEntity.this.toString();
                        }
                        throw new UnsupportedOperationException("Unsupported Method - "+method.getName());
                    }
                });
    }
    
    public double getEyeHeight() {
        return 0;
    }

    
    public double getEyeHeight(boolean ignoreSneaking) {
        return 0;
    }

    
    public Location getEyeLocation() {
        return null;
    }

    
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return null;
    }

    
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return null;
    }

    
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        return null;
    }

    
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return null;
    }

    
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return null;
    }

    
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return null;
    }

    
    public Egg throwEgg() {
        return null;
    }

    
    public Snowball throwSnowball() {
        return null;
    }

    
    public Arrow shootArrow() {
        return null;
    }

    
    public int getRemainingAir() {
        return 0;
    }

    
    public void setRemainingAir(int ticks) {

    }

    
    public int getMaximumAir() {
        return 0;
    }

    
    public void setMaximumAir(int ticks) {

    }

    
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    
    public void setMaximumNoDamageTicks(int ticks) {

    }

    
    public double getLastDamage() {
        return 0;
    }

    
    public int _INVALID_getLastDamage() {
        return 0;
    }

    
    public void setLastDamage(double damage) {

    }

    
    public void _INVALID_setLastDamage(int damage) {

    }

    
    public int getNoDamageTicks() {
        return 0;
    }

    
    public void setNoDamageTicks(int ticks) {

    }

    
    public Player getKiller() {
        return null;
    }

    
    public boolean addPotionEffect(PotionEffect effect) {
        return false;
    }

    
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return false;
    }

    
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        return false;
    }

    
    public boolean hasPotionEffect(PotionEffectType type) {
        return false;
    }

    
    public void removePotionEffect(PotionEffectType type) {

    }

    
    public Collection<PotionEffect> getActivePotionEffects() {
        return null;
    }

    
    public boolean hasLineOfSight(Entity other) {
        return false;
    }

    
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    
    public void setRemoveWhenFarAway(boolean remove) {

    }

    
    public EntityEquipment getEquipment() {
        return this.equipment.asEquipment();
    }

    
    public void setCanPickupItems(boolean pickup) {

    }

    
    public boolean getCanPickupItems() {
        return false;
    }

    
    public boolean isLeashed() {
        return false;
    }

    
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    
    public boolean setLeashHolder(Entity holder) {
        return false;
    }

    
    public void damage(double amount) {

    }

    
    public void _INVALID_damage(int amount) {

    }

    
    public void damage(double amount, Entity source) {

    }

    
    public void _INVALID_damage(int amount, Entity source) {

    }

    
    public double getHealth() {
        return 0;
    }

    
    public int _INVALID_getHealth() {
        return 0;
    }

    
    public void setHealth(double health) {

    }

    
    public void _INVALID_setHealth(int health) {

    }

    
    public double getMaxHealth() {
        return 0;
    }

    
    public int _INVALID_getMaxHealth() {
        return 0;
    }

    
    public void setMaxHealth(double health) {

    }

    
    public void _INVALID_setMaxHealth(int health) {

    }

    
    public void resetMaxHealth() {

    }

    
    public Location getLocation() {
        return null;
    }

    
    public Location getLocation(Location loc) {
        return null;
    }

    
    public void setVelocity(Vector velocity) {

    }

    
    public Vector getVelocity() {
        return null;
    }

    
    public boolean isOnGround() {
        return false;
    }

    
    public World getWorld() {
        return null;
    }

    
    public boolean teleport(Location location) {
        return false;
    }

    
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    
    public boolean teleport(Entity destination) {
        return false;
    }

    
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return false;
    }

    
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        return null;
    }

    
    public int getEntityId() {
        return 0;
    }

    
    public int getFireTicks() {
        return 0;
    }

    
    public int getMaxFireTicks() {
        return 0;
    }

    
    public void setFireTicks(int ticks) {

    }

    
    public void remove() {

    }

    
    public boolean isDead() {
        return false;
    }

    
    public boolean isValid() {
        return false;
    }

    
    public void sendMessage(String message) {

    }

    
    public void sendMessage(String[] messages) {

    }

    
    public Server getServer() {
        return null;
    }

    
    public String getName() {
        return null;
    }

    
    public Entity getPassenger() {
        return null;
    }

    
    public boolean setPassenger(Entity passenger) {
        return false;
    }

    
    public boolean isEmpty() {
        return false;
    }

    
    public boolean eject() {
        return false;
    }

    
    public float getFallDistance() {
        return 0;
    }

    
    public void setFallDistance(float distance) {

    }

    
    public void setLastDamageCause(EntityDamageEvent event) {

    }

    
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    
    public UUID getUniqueId() {
        return null;
    }

    
    public int getTicksLived() {
        return 0;
    }

    
    public void setTicksLived(int value) {

    }

    
    public void playEffect(EntityEffect type) {

    }

    
    public EntityType getType() {
        return null;
    }

    
    public boolean isInsideVehicle() {
        return false;
    }

    
    public boolean leaveVehicle() {
        return false;
    }

    
    public Entity getVehicle() {
        return null;
    }

    
    public void setCustomName(String name) {

    }

    
    public String getCustomName() {
        return null;
    }

    
    public void setCustomNameVisible(boolean flag) {

    }

    
    public boolean isCustomNameVisible() {
        return false;
    }

    
    public Entity.Spigot spigot() {
        return null;
    }

    
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

    }

    
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {

    }

    
    public boolean isPermissionSet(String name) {
        return false;
    }

    
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    
    public boolean hasPermission(String name) {
        return false;
    }

    
    public boolean hasPermission(Permission perm) {
        return false;
    }

    
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    
    public void removeAttachment(PermissionAttachment attachment) {

    }

    
    public void recalculatePermissions() {

    }

    
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    
    public boolean isOp() {
        return false;
    }

    
    public void setOp(boolean value) {

    }

    
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null;
    }

    
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return null;
    }
}