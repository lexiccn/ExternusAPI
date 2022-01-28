package me.deltaorion.extapi.test.unit.bukkit;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryType;
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
import org.github.paperspigot.Title;

import java.net.InetSocketAddress;
import java.util.*;

public class TestPlayer implements Player {

    private String name;
    private final PlayerInventory inventory = new PlayerInventoryMock(this);
    private final EntityEquipment entityEquipment = new MockEntityEquipment(inventory,this);

    public TestPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public void setDisplayName(String name) {
        this.name = name;
    }

    @Override
    public String getPlayerListName() {
        return name;
    }

    @Override
    public void setPlayerListName(String name) {
        this.name = name;
    }

    @Override
    public void setCompassTarget(Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getCompassTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InetSocketAddress getAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isConversing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptConversationInput(String input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRawMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void kickPlayer(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void chat(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean performCommand(String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSneaking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSneaking(boolean sneak) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSprinting() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSleepingIgnored() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMap(MapView map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(BaseComponent component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListHeaderFooter(BaseComponent[] header, BaseComponent[] footer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerListHeaderFooter(BaseComponent header, BaseComponent footer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubtitle(BaseComponent[] subtitle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubtitle(BaseComponent subtitle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showTitle(BaseComponent[] title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showTitle(BaseComponent title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showTitle(BaseComponent[] title, BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showTitle(BaseComponent title, BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendTitle(Title title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateTitle(Title title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hideTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAchievement(Achievement achievement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getPlayerTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getPlayerTimeOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetPlayerTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WeatherType getPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetPlayerWeather() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveExp(int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void giveExpLevels(int amount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getExp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExp(float exp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLevel(int level) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTotalExperience() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTotalExperience(int exp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getExhaustion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExhaustion(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSaturation(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFoodLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFoodLevel(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOnline() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBanned() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBanned(boolean banned) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWhitelisted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getPlayer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getFirstPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastPlayed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPlayedBefore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAllowFlight(boolean flight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hidePlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showPlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canSee(Player player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getLocation(Location loc) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVelocity(Vector velocity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector getVelocity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOnGround() {
        throw new UnsupportedOperationException();
    }

    @Override
    public World getWorld() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(Entity destination) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getEntityId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFireTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxFireTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFireTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(String[] messages) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Server getServer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getPassenger() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean eject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFallDistance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFallDistance(float distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UUID getUniqueId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getTicksLived() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTicksLived(int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void playEffect(EntityEffect type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isInsideVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean leaveVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getVehicle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCustomNameVisible() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFlying() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlying(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFlySpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getWalkSpeed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTexturePack(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setResourcePack(String url) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Scoreboard getScoreboard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHealthScaled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHealthScaled(boolean scale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getHealthScale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getSpectatorTarget() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void resetTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spigot spigot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory getEnderChest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InventoryView getOpenInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void openInventory(InventoryView inventory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        inventory.setItemInHand(item);
    }

    @Override
    public ItemStack getItemOnCursor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSleeping() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSleepTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GameMode getGameMode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGameMode(GameMode mode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBlocking() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getExpToLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getEyeHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Location getEyeLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Egg throwEgg() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Snowball throwSnowball() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Arrow shootArrow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRemainingAir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemainingAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaximumAir() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaximumAir(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaximumNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getLastDamage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int _INVALID_getLastDamage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastDamage(double damage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _INVALID_setLastDamage(int damage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNoDamageTicks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Player getKiller() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityEquipment getEquipment() {
        return entityEquipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCanPickupItems() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLeashed() {
        return false;
    }

    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    @Override
    public boolean setLeashHolder(Entity holder) {
        return false;
    }

    @Override
    public void damage(double amount) {

    }

    @Override
    public void _INVALID_damage(int amount) {

    }

    @Override
    public void damage(double amount, Entity source) {

    }

    @Override
    public void _INVALID_damage(int amount, Entity source) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public int _INVALID_getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public void _INVALID_setHealth(int health) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public int _INVALID_getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double health) {

    }

    @Override
    public void _INVALID_setMaxHealth(int health) {

    }

    @Override
    public void resetMaxHealth() {

    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {

    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {

    }

    @Override
    public boolean isPermissionSet(String name) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    @Override
    public boolean hasPermission(String name) {
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return false;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {

    }

    @Override
    public void recalculatePermissions() {

    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {

    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {

    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        return null;
    }
}
