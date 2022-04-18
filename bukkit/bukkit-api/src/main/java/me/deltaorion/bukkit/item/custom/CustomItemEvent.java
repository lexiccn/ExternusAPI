package me.deltaorion.bukkit.item.custom;

import com.google.common.base.MoreObjects;
import me.deltaorion.bukkit.item.wrapper.CIEventWrapper;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.predicate.EventPredicate;
import net.jcip.annotations.Immutable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class is a data structure that represents a fired custom item event. This is not a class that listens to an event but is rather
 * a container for information relating to the fired event. This contains information such as
 *   - the extracted entity from the event as defined by the listeners {@link CIEventWrapper}
 *   - All custom items that met the predicate
 *   - The actual event that was listened to
 *   - The custom item that this is related to
 *
 *  If multiple items meet the specified {@link EventPredicate} and they are all custom,
 *  then all of them will be listed here. The event will NOT be fired multiple times for each of the items.
 *
 *  The {@link CustomItemEventListener} Listens to the specified event T and does additional checks based on the specified predicate
 *  and CIEventWrapper. When all those checks are correct then this event will be fired.
 *
 * @param <T> The event type that the event listener should listen to.
 */

@Immutable
public class CustomItemEvent<T extends Event> {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final CustomItem item;
    @NotNull private final T event;
    @NotNull private final List<InventoryItem> itemStacks;
    @NotNull private final LivingEntity entity;

    public CustomItemEvent(@NotNull BukkitPlugin plugin,@NotNull CustomItem item, @NotNull T event, @NotNull LivingEntity entity, @NotNull List<InventoryItem> itemStacks) {
        this.item = Objects.requireNonNull(item);
        this.event = Objects.requireNonNull(event);
        this.itemStacks = Objects.requireNonNull(Collections.unmodifiableList(itemStacks));
        this.entity = Objects.requireNonNull(entity);
        this.plugin = Objects.requireNonNull(plugin);
    }

    @NotNull
    public CustomItem getCustomItem() {
        return item;
    }

    /**
     *
     * @return Returns the original event that was listened to
     */
    @NotNull
    public T getEvent() {
        return event;
    }

    /**
     *
     * @return All ItemStacks in the entities inventory in that meet the listeners {@link EventPredicate}
     * and that are custom. Use the InventoryItem for manipulating the item in their inventory.
     */
    @NotNull
    public List<InventoryItem> getItemStacks() {
        return itemStacks;
    }

    @NotNull
    public LivingEntity getEntity() {
        return entity;
    }

    @NotNull
    public BukkitPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Item",this.item)
                .add("Event",this.event)
                .add("Entity",this.entity)
                .add("Retrieved Items",this.itemStacks)
                .toString();
    }
}
