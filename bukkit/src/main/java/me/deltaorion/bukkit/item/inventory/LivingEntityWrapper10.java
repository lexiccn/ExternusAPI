package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.LivingEntityItem;
import me.deltaorion.bukkit.item.position.SlotType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class LivingEntityWrapper10 extends LivingEntityWrapper{
    public LivingEntityWrapper10(@NotNull LivingEntity entity) {
        super(entity);
    }

    @Override
    public InventoryItem getMainHand() {
        return new LivingEntityItem(entity,SlotType.MAIN_HAND,entity.getEquipment().getItemInMainHand());
    }

    @NotNull
    @Override
    public InventoryItem getOffHand() {
        return new LivingEntityItem(entity,SlotType.OFF_HAND,entity.getEquipment().getItemInOffHand());
    }

    @NotNull
    @Override
    public InventoryItem[] getHands() {
        return new InventoryItem[]{getMainHand(),getOffHand()};
    }

    @NotNull
    @Override
    public InventoryItem[] getHotBar() {
        InventoryItem[] hotBar = new InventoryItem[2];
        hotBar[0] = getMainHand();
        hotBar[1] = getOffHand();
        return hotBar;
    }
}
