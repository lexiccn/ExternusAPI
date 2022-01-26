package me.deltaorion.extapi.item.custom;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.InventoryItemStack;
import me.deltaorion.extapi.item.predicate.EventPredicate;
import me.deltaorion.extapi.item.wrapper.CIEventWrapper;
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

//unit testing
public class CustomItemEventListener<T extends Event> {

    @NotNull private final EventPredicate predicate;
    @NotNull private final CIEventWrapper<T> eventWrapper;
    @NotNull private final CustomItem customItem;
    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Method method;

    private CustomItemEventListener(@NotNull EventPredicate predicate, @NotNull CIEventWrapper<T> eventWrapper, @NotNull CustomItem customItem, @NotNull BukkitPlugin plugin, @NotNull Method method) {
        this.predicate = Objects.requireNonNull(predicate);
        this.eventWrapper = eventWrapper;
        this.customItem = customItem;
        this.plugin = plugin;
        this.method = method;
    }

    //custom item is the underlying listener object, call this over and over, loop through all methods
    public static <T extends Event> void register(@NotNull BukkitPlugin plugin, @NotNull Class<T> clazz, final @NotNull CustomItem customItem, @NotNull EventPriority priority, boolean ignoreCancelled,
                                                  @NotNull EventPredicate predicate, @Nullable CIEventWrapper<T> eventWrapper, @NotNull Method method) {
        if(eventWrapper == null)
            throw new IllegalArgumentException("Cannot resolve event wrapper for class " + clazz.getSimpleName() + ". This can be resolved by making or specifying custom CIEventWrapper for it");

        final CustomItemEventListener<T> customItemEventListener = new CustomItemEventListener<>(predicate,eventWrapper,customItem,plugin,method);

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

        Player player = eventWrapper.getPlayer(event);
        if(player==null)
            return;

        List<InventoryItemStack> itemStackList = predicate.conditionMet(customItem,player);
        if(!predicate.conditionMet(itemStackList))
            return;

        CustomItemEvent<T> itemEvent = new CustomItemEvent<>(plugin,customItem,event,player,itemStackList);
        try {
            method.invoke(customItem,itemEvent);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
