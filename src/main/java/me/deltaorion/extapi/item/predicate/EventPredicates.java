package me.deltaorion.extapi.item.predicate;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.InventoryItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

//unit testing
public class EventPredicates {

    public static EventPredicate getPredicate(@NotNull EventCondition condition) {
        Objects.requireNonNull(condition);
        switch (condition) {
            case MAIN_HAND:
                return MAIN_HAND();
            case HOTBAR:
                return HOTBAR();
            case ARMOR:
                return ARMOR();
            case INVENTORY:
                return INVENTORY();
            default:
                throw new UnsupportedOperationException("Unknown Event Predicate '"+condition+"'");
        }
    }

    public static EventPredicate MAIN_HAND() {
        return new EventPredicate() {
            @Override
            public List<InventoryItemStack> conditionMet(CustomItem item, Player player) {
                if(item.isCustomItem(player.getItemInHand())) {
                    return ImmutableList.of(new InventoryItemStack(player.getInventory().getHeldItemSlot(),player.getItemInHand()));
                }
                return Collections.emptyList();
            }
        };
    }

    public static EventPredicate HOTBAR() {
        return new EventPredicate() {
            @Override
            public List<InventoryItemStack> conditionMet(CustomItem item, Player player) {
                List<InventoryItemStack> itemStacks = new ArrayList<>();
                for(int i=0;i<=8;i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if(item.isCustomItem(itemStack)) {
                        itemStacks.add(new InventoryItemStack(i,itemStack));
                    }
                }
                return itemStacks;
            }
        };
    }

    public static EventPredicate INVENTORY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItemStack> conditionMet(CustomItem item, Player player) {
                List<InventoryItemStack> itemStacks = new ArrayList<>();
                for(int i=0;i<player.getInventory().getSize();i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if(item.isCustomItem(itemStack)) {
                        itemStacks.add(new InventoryItemStack(i,itemStack));
                    }
                }
                return itemStacks;
            }
        };
    }

    public static EventPredicate ARMOR() {
        return new EventPredicate() {
            @Override
            public List<InventoryItemStack> conditionMet(CustomItem item, Player player) {
                List<InventoryItemStack> itemStacks = new ArrayList<>();
                for(int i=36;i<=39;i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if(item.isCustomItem(itemStack)) {
                        itemStacks.add(new InventoryItemStack(i,itemStack));
                    }
                }
                return itemStacks;
            }
        };
    }

}
