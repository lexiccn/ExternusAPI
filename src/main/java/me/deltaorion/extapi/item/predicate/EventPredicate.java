package me.deltaorion.extapi.item.predicate;

import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.InventoryItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public interface EventPredicate {

    public List<InventoryItemStack> conditionMet(CustomItem item, Player player);

    default boolean conditionMet(List<InventoryItemStack> itemStacks) {
        return itemStacks.size() > 0;
    }
}
