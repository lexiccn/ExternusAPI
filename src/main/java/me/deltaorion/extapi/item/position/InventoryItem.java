package me.deltaorion.extapi.item.position;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an ItemStack that is inside of an inventory. The inventory position is which slot it is located within
 * the inventory and the ItemStack is the ItemStack value;
 */

public interface InventoryItem {

    /**
     * Sets the item in the inventory to the new itemstack.
     */
    public void setItem(@Nullable ItemStack itemStack);

    @Nullable
    public ItemStack getItemStack();

    public int getRawSlot();

    public SlotType getSlotType();

    default void removeItem() {
        setItem(null);
    }


}
