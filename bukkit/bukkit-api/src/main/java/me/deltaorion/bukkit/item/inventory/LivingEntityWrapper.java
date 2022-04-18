package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.LivingEntityItem;
import me.deltaorion.bukkit.item.position.SlotType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class LivingEntityWrapper implements InventoryWrapper {

    @NotNull protected final LivingEntity entity;

    public LivingEntityWrapper(@NotNull LivingEntity entity) {
        this.entity = Objects.requireNonNull(entity);
    }

    @NotNull
    @Override
    public InventoryItem[] getArmor() {
        InventoryItem[] items = new InventoryItem[4];
        items[0] = getBoots();
        items[1] = getLeggings();
        items[2] = getChestplate();
        items[3] = getHelmet();
        return items;
    }

    private InventoryItem getHelmet() {
        return new LivingEntityItem(entity, SlotType.HELMET,entity.getEquipment().getHelmet());
    }

    private InventoryItem getChestplate() {
        return new LivingEntityItem(entity, SlotType.CHESTPLATE, entity.getEquipment().getChestplate());
    }
    private InventoryItem getBoots() {
        return new LivingEntityItem(entity, SlotType.BOOTS,entity.getEquipment().getBoots());
    }

    private InventoryItem getLeggings() {
        return new LivingEntityItem(entity,SlotType.LEGGINGS,entity.getEquipment().getLeggings());
    }

    @NotNull
    @Override
    public InventoryItem[] getInventory() {
        return (InventoryItem[]) ArrayUtils.addAll(getArmor(),getHotBar());
    }

    @NotNull
    @Override
    public InventoryItem fromSlot(SlotType slotType) {
        switch (slotType) {
            case MAIN_HAND:
                return getMainHand();
            case OFF_HAND:
                return getOffHand();
            case CHESTPLATE:
                return getChestplate();
            case LEGGINGS:
                return getLeggings();
            case BOOTS:
                return getBoots();
            case HELMET:
                return getHelmet();
        }
        throw new UnsupportedOperationException("There is no way to insert any item into this slot type '"+slotType+"'");
    }
}
