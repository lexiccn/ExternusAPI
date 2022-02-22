package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.item.position.HumanEntityItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.SlotType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class HumanEntityWrapper10 extends HumanEntityWrapper {

    private final int HOTBAR_END = 9;

    protected HumanEntityWrapper10(@NotNull HumanEntity human) {
        super(human);
    }

    @Override
    public InventoryItem getMainHand() {
        return new HumanEntityItem(human,human.getInventory().getHeldItemSlot());
    }

    @NotNull
    @Override
    public InventoryItem getOffHand() {
        return new HumanEntityItem(human, SlotType.OFF_HAND.getBukkitSlot());
    }

    @NotNull
    @Override
    public InventoryItem[] getHands() {
        return new InventoryItem[]{getMainHand(),getOffHand()};
    }

    @NotNull
    @Override
    public InventoryItem[] getHotBar() {
        InventoryItem[] hotBar = new InventoryItem[HOTBAR_END+1];
        for(int i=0;i<HOTBAR_END;i++) {
            hotBar[i] = new HumanEntityItem(human,i);
        }
        hotBar[HOTBAR_END] = getOffHand();
        return hotBar;
    }

    @NotNull
    @Override
    public InventoryItem[] getInventory() {
        return (InventoryItem[]) ArrayUtils.addAll(ArrayUtils.addAll(getBaseInventory(),getArmor()),new InventoryItem[]{getOffHand()});
    }
}
