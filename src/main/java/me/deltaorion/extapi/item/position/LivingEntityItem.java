package me.deltaorion.extapi.item.position;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LivingEntityItem implements InventoryItem {

    @NotNull private final LivingEntity entity;
    @NotNull private final SlotType type;
    @Nullable private ItemStack itemStack;

    public LivingEntityItem(@NotNull LivingEntity entity, @NotNull SlotType type, @Nullable ItemStack itemStack) {
        this.entity = Objects.requireNonNull(entity);
        this.type = Objects.requireNonNull(type);
        this.itemStack = itemStack;
    }

    @Override
    public void setItem(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        switch (type) {
            case HELMET:
                entity.getEquipment().setHelmet(itemStack);
                return;
            case MAIN_HAND:
                entity.getEquipment().setItemInHand(itemStack);
                return;
            case BOOTS:
                entity.getEquipment().setBoots(itemStack);
                return;
            case LEGGINGS:
                entity.getEquipment().setLeggings(itemStack);
                return;
            case CHESTPLATE:
                entity.getEquipment().setChestplate(itemStack);
        }
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public int getRawSlot() {
        return type.getSlot();
    }

    @Override
    public SlotType getSlotType() {
        return type;
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Entity",entity)
                .add("Slot",this.type)
                .add("ItemStack",this.itemStack)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
       if(!(o instanceof LivingEntityItem))
           return false;

       LivingEntityItem item = (LivingEntityItem) o;
       return item.entity.equals(this.entity) && item.type.equals(this.type) && Objects.equals(this.itemStack,item.itemStack);
    }
}
