package me.deltaorion.bukkit.item.position;

import com.google.common.base.MoreObjects;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof NonInventoryItem))
            return false;

        return Objects.equals(((NonInventoryItem) o).itemStack,this.itemStack);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ItemStack",itemStack)
                .toString();
    }


}
