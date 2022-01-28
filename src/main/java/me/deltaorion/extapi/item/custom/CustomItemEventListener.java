package me.deltaorion.extapi.item.custom;

import com.google.common.collect.ImmutableList;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

//unit testing
public class CustomItemEventListener<T extends Event> {

    @NotNull private final EventPredicate predicate;
    @NotNull private final CIEventWrapper<T> eventWrapper;
    @NotNull private final CustomItem customItem;
    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Method method;
    private final boolean playerOnly;

    private CustomItemEventListener(@NotNull EventPredicate predicate, @NotNull CIEventWrapper<T> eventWrapper, @NotNull CustomItem customItem, @NotNull BukkitPlugin plugin, @NotNull Method method, boolean playerOnly) {
        this.predicate = Objects.requireNonNull(predicate);
        this.eventWrapper = eventWrapper;
        this.customItem = customItem;
        this.plugin = plugin;
        this.method = method;
        this.playerOnly = playerOnly;
    }

    //custom item is the underlying listener object, call this over and over, loop through all methods
    public static <T extends Event> void register(@NotNull BukkitPlugin plugin, @NotNull Class<T> clazz, final @NotNull CustomItem customItem, @NotNull EventPriority priority, boolean ignoreCancelled,
                                                  @NotNull EventPredicate predicate, @Nullable CIEventWrapper<T> eventWrapper, @NotNull Method method, boolean playerOnly) {
        if(eventWrapper == null)
            throw new IllegalArgumentException("Cannot resolve event wrapper for class " + clazz.getSimpleName() + ". This can be resolved by making or specifying custom CIEventWrapper for it");

        final CustomItemEventListener<T> customItemEventListener = new CustomItemEventListener<>(predicate,eventWrapper,customItem,plugin,method,playerOnly);

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
            method.invoke(customItem,itemEvent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            try {
                throw new EventException(e.getCause());
            } catch (EventException eventException) {
                eventException.printStackTrace();
            }
        }
    }

}
