package me.deltaorion.extapi.item.custom;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.position.InventoryItem;
import net.jcip.annotations.Immutable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @NotNull
    public T getEvent() {
        return event;
    }

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
}
