package me.deltaorion.extapi.item.predicate;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.position.HumanEntityItem;
import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.item.position.LivingEntityItem;
import me.deltaorion.extapi.item.position.SlotType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
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
            default:
                throw new UnsupportedOperationException("Unknown Event Predicate '"+condition+"'");
        }
    }

    public static EventPredicate MAIN_HAND() {
        return new EventPredicate() {

            private final EventPredicate mainHandHumanEntity = MAIN_HAND_HUMAN_ENTITY();
            private final EventPredicate mainHandLivingEntity = MAIN_HAND_LIVING_ENTITY();

            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                if(entity instanceof HumanEntity) {
                    return mainHandHumanEntity.conditionMet(item,entity);
                } else {
                    return mainHandLivingEntity.conditionMet(item,entity);
                }
            }
        };
    }

    private static EventPredicate MAIN_HAND_HUMAN_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                HumanEntity humanEntity = (HumanEntity) entity;
                if(item.isCustomItem(humanEntity.getItemInHand())) {
                    return ImmutableList.of(new HumanEntityItem(humanEntity,humanEntity.getInventory().getHeldItemSlot(),humanEntity.getItemInHand()));
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    private static EventPredicate MAIN_HAND_LIVING_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                if(item.isCustomItem(entity.getEquipment().getItemInHand())) {
                    return ImmutableList.of(new LivingEntityItem(entity, SlotType.MAIN_HAND,entity.getEquipment().getItemInHand()));
                } else {
                    return Collections.emptyList();
                }
            }
        };
    }

    public static EventPredicate HOTBAR() {

        return new EventPredicate() {

            private final EventPredicate hotbarHumanEntity = HOTBAR_HUMAN_ENTITY();
            private final EventPredicate hotbarLivingEntity = MAIN_HAND_LIVING_ENTITY();

            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                if(entity instanceof HumanEntity) {
                    return hotbarHumanEntity.conditionMet(item,entity);
                } else {
                    return hotbarLivingEntity.conditionMet(item,entity);
                }
            }
        };
    }

    private static EventPredicate HOTBAR_HUMAN_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                HumanEntity humanEntity = (HumanEntity) entity;
                List<InventoryItem> inventoryItems = new ArrayList<>();
                for(int i=0;i<=8;i++) {
                    ItemStack itemStack = humanEntity.getInventory().getItem(i);
                    if(item.isCustomItem(itemStack)) {
                        inventoryItems.add(new HumanEntityItem(humanEntity,i,itemStack));
                    }
                }
                return Collections.unmodifiableList(inventoryItems);
            }
        };
    }

    public static EventPredicate ARMOR() {
        return new EventPredicate() {

            private final EventPredicate armorLivingEntity = ARMOR_LIVING_ENTITY();
            private final EventPredicate armorHumanEntity = ARMOR_HUMAN_ENTITY();

            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                if(entity instanceof HumanEntity) {
                    return armorHumanEntity.conditionMet(item,entity);
                } else {
                    return armorLivingEntity.conditionMet(item,entity);
                }
            }
        };
    }

    private static EventPredicate ARMOR_LIVING_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {

                List<InventoryItem> itemStacks = new ArrayList<>();

                if(item.isCustomItem(entity.getEquipment().getChestplate()))
                    itemStacks.add(new LivingEntityItem(entity,SlotType.CHESTPLATE,entity.getEquipment().getChestplate()));
                if(item.isCustomItem(entity.getEquipment().getLeggings()))
                    itemStacks.add(new LivingEntityItem(entity,SlotType.LEGGINGS, entity.getEquipment().getLeggings()));
                if(item.isCustomItem(entity.getEquipment().getBoots()))
                    itemStacks.add(new LivingEntityItem(entity,SlotType.BOOTS, entity.getEquipment().getBoots()));
                if(item.isCustomItem(entity.getEquipment().getHelmet()))
                    itemStacks.add(new LivingEntityItem(entity,SlotType.HELMET, entity.getEquipment().getHelmet()));

                return itemStacks;
            }
        };
    }

    private static EventPredicate ARMOR_HUMAN_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                List<InventoryItem> itemStacks = new ArrayList<>();
                HumanEntity humanEntity = (HumanEntity) entity;
                for (int i = 36; i <= 39; i++) {
                    ItemStack itemStack = humanEntity.getInventory().getItem(i);
                    if (item.isCustomItem(itemStack)) {
                        itemStacks.add(new HumanEntityItem(humanEntity,i,itemStack));
                    }
                }
                return itemStacks;
            }
        };
    }

    public static EventPredicate INVENTORY() {


        return new EventPredicate() {

            private final EventPredicate inventoryLivingEntity = INVENTORY_LIVING_ENTITY();
            private final EventPredicate inventoryHumanEntity = INVENTORY_HUMAN_ENTITY();

            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                if(entity instanceof HumanEntity) {
                    return inventoryHumanEntity.conditionMet(item,entity);
                } else {
                    return inventoryLivingEntity.conditionMet(item,entity);
                }
            }
        };
    }

    private static EventPredicate INVENTORY_HUMAN_ENTITY() {
        return new EventPredicate() {
            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                HumanEntity player = (HumanEntity) entity;
                List<InventoryItem> itemStacks = new ArrayList<>();
                for (int i = 0; i < 40; i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (item.isCustomItem(itemStack)) {
                        itemStacks.add(new HumanEntityItem(player,i, itemStack));
                    }
                }
                return Collections.unmodifiableList(itemStacks);
            }
        };
    }

    private static EventPredicate INVENTORY_LIVING_ENTITY() {
        return new EventPredicate() {

            private final EventPredicate armorLivingEntity = ARMOR_LIVING_ENTITY();
            private final EventPredicate mainHandLivingEntity = MAIN_HAND_LIVING_ENTITY();

            @Override
            public List<InventoryItem> conditionMet(@NotNull CustomItem item, @NotNull LivingEntity entity) {
                List<InventoryItem> items = new ArrayList<>();
                items.addAll(armorLivingEntity.conditionMet(item,entity));
                items.addAll(mainHandLivingEntity.conditionMet(item,entity));

                return items;
            }
        };
    }

}
