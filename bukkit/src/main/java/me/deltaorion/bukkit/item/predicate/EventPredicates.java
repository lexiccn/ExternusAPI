package me.deltaorion.bukkit.item.predicate;

import com.google.common.collect.ImmutableList;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.inventory.InventoryWrapper;
import me.deltaorion.bukkit.item.inventory.InventoryWrappers;
import me.deltaorion.bukkit.item.position.InventoryItem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventPredicates {

    private EventPredicates() {
        throw new UnsupportedOperationException();
    }

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
            case OFF_HAND:
                return OFF_HAND();
            case ANY_HAND:
                return ANY_HAND();
            default:
                throw new UnsupportedOperationException("Unknown Event Predicate '"+condition+"'");
        }
    }

    private final static int INVENTORY_SIZE = 36;
    private final static int INVENTORY_START = 0;
    private final static int HOTBAR_SIZE = 9;

    private final static int ARMOR_START = 36;
    private final static int ARMOR_STOP = 39;


    public static EventPredicate MAIN_HAND() {
        return (item, entity) -> checkFromSingular(item,getWrapper(entity).getMainHand());
    }

    public static EventPredicate OFF_HAND() {
        return (item, entity) -> checkFromSingular(item,getWrapper(entity).getOffHand());
    }

    public static EventPredicate ANY_HAND() {
        return (item, entity) -> checkFromArr(item,getWrapper(entity).getHands());
    }

    public static EventPredicate HOTBAR() {
        return (item, entity) -> checkFromArr(item,getWrapper(entity).getHotBar());
    }

    public static EventPredicate ARMOR() {
        return (item, entity) -> checkFromArr(item,getWrapper(entity).getArmor());
    }

    public static EventPredicate INVENTORY() {
        return (item, entity) -> checkFromArr(item,getWrapper(entity).getInventory());
    }



    private static List<InventoryItem> checkFromArr(@NotNull CustomItem item, @NotNull InventoryItem[] arr) {
        List<InventoryItem> items = new ArrayList<>();
        for(InventoryItem inventoryItem : arr) {
            if(item.isCustomItem(inventoryItem.getItemStack())) {
                items.add(inventoryItem);
            }
        }
        return Collections.unmodifiableList(items);
    }

    private static List<InventoryItem> checkFromSingular(@NotNull CustomItem item, @NotNull InventoryItem inventoryItem) {
        if(item.isCustomItem(inventoryItem.getItemStack())) {
            return ImmutableList.of(inventoryItem);
        } else {
            return Collections.emptyList();
        }
    }

    private static InventoryWrapper getWrapper(LivingEntity entity) {
        return InventoryWrappers.FROM_VERSION(Objects.requireNonNull(EMaterial.getVersion(),"Unknown Version, Did EMaterial initialise properly?"),entity);
    }


}
