package me.deltaorion.extapi.item.position;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NonInventoryItem implements InventoryItem {

    @Nullable private ItemStack itemStack;

    public NonInventoryItem(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public int getRawSlot() {
        return -1;
    }

    @Override
    public SlotType getSlotType() {
        return SlotType.OTHER;
    }
}
