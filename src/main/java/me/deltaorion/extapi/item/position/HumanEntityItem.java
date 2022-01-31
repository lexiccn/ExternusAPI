package me.deltaorion.extapi.item.position;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an ItemStack residing in a Human Entity, such as an NPC or player's inventory.
 */
public class HumanEntityItem implements InventoryItem {

    private final HumanEntity entity;
    private final int position;
    @Nullable private ItemStack itemStack;

    public HumanEntityItem(HumanEntity entity, int position, @Nullable ItemStack itemStack) {
        this.position = position;
        this.itemStack = itemStack;
        this.entity = entity;
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        this.entity.getInventory().setItem(position,itemStack);
        this.itemStack = itemStack;
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public int getRawSlot() {
        return this.position;
    }

    @Override
    public SlotType getSlotType() {
        if(this.position == entity.getInventory().getHeldItemSlot())
            return SlotType.MAIN_HAND;

        if(this.position==36)
            return SlotType.BOOTS;

        if(this.position==37)
            return SlotType.LEGGINGS;

        if(this.position==38)
            return SlotType.CHESTPLATE;

        if(this.position==39)
            return SlotType.HELMET;

        return SlotType.OTHER;
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Entity",entity)
                .add("Position",position)
                .add("ItemStack",itemStack)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof HumanEntityItem))
            return false;

        HumanEntityItem entityItem = (HumanEntityItem) o;

        return this.position == entityItem.position && Objects.equals(entityItem.itemStack,this.itemStack)
                && this.entity.equals(entityItem.entity);
    }
}
