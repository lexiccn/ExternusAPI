package me.deltaorion.bukkit.item.inventory;

import me.deltaorion.bukkit.item.position.HumanEntityItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.SlotType;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class HumanEntityWrapper implements InventoryWrapper {

    @NotNull protected final HumanEntity human;
    private final int BASE_SIZE = 36;

    protected HumanEntityWrapper(@NotNull HumanEntity human) {
        this.human = Objects.requireNonNull(human);
    }

    @NotNull
    @Override
    public InventoryItem[] getArmor() {
        InventoryItem[] armor = new InventoryItem[4];
        armor[0] = getBoots();
        armor[1] = getLeggings();
        armor[2] = getChestplate();
        armor[3] = getHelmet();
        return armor;
    }

    private InventoryItem getHelmet() {
        return new HumanEntityItem(human,SlotType.HELMET.getBukkitSlot());
    }

    private InventoryItem getChestplate() {
        return new HumanEntityItem(human,SlotType.CHESTPLATE.getBukkitSlot());
    }

    private InventoryItem getLeggings() {
        return new HumanEntityItem(human,SlotType.LEGGINGS.getBukkitSlot());
    }

    private InventoryItem getBoots() {
        return new HumanEntityItem(human,SlotType.BOOTS.getBukkitSlot());
    }


    protected InventoryItem[] getBaseInventory() {
        InventoryItem[] items = new InventoryItem[BASE_SIZE];
        for(int i=0;i<BASE_SIZE;i++) {
            items[i] = new HumanEntityItem(human,i);
        }
        return items;
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
