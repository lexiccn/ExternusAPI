package me.deltaorion.extapi.item.position;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericInventoryItem implements InventoryItem {

    @NotNull private final Inventory inventory;
    @Nullable private ItemStack itemStack;
    private final int slot;

    public GenericInventoryItem(@NotNull Inventory inventory, @Nullable ItemStack itemStack, int slot) {
        this.inventory = inventory;
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        this.inventory.setItem(slot,itemStack);
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public int getRawSlot() {
        return slot;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.OTHER;
    }

}
