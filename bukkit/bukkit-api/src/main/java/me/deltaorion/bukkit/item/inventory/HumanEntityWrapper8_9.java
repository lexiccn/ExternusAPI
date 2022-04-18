package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.item.position.HumanEntityItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class HumanEntityWrapper8_9 extends HumanEntityWrapper {

    protected HumanEntityWrapper8_9(@NotNull HumanEntity human) {
        super(human);
    }

    private final int HOTBAR_END = 9;

    @Override
    public InventoryItem getMainHand() {
        return new HumanEntityItem(human,human.getInventory().getHeldItemSlot());
    }

    @NotNull
    @Override
    public InventoryItem getOffHand() {
        throw new UnsupportedVersionException("Off-hand is not supported in this version!");
    }

    @NotNull
    @Override
    public InventoryItem[] getHands() {
        return new InventoryItem[]{getMainHand()};
    }

    @NotNull
    @Override
    public InventoryItem[] getHotBar() {
        InventoryItem[] hotBar = new InventoryItem[HOTBAR_END];
        for(int i=0;i<HOTBAR_END;i++) {
            hotBar[i] = new HumanEntityItem(human,i);
        }
        return hotBar;
    }

    @NotNull
    @Override
    public InventoryItem[] getInventory() {
        return (InventoryItem[]) ArrayUtils.addAll(getArmor(),getBaseInventory());
    }
}
