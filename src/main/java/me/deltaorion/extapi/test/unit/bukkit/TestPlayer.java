package me.deltaorion.extapi.test.unit.bukkit;

import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.*;

public class TestPlayer {

    private String name;
    private final PlayerInventoryMock inventory = new PlayerInventoryMock(this);
    private final MockEntityEquipment entityEquipment = new MockEntityEquipment(inventory);
    private final UUID uuid;
    private Scoreboard scoreboard;

    public TestPlayer(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
    }

    public Player asPlayer() {
        Class<Player> playerClass = Player.class;
        return (Player) Proxy.newProxyInstance(Player.class.getClassLoader(),
                new Class[]{Player.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        if(methodName.equals("getDisplayName")) {
                            return getDisplayName();
                        } else if(methodName.equals("setDisplayName")) {
                            setDisplayName((String) args[0]);
                            return null;
                        } else if(methodName.equals("getPlayerListName")) {
                            return getPlayerListName();
                        } else if(methodName.equals("setPlayerListName")) {
                            setPlayerListName((String) args[0]);
                            return null;
                        } else if(methodName.equals("getUniqueId")) {
                            return getUniqueId();
                        } else if(methodName.equals("setScoreboard")) {
                            setScoreboard((Scoreboard) args[0]);
                            return null;
                        } else if(methodName.equals("getScoreboard")) {
                            return getScoreboard();
                        } else if(methodName.equals("spigot")) {
                            return spigot();
                        } else if(methodName.equals("getEquipment")) {
                            return getEquipment();
                        } else if(methodName.equals("getInventory")) {
                            return getInventory();
                        } else if(methodName.equals("equals")) {
                            return TestPlayer.this.equals(args[0]);
                        } else if(methodName.equals("hashCode")) {
                            return TestPlayer.this.hashCode();
                        } else if(methodName.equals("toString")) {
                            return TestPlayer.this.toString();
                        } else if(methodName.equals("getItemInHand")) {
                            return getItemInHand();
                        } else if(methodName.equals("setItemInHand")) {
                            setItemInHand((org.bukkit.inventory.ItemStack) args[0]);
                            return null;
                        } else if(methodName.equals("getLocale")) {
                            return "en";
                        }
                        throw new UnsupportedOperationException("Unsupported Method - "+methodName);
                    }
                });
    }

    
    public String getDisplayName() {
        return name;
    }

    
    public void setDisplayName(String name) {
        this.name = name;
    }

    
    public String getPlayerListName() {
        return name;
    }

    
    public void setPlayerListName(String name) {
        this.name = name;
    }

    
    public void setCompassTarget(Location loc) {
        throw new UnsupportedOperationException();
    }

    
    public Location getCompassTarget() {
        throw new UnsupportedOperationException();
    }

    
    public InetSocketAddress getAddress() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isConversing() {
        throw new UnsupportedOperationException();
    }

    
    public void acceptConversationInput(String input) {
        throw new UnsupportedOperationException();
    }

    
    public boolean beginConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    
    public void abandonConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        throw new UnsupportedOperationException();
    }

    
    public void sendRawMessage(String message) {
        throw new UnsupportedOperationException();
    }

    
    public void kickPlayer(String message) {
        throw new UnsupportedOperationException();
    }

    
    public void chat(String msg) {
        throw new UnsupportedOperationException();
    }

    
    public boolean performCommand(String command) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isSneaking() {
        throw new UnsupportedOperationException();
    }

    
    public void setSneaking(boolean sneak) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isSprinting() {
        throw new UnsupportedOperationException();
    }

    
    public void setSprinting(boolean sprinting) {
        throw new UnsupportedOperationException();
    }

    
    public void saveData() {
        throw new UnsupportedOperationException();
    }

    
    public void loadData() {
        throw new UnsupportedOperationException();
    }

    
    public void setSleepingIgnored(boolean isSleeping) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isSleepingIgnored() {
        throw new UnsupportedOperationException();
    }

    
    public void playNote(Location loc, byte instrument, byte note) {
        throw new UnsupportedOperationException();
    }

    
    public void playNote(Location loc, Instrument instrument, Note note) {
        throw new UnsupportedOperationException();
    }

    
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    
    public void playSound(Location location, String sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    
    public void playEffect(Location loc, Effect effect, int data) {
        throw new UnsupportedOperationException();
    }

    
    public <T> void playEffect(Location loc, Effect effect, T data) {
        throw new UnsupportedOperationException();
    }

    
    public void sendBlockChange(Location loc, Material material, byte data) {
        throw new UnsupportedOperationException();
    }

    
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        throw new UnsupportedOperationException();
    }

    
    public void sendBlockChange(Location loc, int material, byte data) {
        throw new UnsupportedOperationException();
    }

    
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void sendMap(MapView map) {
        throw new UnsupportedOperationException();
    }

    
    public void sendMessage(BaseComponent component) {
        throw new UnsupportedOperationException();
    }

    
    public void sendMessage(BaseComponent... components) {
        throw new UnsupportedOperationException();
    }

    
    public void setPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer) {
        throw new UnsupportedOperationException();
    }

    
    public void setPlayerListHeaderFooter(BaseComponent header, BaseComponent footer) {
        throw new UnsupportedOperationException();
    }

    
    public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    
    public void setSubtitle(BaseComponent[] subtitle) {
        throw new UnsupportedOperationException();
    }

    
    public void setSubtitle(BaseComponent subtitle) {
        throw new UnsupportedOperationException();
    }

    
    public void showTitle(BaseComponent[] title) {
        throw new UnsupportedOperationException();
    }

    
    public void showTitle(BaseComponent title) {
        throw new UnsupportedOperationException();
    }

    
    public void showTitle(BaseComponent[] title, BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    
    public void showTitle(BaseComponent title, BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    
    public void sendTitle(Title title) {
        throw new UnsupportedOperationException();
    }

    
    public void updateTitle(Title title) {
        throw new UnsupportedOperationException();
    }

    
    public void hideTitle() {
        throw new UnsupportedOperationException();
    }

    
    public void updateInventory() {
        throw new UnsupportedOperationException();
    }

    
    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    
    public void removeAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    
    public boolean hasAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        throw new UnsupportedOperationException();
    }

    
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        throw new UnsupportedOperationException();
    }

    
    public void setPlayerTime(long time, boolean relative) {
        throw new UnsupportedOperationException();
    }

    
    public long getPlayerTime() {
        throw new UnsupportedOperationException();
    }

    
    public long getPlayerTimeOffset() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isPlayerTimeRelative() {
        throw new UnsupportedOperationException();
    }

    
    public void resetPlayerTime() {
        throw new UnsupportedOperationException();
    }

    
    public void setPlayerWeather(WeatherType type) {
        throw new UnsupportedOperationException();
    }

    
    public WeatherType getPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    
    public void resetPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    
    public void giveExp(int amount) {
        throw new UnsupportedOperationException();
    }

    
    public void giveExpLevels(int amount) {
        throw new UnsupportedOperationException();
    }

    
    public float getExp() {
        throw new UnsupportedOperationException();
    }

    
    public void setExp(float exp) {
        throw new UnsupportedOperationException();
    }

    
    public int getLevel() {
        throw new UnsupportedOperationException();
    }

    
    public void setLevel(int level) {
        throw new UnsupportedOperationException();
    }

    
    public int getTotalExperience() {
        throw new UnsupportedOperationException();
    }

    
    public void setTotalExperience(int exp) {
        throw new UnsupportedOperationException();
    }

    
    public float getExhaustion() {
        throw new UnsupportedOperationException();
    }

    
    public void setExhaustion(float value) {
        throw new UnsupportedOperationException();
    }

    
    public float getSaturation() {
        throw new UnsupportedOperationException();
    }

    
    public void setSaturation(float value) {
        throw new UnsupportedOperationException();
    }

    
    public int getFoodLevel() {
        throw new UnsupportedOperationException();
    }

    
    public void setFoodLevel(int value) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isOnline() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isBanned() {
        throw new UnsupportedOperationException();
    }

    
    public void setBanned(boolean banned) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isWhitelisted() {
        throw new UnsupportedOperationException();
    }

    
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    
    public Player getPlayer() {
        throw new UnsupportedOperationException();
    }

    
    public long getFirstPlayed() {
        throw new UnsupportedOperationException();
    }

    
    public long getLastPlayed() {
        throw new UnsupportedOperationException();
    }

    
    public boolean hasPlayedBefore() {
        throw new UnsupportedOperationException();
    }

    
    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    
    public void setBedSpawnLocation(Location location) {
        throw new UnsupportedOperationException();
    }

    
    public void setBedSpawnLocation(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException();
    }

    
    public void setAllowFlight(boolean flight) {
        throw new UnsupportedOperationException();
    }

    
    public void hidePlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    
    public void showPlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    
    public boolean canSee(Player player) {
        throw new UnsupportedOperationException();
    }

    
    public Location getLocation() {
        throw new UnsupportedOperationException();
    }

    
    public Location getLocation(Location loc) {
        throw new UnsupportedOperationException();
    }

    
    public void setVelocity(Vector velocity) {
        throw new UnsupportedOperationException();
    }

    
    public Vector getVelocity() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isOnGround() {
        throw new UnsupportedOperationException();
    }

    
    public World getWorld() {
        throw new UnsupportedOperationException();
    }

    
    public boolean teleport(Location location) {
        throw new UnsupportedOperationException();
    }

    
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    
    public boolean teleport(Entity destination) {
        throw new UnsupportedOperationException();
    }

    
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }

    
    public int getEntityId() {
        throw new UnsupportedOperationException();
    }

    
    public int getFireTicks() {
        throw new UnsupportedOperationException();
    }

    
    public int getMaxFireTicks() {
        throw new UnsupportedOperationException();
    }

    
    public void setFireTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    
    public void remove() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isDead() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }

    
    public void sendMessage(String message) {
        throw new UnsupportedOperationException();
    }

    
    public void sendMessage(String[] messages) {
        throw new UnsupportedOperationException();
    }

    
    public Server getServer() {
        throw new UnsupportedOperationException();
    }

    
    public Entity getPassenger() {
        throw new UnsupportedOperationException();
    }

    
    public boolean setPassenger(Entity passenger) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    
    public boolean eject() {
        throw new UnsupportedOperationException();
    }

    
    public float getFallDistance() {
        throw new UnsupportedOperationException();
    }

    
    public void setFallDistance(float distance) {
        throw new UnsupportedOperationException();
    }

    
    public void setLastDamageCause(EntityDamageEvent event) {
        throw new UnsupportedOperationException();
    }

    
    public EntityDamageEvent getLastDamageCause() {
        throw new UnsupportedOperationException();
    }

    
    public UUID getUniqueId() {
        return uuid;
    }

    
    public int getTicksLived() {
        throw new UnsupportedOperationException();
    }

    
    public void setTicksLived(int value) {
        throw new UnsupportedOperationException();
    }

    
    public void playEffect(EntityEffect type) {
        throw new UnsupportedOperationException();
    }

    
    public EntityType getType() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isInsideVehicle() {
        throw new UnsupportedOperationException();
    }

    
    public boolean leaveVehicle() {
        throw new UnsupportedOperationException();
    }

    
    public Entity getVehicle() {
        throw new UnsupportedOperationException();
    }

    
    public void setCustomName(String name) {
        throw new UnsupportedOperationException();
    }

    
    public String getCustomName() {
        throw new UnsupportedOperationException();
    }

    
    public void setCustomNameVisible(boolean flag) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isCustomNameVisible() {
        throw new UnsupportedOperationException();
    }

    
    public boolean isFlying() {
        throw new UnsupportedOperationException();
    }

    
    public void setFlying(boolean value) {
        throw new UnsupportedOperationException();
    }

    
    public void setFlySpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public float getFlySpeed() {
        throw new UnsupportedOperationException();
    }

    
    public float getWalkSpeed() {
        throw new UnsupportedOperationException();
    }

    
    public void setTexturePack(String url) {
        throw new UnsupportedOperationException();
    }

    
    public void setResourcePack(String url) {
        throw new UnsupportedOperationException();
    }

    
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        this.scoreboard = scoreboard;
    }

    
    public boolean isHealthScaled() {
        throw new UnsupportedOperationException();
    }

    
    public void setHealthScaled(boolean scale) {
        throw new UnsupportedOperationException();
    }

    
    public void setHealthScale(double scale) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    
    public double getHealthScale() {
        throw new UnsupportedOperationException();
    }

    public Entity getSpectatorTarget() {
        throw new UnsupportedOperationException();
    }

    
    public void setSpectatorTarget(Entity entity) {
        throw new UnsupportedOperationException();
    }

    
    public void sendTitle(String title, String subtitle) {
        throw new UnsupportedOperationException();
    }

    
    public void resetTitle() {
        throw new UnsupportedOperationException();
    }

    private static class TestSpigot extends Player.Spigot {
        @Override
        public String getLocale() {
            return "en";
        }
    }

    public Player.Spigot spigot() {
        return new TestSpigot();
    }


    
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }

    
    public String getName() {
        return name;
    }

    
    public PlayerInventory getInventory() {
        return inventory.asPlayerInventory();
    }

    
    public Inventory getEnderChest() {
        throw new UnsupportedOperationException();
    }

    
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        throw new UnsupportedOperationException();
    }

    
    public InventoryView getOpenInventory() {
        throw new UnsupportedOperationException();
    }

    
    public InventoryView openInventory(Inventory inventory) {
        throw new UnsupportedOperationException();
    }

    
    public InventoryView openWorkbench(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    
    public InventoryView openEnchanting(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    
    public void openInventory(InventoryView inventory) {
        throw new UnsupportedOperationException();
    }

    
    public void closeInventory() {
        throw new UnsupportedOperationException();
    }

    
    public ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    
    public void setItemInHand(ItemStack item) {
        inventory.setItemInHand(item);
    }

    
    public ItemStack getItemOnCursor() {
        throw new UnsupportedOperationException();
    }

    
    public void setItemOnCursor(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isSleeping() {
        throw new UnsupportedOperationException();
    }

    
    public int getSleepTicks() {
        throw new UnsupportedOperationException();
    }

    
    public GameMode getGameMode() {
        throw new UnsupportedOperationException();
    }

    
    public void setGameMode(GameMode mode) {
        throw new UnsupportedOperationException();
    }

    
    public boolean isBlocking() {
        throw new UnsupportedOperationException();
    }

    
    public int getExpToLevel() {
        throw new UnsupportedOperationException();
    }

    
    public double getEyeHeight() {
        throw new UnsupportedOperationException();
    }

    
    public double getEyeHeight(boolean ignoreSneaking) {
        throw new UnsupportedOperationException();
    }

    
    public Location getEyeLocation() {
        throw new UnsupportedOperationException();
    }

    
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    
    public Egg throwEgg() {
        throw new UnsupportedOperationException();
    }

    
    public Snowball throwSnowball() {
        throw new UnsupportedOperationException();
    }

    
    public Arrow shootArrow() {
        throw new UnsupportedOperationException();
    }

    
    public int getRemainingAir() {
        throw new UnsupportedOperationException();
    }

    
    public void setRemainingAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    
    public int getMaximumAir() {
        throw new UnsupportedOperationException();
    }

    
    public void setMaximumAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    
    public int getMaximumNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    
    public void setMaximumNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    
    public double getLastDamage() {
        throw new UnsupportedOperationException();
    }

    
    public int _INVALID_getLastDamage() {
        throw new UnsupportedOperationException();
    }

    
    public void setLastDamage(double damage) {
        throw new UnsupportedOperationException();
    }

    
    public void _INVALID_setLastDamage(int damage) {
        throw new UnsupportedOperationException();
    }

    
    public int getNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    
    public void setNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    
    public Player getKiller() {
        throw new UnsupportedOperationException();
    }

    
    public boolean addPotionEffect(PotionEffect effect) {
        throw new UnsupportedOperationException();
    }

    
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        throw new UnsupportedOperationException();
    }

    
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        throw new UnsupportedOperationException();
    }

    
    public boolean hasPotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    
    public void removePotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    
    public Collection<PotionEffect> getActivePotionEffects() {
        throw new UnsupportedOperationException();
    }

    
    public boolean hasLineOfSight(Entity other) {
        throw new UnsupportedOperationException();
    }

    
    public boolean getRemoveWhenFarAway() {
        throw new UnsupportedOperationException();
    }

    
    public void setRemoveWhenFarAway(boolean remove) {
        throw new UnsupportedOperationException();
    }

    
    public EntityEquipment getEquipment() {
        return entityEquipment.asEquipment();
    }

    
    public void setCanPickupItems(boolean pickup) {
        throw new UnsupportedOperationException();
    }

    
    public boolean getCanPickupItems() {
        throw new UnsupportedOperationException();
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

    
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {

    }

    
    public Set<String> getListeningPluginChannels() {
        return null;
    }

    
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null;
    }

    
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return null;
    }
}