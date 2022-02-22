package me.deltaorion.bukkit.item.wrapper;

import me.deltaorion.bukkit.item.position.GenericInventoryItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.NonInventoryItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

//unit testing
public class EventWrappers {

    @NotNull
    public static CIEventWrapper<? extends Event> get(@NotNull Class<? extends Event> clazz, @NotNull CustomEventWrapper wrapper) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(wrapper);

        switch (wrapper) {
            case ROOT:
                return getRoot(clazz);
            case PLAYER_EVENT:
                return PLAYER_EVENT();
            case DAMAGE_ANOTHER_ENTITY:
                return DAMAGE_ANOTHER_ENTITY();
            case GET_DAMAGED_BY_ENTITY:
                return GET_DAMAGED();
            case ENTITY_EVENT:
                return ENTITY_EVENT();
            case INVENTORY_INTERACT:
                return INVENTORY_INTERACT();
            case INVENTORY_OPEN:
                return INVENTORY_OPEN();
            case BLOCK_BREAK:
                return BLOCK_BREAK();
            case BLOCK_DAMAGE:
                return BLOCK_DAMAGE();
            case BLOCK_PLACE:
                return BLOCK_PLACE();
            case SIGN_CHANGE:
                return SIGN_CHANGE();
            case IGNITE_BLOCK:
                return IGNITE_BLOCK();
            case ENCHANT_ITEM_BEING_ENCHANTED:
                return ENCHANT_ITEM_BEING_ENCHANTED();
            case PREPARE_ENCHANT_ITEM_BEING_ENCHANTED:
                return PREPARE_ENCHANT_ITEM_ENCHANTED();
            case INVENTORY_CLOSE:
                return INVENTORY_CLOSE();
            case PREPARE_ENCHANT_ITEM_PLAYER_INVENTORY:
                return PREPARE_ENCHANT_ITEM_PLAYER_INVENTORY();
            case ENCHANT_ITEM_PLAYER_INVENTORY:
                return ENCHANT_ITEM_PLAYER();
            case PROJECTILE_LAUNCH:
                return PROJECTILE_LAUNCH();
            case TARGETTED_BY_ENTITY:
                return TARGETTED_BY_ENTITY();
            case ENTITY_COMBUSTS_ENTITY:
                return ENTITY_COMBUSTS_ENTITY();
            case ENTITY_COMBUSTED_BY_ENTITY:
                return ENTITY_COMBUSTED_BY_ENTITY();
            case INVENTORY_CLICK:
                return INVENTORY_CLICK();
            default:
                throw new UnsupportedOperationException("Unknown Event wrapper for '"+wrapper+"'");
        }
    }

    private static CIEventWrapper<EntityCombustByEntityEvent> ENTITY_COMBUSTED_BY_ENTITY() {
        return new CIEventWrapper<EntityCombustByEntityEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityCombustByEntityEvent event) {
                if(!(event.getEntity() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getEntity();
            }
        };
    }

    private static CIEventWrapper<EntityCombustByEntityEvent> ENTITY_COMBUSTS_ENTITY() {
        return new CIEventWrapper<EntityCombustByEntityEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityCombustByEntityEvent event) {
                if(!(event.getCombuster() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getCombuster();
            }
        };
    }

    private static CIEventWrapper<EntityTargetEvent> TARGETTED_BY_ENTITY() {
        return new CIEventWrapper<EntityTargetEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityTargetEvent event) {
                if(!(event.getTarget() instanceof LivingEntity)) {
                   return null;
                }

                return (LivingEntity) event.getTarget();
            }
        };
    }

    private static CIEventWrapper<ProjectileLaunchEvent> PROJECTILE_LAUNCH() {
        return new CIEventWrapper<ProjectileLaunchEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(ProjectileLaunchEvent event) {
                if(!(event.getEntity().getShooter() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getEntity().getShooter();
            }
        };
    }

    public static CIEventWrapper<PrepareItemEnchantEvent> PREPARE_ENCHANT_ITEM_PLAYER_INVENTORY() {
        return new CIEventWrapper<PrepareItemEnchantEvent>() {
            @Nullable
            @Override
            public Player getEntity(PrepareItemEnchantEvent event) {
                return event.getEnchanter();
            }
        };
    }

    public static CIEventWrapper<EnchantItemEvent> ENCHANT_ITEM_PLAYER() {
        return new CIEventWrapper<EnchantItemEvent>() {
            @Nullable
            @Override
            public Player getEntity(EnchantItemEvent event) {
                return event.getEnchanter();
            }
        };
    }


    public static CIEventWrapper<PrepareItemEnchantEvent> PREPARE_ENCHANT_ITEM_ENCHANTED() {
        return new CIEventWrapper<PrepareItemEnchantEvent>() {
            @Nullable
            @Override
            public Player getEntity(PrepareItemEnchantEvent event) {
                return event.getEnchanter();
            }

            @Nullable @Override
            public InventoryItem getItem(PrepareItemEnchantEvent event) {
                return new NonInventoryItem(event.getItem());
            }
        };
    }

    public static CIEventWrapper<EnchantItemEvent> ENCHANT_ITEM_BEING_ENCHANTED() {
        return new CIEventWrapper<EnchantItemEvent>() {
            @Nullable
            @Override
            public Player getEntity(EnchantItemEvent event) {
                return event.getEnchanter();
            }

            @Nullable
            @Override
            public InventoryItem getItem(EnchantItemEvent event) {
                return new NonInventoryItem(event.getItem());
            }
        };
    }

    private static CIEventWrapper<SignChangeEvent> SIGN_CHANGE() {
        return new CIEventWrapper<SignChangeEvent>() {
            @Nullable
            @Override
            public Player getEntity(SignChangeEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<BlockIgniteEvent> IGNITE_BLOCK() {
        return new CIEventWrapper<BlockIgniteEvent>() {
            @Nullable
            @Override
            public Player getEntity(BlockIgniteEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<InventoryOpenEvent> INVENTORY_OPEN() {
        return new CIEventWrapper<InventoryOpenEvent>() {
            @Nullable
            @Override
            public Player getEntity(InventoryOpenEvent event) {
                return (Player) event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<InventoryCloseEvent> INVENTORY_CLOSE() {
        return new CIEventWrapper<InventoryCloseEvent>() {
            @Nullable
            @Override
            public Player getEntity(InventoryCloseEvent event) {
                return (Player) event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<BlockDamageEvent> BLOCK_DAMAGE() {
        return new CIEventWrapper<BlockDamageEvent>() {
            @Nullable
            @Override
            public Player getEntity(BlockDamageEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<BlockPlaceEvent> BLOCK_PLACE() {
        return new CIEventWrapper<BlockPlaceEvent>() {
            @Nullable
            @Override
            public Player getEntity(BlockPlaceEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<PlayerEvent> PLAYER_EVENT() {
        return new CIEventWrapper<PlayerEvent>() {
            @Nullable
            @Override
            public Player getEntity(PlayerEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<EntityDamageByEntityEvent> DAMAGE_ANOTHER_ENTITY() {
        return new CIEventWrapper<EntityDamageByEntityEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getDamager();
            }
        };
    }

    public static CIEventWrapper<EntityDamageByEntityEvent> GET_DAMAGED() {
        return new CIEventWrapper<EntityDamageByEntityEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityDamageByEntityEvent event) {
                if(!(event.getEntity() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getEntity();
            }
        };
    }

    public static CIEventWrapper<EntityEvent> ENTITY_EVENT() {
        return new CIEventWrapper<EntityEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(EntityEvent event) {
                if(!(event.getEntity() instanceof LivingEntity))
                    return null;

                return (LivingEntity) event.getEntity();
            }
        };
    }

    public static CIEventWrapper<InventoryInteractEvent> INVENTORY_INTERACT() {
        return new CIEventWrapper<InventoryInteractEvent>() {
            @Nullable
            @Override
            public Player getEntity(InventoryInteractEvent event) {
                if(!(event.getWhoClicked() instanceof Player))
                    return null;

                return (Player) event.getWhoClicked();
            }
        };
    }

    public static CIEventWrapper<InventoryClickEvent> INVENTORY_CLICK() {
        return new CIEventWrapper<InventoryClickEvent>() {
            @Nullable
            @Override
            public LivingEntity getEntity(InventoryClickEvent event) {
                if(!(event.getWhoClicked() instanceof Player))
                    return null;

                return (Player) event.getWhoClicked();
            }

            @Nullable
            @Override
            public InventoryItem getItem(InventoryClickEvent event) {
                return new GenericInventoryItem(event.getInventory(),event.getCurrentItem(),event.getSlot());
            }
        };
    }

    public static CIEventWrapper<BlockBreakEvent> BLOCK_BREAK() {
        return new CIEventWrapper<BlockBreakEvent>() {
            @Nullable
            @Override
            public Player getEntity(BlockBreakEvent event) {
                return event.getPlayer();
            }
        };
    }



    @NotNull
    private static CIEventWrapper<? extends Event> getRoot(@NotNull Class<? extends Event> clazz) {
        Objects.requireNonNull(clazz);
        List<CustomEventWrapper> wrapperList = CustomEventWrapper.fromClass(clazz);
        if(wrapperList == null || wrapperList.size()==0)
            throw new UnsupportedOperationException("Cannot find a suitable event wrapper for class '"+clazz.getSimpleName()+"' Please specify a custom event wrapper");

        CustomEventWrapper wrapper = wrapperList.get(0);
        if(wrapper.equals(CustomEventWrapper.ROOT))
            throw new IllegalStateException("Fatal Error, Root is tied to a clazz or type or the entered class was null.");

        return get(clazz,wrapper);
    }
}
