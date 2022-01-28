package me.deltaorion.extapi.item.predicate;

import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.position.InventoryItem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface EventPredicate {

    public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity);

    default boolean conditionMet(@NotNull List<InventoryItem> itemStacks) {
        return itemStacks.size() > 0;
    }
}
