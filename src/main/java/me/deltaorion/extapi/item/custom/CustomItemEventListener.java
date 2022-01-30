package me.deltaorion.extapi.item.custom;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.command.sent.MessageErrors;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.item.predicate.EventPredicate;
import me.deltaorion.extapi.item.wrapper.CIEventWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

//unit testing
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

    public void setExecutor(@NotNull EventExecutor executor) {
        this.executor = Objects.requireNonNull(executor);
    }

    //custom item is the underlying listener object, call this over and over, loop through all methods
    public static <T extends Event> void register(@NotNull BukkitPlugin plugin, @NotNull Class<T> clazz, final @NotNull CustomItem customItem,
                                                  @NotNull EventPredicate predicate, @Nullable CIEventWrapper<T> eventWrapper, @NotNull Method method,
                                                  boolean playerOnly, @NotNull EventPriority priority, boolean ignoreCancelled) {
        if(eventWrapper == null)
            throw new IllegalArgumentException("Cannot resolve event wrapper for class " + clazz.getSimpleName() + ". This can be resolved by making or specifying custom CIEventWrapper for it");

        ItemEventConsumer<T> consumer = new ItemEventConsumer<T>() {
            @Override
            public void onEvent(CustomItem item, CustomItemEvent<T> event) throws EventException {
                try {
                    method.invoke(item,event);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new EventException(e.getCause());
                }
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

        ItemEventConsumer<T> consumer = new ItemEventConsumer<T>() {
            @Override
            public void onEvent(CustomItem item, CustomItemEvent<T> event) throws EventException {
                try {
                    method.accept(event);
                } catch (Throwable e) {
                    throw new EventException(e);
                }
            }
        };

        final CustomItemEventListener<T> customItemEventListener = new CustomItemEventListener<>(predicate,eventWrapper,customItem,plugin,consumer,playerOnly,clazz);
        applyExecutor(customItemEventListener,clazz,plugin,null);
        customItemEventListener.register(priority,ignoreCancelled);
    }

    private static <T extends Event> CustomItemEventListener<T> applyExecutor(@NotNull CustomItemEventListener<T> customItemEventListener,@NotNull Class<T> clazz,@NotNull BukkitPlugin plugin, @Nullable Method method) {
        EventExecutor executor = new co.aikar.timings.TimedEventExecutor(new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                try {
                    if (!clazz.isAssignableFrom(event.getClass())) {
                        return;
                    }
                    customItemEventListener.onEvent(clazz.cast(event));
                } catch (Throwable t) {
                    throw new EventException(t);
                }
            }
        }, plugin, method, clazz);

        customItemEventListener.setExecutor(executor);
        return customItemEventListener;
    }

    public void register(@NotNull EventPriority priority, boolean ignoreCancelled) {
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
            consumer.onEvent(customItem,itemEvent);
        } catch (EventException e) {
            if(itemEvent.getEntity() instanceof Player) {
                plugin.wrapSender(itemEvent.getEntity()).sendMessage(MessageErrors.INTERNAL_ERROR_ITEM().toString(customItem.getName()));
            }
            plugin.getPluginLogger().severe("An error occurred while trying to execute an event",e);
        }
    }

}
