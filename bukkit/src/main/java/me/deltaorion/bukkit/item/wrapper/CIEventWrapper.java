package me.deltaorion.bukkit.item.wrapper;

import me.deltaorion.bukkit.item.position.InventoryItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * A simple interface that wraps a bukkit event and returns the player in that event so that it can be checked later using the
 * event predicates. If the event has an item associated with it, that is the item associated with the event should be checked
 * rather than the players inventory then simply override {@link CIEventWrapper#getItem(Event)} and still return the player.
 *
 * @param <T> The event type to wrap
 */
public interface CIEventWrapper<T extends Event> {

    /**
     * Retrieves the entity associated with the event so that its inventory can be checked for the custom item predicate. If a
     * null value is returned then it will be assumed that this is not a custom item event. That is if a null value is returned
     * the associated event will not be called for the custom item. If the retrieved player is not null and the retrieved {@link #getItem(Event)} is not
     * null then the itemstack will be checked
     *
     * @param event The event which to wrap
     * @return The player or null if it does not exist
     */

    @Nullable
    public LivingEntity getEntity(T event);

    /**
     * Retrieves an itemstack associated with the event and this itemstack will be checked if it is the custom item.
     * If the retrieved player is not null and the retrieved {@link #getItem(Event)} is not null then the itemstack will be checked
     * if it is custom instead of the event predicate. If this is null and the retrieved {@link #getEntity(Event)} is not then the event predicate
     * will be checked.
     *
     * @param event The event to retrieve the item from
     * @return The associated itemstack
     */

    @Nullable
    default InventoryItem getItem(T event) {
        return null;
    }
}
