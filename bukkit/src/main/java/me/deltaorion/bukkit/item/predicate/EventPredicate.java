package me.deltaorion.bukkit.item.predicate;

import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An event predicate represents the condition that a custom item needs to be in for it to activate. An event predicate
 * should analyse the entities context and check that the custom item meets said context. In most cases this means checking that
 * the item lays in some specific situation in the inventory, such as if it is in the entities main hand.
 */
public interface EventPredicate {

    /**
     * Checks whether the entity meets the given predicate.
     *   - If it does meet the predicate, list ALL ItemStacks that meet the condition, not just the first one found.
     *   - If it does NOT meet the predicate, return an empty list.
     *
     * @param item The custom item to check with
     * @param entity the entity to check.
     * @return whether the entity met the predicate
     */
    public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity);


    default boolean conditionMet(@NotNull List<InventoryItem> itemStacks) {
        return itemStacks.size() > 0;
    }
}
