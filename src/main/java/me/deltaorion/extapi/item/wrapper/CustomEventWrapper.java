package me.deltaorion.extapi.item.wrapper;

import com.google.gson.reflect.TypeToken;
import me.deltaorion.extapi.test.unit.bukkit.TestEvent;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum CustomEventWrapper {



    ROOT(null),
    //Called when the player is damaged by another entity
    ENtity_DAMAGED_BY_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    //called when this player damages another entity
    ENTITY_DAMAGES_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    //retrieves the player from any player event
    PLAYER_EVENT(TypeToken.get(PlayerEvent.class)),
    //retrieves the entity from any entity event
    ENTITY_EVENT(TypeToken.get(EntityEvent.class)),
    //retrieves the inventory clicker from any inventory interact
    INVENTORY_INTERACT(TypeToken.get(InventoryInteractEvent.class)),
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
    //enchant table which is currently being enchanted is the custom item
    ENCHANT_ITEM_BEING_ENCHANTED(TypeToken.get(EnchantItemEvent.class)),
    //Called when an ItemStack is inserted in an enchantment table - can be called multiple times, checks if this custom
    //item is the one that is being placed
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
    //detects when this player launches a projectile.
    PROJECTILE_LAUNCH(TypeToken.get(ProjectileLaunchEvent.class)),
    ;

    private final TypeToken<?> typeToken;
    private final static Map<TypeToken<?>,List<CustomEventWrapper>> byType;

    static {
        Map<TypeToken<?>,List<CustomEventWrapper>> temp = new HashMap<>();
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

    CustomEventWrapper(TypeToken<?> typeToken) {
        this.typeToken = typeToken;
    }

    @Nullable
    public static List<CustomEventWrapper> fromClass(@NotNull Class<?> clazz) {
        TypeToken<?> typeToken = TypeToken.get(clazz);
        if(byType.containsKey(typeToken)) {
            return byType.get(typeToken);
        } else {
            for(Map.Entry<TypeToken<?>,List<CustomEventWrapper>> entry : byType.entrySet()) {
                if(entry.getKey().getRawType().isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }

            return null;
        }
    }
}
