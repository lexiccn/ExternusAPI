package me.deltaorion.extapi.item;

import net.jcip.annotations.Immutable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents an ItemStack that is inside of an inventory. The inventory position is which slot it is located within
 * the inventory and the ItemStack is the ItemStack value;
 */

@Immutable
public class InventoryItemStack {

    private final int position;
    @NotNull private final ItemStack itemStack;

    public InventoryItemStack(int position, @NotNull ItemStack itemStack) {
        this.position = position;
        this.itemStack = Objects.requireNonNull(itemStack);
    }

    public int getPosition() {
        return position;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }
}
