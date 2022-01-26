package me.deltaorion.extapi.item.custom;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.InventoryItemStack;
import net.jcip.annotations.Immutable;
import org.bukkit.entity.Player;
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
    @NotNull private final List<InventoryItemStack> itemStacks;
    @NotNull private final Player player;

    public CustomItemEvent(@NotNull BukkitPlugin plugin,@NotNull CustomItem item, @NotNull T event, @NotNull Player player, @NotNull List<InventoryItemStack> itemStacks) {
        this.item = Objects.requireNonNull(item);
        this.event = Objects.requireNonNull(event);
        this.itemStacks = Objects.requireNonNull(Collections.unmodifiableList(itemStacks));
        this.player = Objects.requireNonNull(player);
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
    public List<InventoryItemStack> getItemStacks() {
        return itemStacks;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public BukkitPlugin getPlugin() {
        return this.plugin;
    }
}
