package me.deltaorion.bukkit.item.custom;

import com.google.common.collect.ImmutableList;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.bukkit.item.predicate.EventPredicate;
import me.deltaorion.bukkit.item.wrapper.CIEventWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A Custom Item Event Listener is a wrapper for an {@link Event}. This class
 *   - listens to a bukkit event
 *   - when the bukkit event is fired, this will check that the extracted entity from the {@link CIEventWrapper} meets
 *     the specified {@link EventPredicate}
 *   - registers the listener for the event
 *
 * @param <T> The event class to listen to
 */
public class CustomItemEventListener<T extends Event> {

    @NotNull private final EventPredicate predicate;
    @NotNull private final CIEventWrapper<T> eventWrapper;
    @NotNull private final CustomItem customItem;
    @NotNull private final BukkitPlugin plugin;
    @NotNull private final ItemEventConsumer<T> consumer;
    @Nullable private EventExecutor executor;
    @NotNull private final Class<T> clazz;
    private final boolean playerOnly;

    private CustomItemEventListener(@NotNull EventPredicate predicate, @NotNull CIEventWrapper<T> eventWrapper, @NotNull CustomItem customItem, @NotNull BukkitPlugin plugin, @NotNull ItemEventConsumer<T> consumer, boolean playerOnly, @NotNull Class<T> clazz) {
        this.predicate = Objects.requireNonNull(predicate);
        this.eventWrapper = Objects.requireNonNull(eventWrapper);
        this.customItem = Objects.requireNonNull(customItem);
        this.plugin = Objects.requireNonNull(plugin);
        this.consumer = Objects.requireNonNull(consumer);
        this.playerOnly = playerOnly;
        this.clazz = clazz;
    }

    private void setExecutor(@NotNull EventExecutor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    //Registers a custom event from an extracted method from an object.
    public static <T extends Event> void register(@NotNull BukkitPlugin plugin, @NotNull Class<T> clazz, final @NotNull CustomItem customItem,
                                                  @NotNull EventPredicate predicate, @Nullable CIEventWrapper<T> eventWrapper, @NotNull Method method,
                                                  boolean playerOnly, @NotNull EventPriority priority, boolean ignoreCancelled) {
        if(eventWrapper == null)
            throw new IllegalArgumentException("Cannot resolve event wrapper for class " + clazz.getSimpleName() + ". This can be resolved by making or specifying custom CIEventWrapper for it");

        ItemEventConsumer<T> consumer = (item, event) -> {
            try {
                method.invoke(item,event);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new EventException(e.getCause());
            }
        };

        final CustomItemEventListener<T> customItemEventListener = new CustomItemEventListener<>(predicate,eventWrapper,customItem,plugin,consumer,playerOnly,clazz);
        applyExecutor(customItemEventListener,clazz,plugin,method);
        customItemEventListener.register(priority,ignoreCancelled);
    }

    public static <T extends Event> void register(@NotNull BukkitPlugin plugin, @NotNull Class<T> clazz, final @NotNull CustomItem customItem,
                                                                   @NotNull EventPredicate predicate, @Nullable CIEventWrapper<T> eventWrapper, @NotNull Consumer<CustomItemEvent<T>> method,
                                                                   boolean playerOnly, @NotNull EventPriority priority, boolean ignoreCancelled) {
        if(eventWrapper == null)
            throw new IllegalArgumentException("Cannot resolve event wrapper for class " + clazz.getSimpleName() + ". This can be resolved by making or specifying custom CIEventWrapper for it");

        ItemEventConsumer<T> consumer = (item, event) -> {
            try {
                method.accept(event);
            } catch (Throwable e) {
                throw new EventException(e);
            }
        };

        final CustomItemEventListener<T> customItemEventListener = new CustomItemEventListener<>(predicate,eventWrapper,customItem,plugin,consumer,playerOnly,clazz);
        applyExecutor(customItemEventListener,clazz,plugin,null);
        customItemEventListener.register(priority,ignoreCancelled);
    }

    private static <T extends Event> void applyExecutor(@NotNull CustomItemEventListener<T> customItemEventListener,@NotNull Class<T> clazz,@NotNull BukkitPlugin plugin, @Nullable Method method) {
        EventExecutor executor = new co.aikar.timings.TimedEventExecutor((listener, event) -> {
            try {
                if (!clazz.isAssignableFrom(event.getClass())) {
                    return;
                }
                customItemEventListener.onEvent(clazz.cast(event));
            } catch (Throwable t) {
                throw new EventException(t);
            }
        }, plugin, method, clazz);

        customItemEventListener.setExecutor(executor);
    }

    private void register(@NotNull EventPriority priority, boolean ignoreCancelled) {
        plugin.getServer().getPluginManager().registerEvent(clazz,customItem,priority,executor,plugin,ignoreCancelled);
    }

    private void onEvent(T event) {

        InventoryItem itemStack = eventWrapper.getItem(event);
        LivingEntity entity = eventWrapper.getEntity(event);

        if(entity==null)
            return;

        if(playerOnly && !(entity instanceof Player))
            return;

        CustomItemEvent<T> itemEvent;
        //Check whether the event is in ItemStack or Entity Mode. If in ItemStack mode then the event wrapper
        //is looking for a specific item from the event rather than the entity.
        if(itemStack!=null) {
            //specific event item. This is used for the
            if(!customItem.isCustomItem(itemStack.getItemStack()))
                return;

            itemEvent = new CustomItemEvent<>(plugin,customItem,event,entity,ImmutableList.of(itemStack));
        } else {
            //no specific event item check inventory
            List<InventoryItem> itemStackList = predicate.conditionMet(customItem,entity);
            if(!predicate.conditionMet(itemStackList))
                return;

            itemEvent = new CustomItemEvent<>(plugin,customItem,event,entity,itemStackList);
        }

        try {
            //we are running foreign code here so we don't know what errors this may cause
            consumer.onEvent(customItem,itemEvent);
        } catch (EventException e) {
            if(itemEvent.getEntity() instanceof Player) {
                plugin.getEServer().wrapSender(itemEvent.getEntity()).sendMessage(MessageErrors.INTERNAL_ERROR_ITEM().toString(customItem.getName()));
            }
            plugin.getPluginLogger().severe("An error occurred while trying to execute an event",e);
        }
    }

}
