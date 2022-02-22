package me.deltaorion.bukkit.item.wrapper;

import com.google.gson.reflect.TypeToken;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *  An enum that represents a variety of custom event wrappers. See more at {@link CIEventWrapper}
 */
public enum CustomEventWrapper {

    //If this is selected then the program will automatically find a relevant custom event wrapper. This may not be the correct
    //event wrapper for your ci event.
    ROOT(null),
    //Checks the entity that caused damage to another entity.
    GET_DAMAGED_BY_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    //Checks the entity which was hurt or damaged by another entity
    DAMAGE_ANOTHER_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    //retrieves the PlayerEvent#getPlayer() from the event
    PLAYER_EVENT(TypeToken.get(PlayerEvent.class)),
    //retrieves the EntityEvent#getEntity() from the event
    ENTITY_EVENT(TypeToken.get(EntityEvent.class)),
    //Retrieves whoever interacted with the inventory. This does NOT retrieve which item was clicked.
    //For example suppose this is used in par with the inventoryClickEvent and the predicate is mainhand. Then
    //the program will check if the item is in their mainhand when they do the inventory click.
    INVENTORY_INTERACT(TypeToken.get(InventoryInteractEvent.class)),
    //Checks whether the item that was clicked is the custom item. This will IGNORE the predicate
    INVENTORY_CLICK(TypeToken.get(InventoryClickEvent.class)),
    //detects when an inventory is open
    INVENTORY_OPEN(TypeToken.get(InventoryOpenEvent.class)),
    //detects when an inventory is closed
    INVENTORY_CLOSE(TypeToken.get(InventoryCloseEvent.class)),
    //detects when a block is broken by the player
    BLOCK_BREAK(TypeToken.get(BlockBreakEvent.class)),
    //detects when a block is damaged by the player
    BLOCK_DAMAGE(TypeToken.get(BlockDamageEvent.class)),
    //detects when a block is placed by the player
    BLOCK_PLACE(TypeToken.get(BlockPlaceEvent.class)),
    //detects when something in a sign, such as a line is changed
    SIGN_CHANGE(TypeToken.get(SignChangeEvent.class)),
    //detects when the player ignites a block
    IGNITE_BLOCK(TypeToken.get(BlockIgniteEvent.class)),
    //detects when a custom item is being enchanted, this does not use the event predicate, it only detects that the item in the
    //enchant table which is currently being enchanted is the custom item. This will IGNORE the event predicate
    ENCHANT_ITEM_BEING_ENCHANTED(TypeToken.get(EnchantItemEvent.class)),
    //Called when an ItemStack is inserted in an enchantment table - can be called multiple times, checks if this custom
    //item is the one that is being placed. This will IGNORE the event predicate
    PREPARE_ENCHANT_ITEM_BEING_ENCHANTED(TypeToken.get(PrepareItemEnchantEvent.class)),
    //When an item is enchanted, this detects wheteher the event predicate is met
    ENCHANT_ITEM_PLAYER_INVENTORY(TypeToken.get(EnchantItemEvent.class)),
    //WHen an item is put into an enchant table, this detects whether the predicate is met
    PREPARE_ENCHANT_ITEM_PLAYER_INVENTORY(TypeToken.get(PrepareItemEnchantEvent.class)),
    //detects when this entity is combusted by another entity
    ENTITY_COMBUSTED_BY_ENTITY(TypeToken.get(EntityCombustByEntityEvent.class)),
    //detects when this player is combusted
    ENTITY_COMBUSTS_ENTITY(TypeToken.get(EntityCombustByEntityEvent.class)),
    //detects when this player is targetted by an entity
    TARGETTED_BY_ENTITY(TypeToken.get(EntityTargetEvent.class)),
    //detects when this entity launches a projectile.
    PROJECTILE_LAUNCH(TypeToken.get(ProjectileLaunchEvent.class)),
    ;

    private final TypeToken<? extends Event> typeToken;
    private final static Map<TypeToken<? extends Event>,List<CustomEventWrapper>> byType;

    //convert the enum into a map for extremely quick access.
    static {
        Map<TypeToken<? extends Event>,List<CustomEventWrapper>> temp = new HashMap<>();
        for(CustomEventWrapper wrapper : CustomEventWrapper.values()) {
            if(wrapper==CustomEventWrapper.ROOT)
                continue;

            List<CustomEventWrapper> wrappers;
            if(!temp.containsKey(wrapper.typeToken)) {
                wrappers = new ArrayList<>();
                temp.put(wrapper.typeToken,wrappers);
            } else {
                wrappers = temp.get(wrapper.typeToken);
            }
            wrappers.add(wrapper);
        }
        byType = Collections.unmodifiableMap(temp);
    }

    CustomEventWrapper(TypeToken<? extends Event> typeToken) {
        this.typeToken = typeToken;
    }

    //finds an appropiate CIEventWrapper given the event class
    @Nullable
    public static List<CustomEventWrapper> fromClass(@NotNull Class<? extends Event> clazz) {
        TypeToken<?> typeToken = TypeToken.get(clazz);
        if(byType.containsKey(typeToken)) {
            return byType.get(typeToken);
        } else {
            for(Map.Entry<TypeToken<? extends Event>,List<CustomEventWrapper>> entry : byType.entrySet()) {
                if(entry.getKey().getRawType().isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }

            return null;
        }
    }
}
