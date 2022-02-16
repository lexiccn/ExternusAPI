package me.deltaorion.extapi.item.inventory;

import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.item.position.SlotType;
import org.jetbrains.annotations.NotNull;

public interface InventoryWrapper {

    public InventoryItem getMainHand();

    /**
     * @return The item in the inventory's offhand.
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException If the current version does not support
     * off-hand items.
     */
    @NotNull
    public InventoryItem getOffHand();

    @NotNull
    public InventoryItem[] getHands();

    @NotNull
    public InventoryItem[] getHotBar();

    @NotNull
    public InventoryItem[] getArmor();

    @NotNull
    public InventoryItem[] getInventory();

    /**
     *
     * @param slotType
     * @return
     */
    @NotNull
    public InventoryItem fromSlot(SlotType slotType);

}
