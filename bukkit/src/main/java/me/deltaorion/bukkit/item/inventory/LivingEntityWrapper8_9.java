package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.LivingEntityItem;
import me.deltaorion.bukkit.item.position.SlotType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class LivingEntityWrapper8_9 extends LivingEntityWrapper {

    public LivingEntityWrapper8_9(@NotNull LivingEntity entity) {
        super(entity);
    }

    @Override
    public InventoryItem getMainHand() {
        return new LivingEntityItem(entity,SlotType.MAIN_HAND,entity.getEquipment().getItemInHand());
    }

    @NotNull
    @Override
    public InventoryItem getOffHand() {
        throw new UnsupportedVersionException("Cannot get off-hand on this version!");
    }

    @NotNull
    @Override
    public InventoryItem[] getHands() {
        return new InventoryItem[]{getMainHand()};
    }

    @NotNull
    @Override
    public InventoryItem[] getHotBar() {
        return new InventoryItem[]{new LivingEntityItem(entity,SlotType.MAIN_HAND,entity.getEquipment().getItemInHand())};
    }


}
