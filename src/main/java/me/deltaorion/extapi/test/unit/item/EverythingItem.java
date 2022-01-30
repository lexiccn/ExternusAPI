package me.deltaorion.extapi.test.unit.item;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.predicate.EventCondition;
import me.deltaorion.extapi.item.wrapper.CustomEventWrapper;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class EverythingItem extends CustomItem {

    private final BukkitPlugin plugin;

    public EverythingItem(BukkitPlugin plugin) {
        super("Everything_Item", new ItemBuilder(EMaterial.NETHER_STAR)
                                            .addHiddenEnchant().build());
        this.plugin = plugin;
        setDefaultDisplayName(Message.valueOfTranslatable("hello"));
        setDefaultLore(ImmutableList.of(Message.valueOfTranslatable("hello")));
    }

    //3 root test cases, 1 testing generic
    @ItemEventHandler(playerOnly = false)
    public void onDeath(CustomItemEvent<EntityDeathEvent> event) {
        plugin.getServer().broadcastMessage("Entity Died!");
    }

    //specific case
    @ItemEventHandler(playerOnly = false)
    public void onDamage(CustomItemEvent<EntityDamageByEntityEvent> event) {
        plugin.getServer().broadcastMessage("Entity Was Damaged");
    }

    //specific case with an item, should ignore sub
    @ItemEventHandler
    public void inventoryClick(CustomItemEvent<InventoryClickEvent> event) {
        plugin.getServer().broadcastMessage("This Item Was Clicked!");
    }

    @ItemEventHandler(playerOnly = false)
    public void onPlayerDeath(CustomItemEvent<PlayerDeathEvent> event) {
        plugin.getServer().broadcastMessage("Player Died!");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.DAMAGE_ANOTHER_ENTITY,playerOnly = false)
    public void whenDamage(CustomItemEvent<EntityDamageByEntityEvent> event) {
        plugin.getServer().broadcastMessage("Entity damaged another entity");
    }

    @ItemEventHandler(condition = EventCondition.INVENTORY)
    public void onDrag(CustomItemEvent<InventoryDragEvent> event) {
        for(ItemStack itemStack : event.getEvent().getNewItems().values()) {
            if(isCustomItem(itemStack)) {
                plugin.getServer().broadcastMessage("This item was included in an inventory drag!");
                return;
            }
        }
    }

    @ItemEventHandler(condition = EventCondition.INVENTORY)
    public void onOpen(CustomItemEvent<InventoryOpenEvent> event) {
        Bukkit.broadcastMessage("Opened inventory while the custom item was inside!");
    }

    @ItemEventHandler(condition = EventCondition.INVENTORY)
    public void onClose(CustomItemEvent<InventoryCloseEvent> event) {
        Bukkit.broadcastMessage("Closed inventory while custom item was inside!");
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent event) {
        if(isCustomItem(event.getEntity().getItemStack())) {
            Bukkit.broadcastMessage("Custom Item Despawned!");
        }
    }

    @ItemEventHandler
    public void onBlockBreak(CustomItemEvent<BlockBreakEvent> event) {
        Bukkit.broadcastMessage("Breaked Block with custom item");
    }

    @ItemEventHandler
    public void onBlockDamage(CustomItemEvent<BlockDamageEvent> event) {
        Bukkit.broadcastMessage("Damaged Block with custom item");
    }

    @ItemEventHandler(condition = EventCondition.HOTBAR)
    public void onBlockPlace(CustomItemEvent<BlockPlaceEvent> event) {
        Bukkit.broadcastMessage("Placed Block with custom item in hotbar");
    }

    @ItemEventHandler
    public void onSignChange(CustomItemEvent<SignChangeEvent> event) {
        Bukkit.broadcastMessage("Changed Sign with Custom Item");
    }

    @ItemEventHandler(condition = EventCondition.HOTBAR)
    public void onBlockIgnite(CustomItemEvent<BlockIgniteEvent> event) {
        Bukkit.broadcastMessage("Ignited a block with the custom item in hotbar");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.ENCHANT_ITEM_BEING_ENCHANTED)
    public void onEnchant(CustomItemEvent<EnchantItemEvent> event) {
        Bukkit.broadcastMessage("The custom item was just enchanted");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.PREPARE_ENCHANT_ITEM_BEING_ENCHANTED)
    public void onEnchantPrepare(CustomItemEvent<PrepareItemEnchantEvent> event) {
        Bukkit.broadcastMessage("The custom item is being prepared to be enchanted!");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.ENCHANT_ITEM_PLAYER_INVENTORY, condition = EventCondition.INVENTORY)
    public void onEnchantInv(CustomItemEvent<EnchantItemEvent> event) {
        Bukkit.broadcastMessage("An item was enchanted while this custom item is in the inventory");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.PREPARE_ENCHANT_ITEM_PLAYER_INVENTORY, condition = EventCondition.INVENTORY)
    public void onPrepareEnchantInv(CustomItemEvent<PrepareItemEnchantEvent> event) {
        Bukkit.broadcastMessage("An item was being prepared to be enchanted while this custom item is in the inventory");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.PROJECTILE_LAUNCH, condition = EventCondition.HOTBAR,playerOnly = false)
    public void onLaunch(CustomItemEvent<ProjectileLaunchEvent> event) {
        Bukkit.broadcastMessage("Launched a projectile while the item was in the hotbar");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.ENTITY_COMBUSTED_BY_ENTITY, condition = EventCondition.HOTBAR, playerOnly = false)
    public void onCombust(CustomItemEvent<EntityCombustByEntityEvent> event) {
        Bukkit.broadcastMessage("I just got lit on fire!");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.ENTITY_COMBUSTS_ENTITY, condition = EventCondition.HOTBAR, playerOnly = false)
    public void onCombusting(CustomItemEvent<EntityCombustByEntityEvent> event) {
        Bukkit.broadcastMessage("I just combusted an enemy");
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.TARGETTED_BY_ENTITY, playerOnly = false)
    public void onTarget(CustomItemEvent<EntityTargetEvent> event) {
        Bukkit.broadcastMessage("I got targetted by an enemy");
    }








}
