package me.deltaorion.bukkit.item.position;

import com.google.common.base.MoreObjects;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an ItemStack residing in a Human Entity, such as an NPC or player's inventory.
 */
public class HumanEntityItem implements InventoryItem {

    private final HumanEntity entity;
    private final int position;

    public HumanEntityItem(HumanEntity entity, int position) {
        this.position = position;
        this.entity = entity;
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        this.entity.getInventory().setItem(position,itemStack);
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return entity.getInventory().getItem(position);
    }

    @Override
    public int getRawSlot() {
        return this.position;
    }

    @Override
    public SlotType getSlotType() {
        if(this.position == entity.getInventory().getHeldItemSlot())
            return SlotType.MAIN_HAND;

        for(SlotType slotType : SlotType.values()) {
            if(slotType.getBukkitSlot()==this.position)
                return slotType;
        }

        return SlotType.OTHER;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Entity",entity)
                .add("Position",position)
                .add("ItemStack",getItemStack())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof HumanEntityItem))
            return false;

        HumanEntityItem entityItem = (HumanEntityItem) o;

        return this.position == entityItem.position
                && this.entity.equals(entityItem.entity);
    }
}
