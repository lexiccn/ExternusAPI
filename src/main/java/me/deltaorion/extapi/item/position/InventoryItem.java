package me.deltaorion.extapi.item.position;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * This is an abstract interface that represents an ItemStack inside of an inventory. As there are multiple types of inventory's
 * One cannot simply provide the slot number as this does not apply well to an entity equipment inventory.
 *
 */
public interface InventoryItem {

    /**
     * Sets the ItemStack inside the position it resides in the inventory.
     *
     * For example, suppose there is a dirt block in slot 12. If you use this method with a new stone block, then slot 12 will now
     * have a stone block in it.
     *
     * @param itemStack The new ItemStack to set this inventory position.
     */
    void setItem(@Nullable ItemStack itemStack);

    /**
     * @return the associated ItemStack that resides inside of this slot.
     */
    @Nullable
    ItemStack getItemStack();

    /**
     * Attempts to return the inventory slot this ItemStack lies in. This may or may not be useful depending on what type
     * of inventory that is being used. This may even return a negative if there was no way of representing the slot.
     *
     * @return The slot
     */
    int getRawSlot();

    /**
     * @return what type of slot this is, whether it be the main hand, some kind of armor slot or any other type of slot.
     */
    SlotType getSlotType();

    /**
     * Removes the item that sits in this slot. This usually means setting the slot to null or to air.
     */
    default void removeItem() {
        setItem(null);
    }


}
