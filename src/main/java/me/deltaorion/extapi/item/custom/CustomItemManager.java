package me.deltaorion.extapi.item.custom;

import com.google.common.base.MoreObjects;
import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.predicate.EventPredicates;
import me.deltaorion.extapi.item.wrapper.CIEventWrapper;
import me.deltaorion.extapi.item.wrapper.EventWrappers;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * The Custom Item Manager maintains a registry of all custom items. This class is used to
 *   - Register custom items. This will take all {@link ItemEventHandler} and register the listeners with bukkit.
 *   - Register regular event listeners in a custom item.
 *   - Maintain an easy way to retrieve registered custom items
 *   - Ensure the same custom item isn't registered multiple times.
 *   - Provide error handling if the registration fails
 */
public class CustomItemManager {

    private final BukkitPlugin plugin;
    private final Map<String, CustomItem> itemRegistry;

    public CustomItemManager(@NotNull BukkitPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.itemRegistry = new HashMap<>();
    }

    /**
     * Registers a custom item. This will register all event listeners and item event listeners. A custom item should
     * only be registered here. You cannot register the same item twice in a row. One should either check if it has been
     * registered or use {@link CustomItemManager#registerIfAbsent(CustomItem)}.
     *
     * For all event listeners to be considered valid the event should be annotated with @{@link ItemEventHandler}. The first
     * parameter of the method should have a {@link CustomItemEvent<Event>} to be considered valid. The item can also
     * register regular bukkit events
     *
     * @param item the custom item to register
     * @throws MissingDependencyException if the NBT API is not present
     * @throws IllegalArgumentException if this custom item has already been registered.
     */

    public void registerItem(@NotNull CustomItem item) {

        if(plugin.getDependency(BukkitAPIDepends.NBTAPI.name()) == null || !Objects.requireNonNull(plugin.getDependency(BukkitAPIDepends.NBTAPI.name())).isActive())
            throw new MissingDependencyException("Cannot register custom item's without the NBT API");

        Objects.requireNonNull(item);
        if (itemRegistry.containsKey(item.getName()))
            throw new IllegalArgumentException("Custom Item has already been registered!");
        //register any normal events in the custom item
        plugin.getServer().getPluginManager().registerEvents(item,plugin);
        try {
            registerItemEvents(item);
        } catch (CustomItemException e) {
            plugin.getLogger().severe(e.getMessage());
            return;
        }

        itemRegistry.put(item.getName(), item);
    }

    /**
     * Registers a custom item loading all of its events and item events. A custom item should only be registered here.
     * This will check if the custom item is present and if it is not present only then will it be registered.
     *
     * @param item The custom item to be registered
     * @throws MissingDependencyException if the NBT API is not present.
     */

    public void registerIfAbsent(@NotNull CustomItem item) {
        Objects.requireNonNull(item);
        if(isRegistered(item)) {
            return;
        }

        registerItem(item);
    }

    public boolean isRegistered(@NotNull CustomItem item) {
        return itemRegistry.containsKey(item.getName());
    }

    public boolean isRegistered(@NotNull String name) {
        Objects.requireNonNull(name);
        return itemRegistry.containsKey(name);
    }

    @Nullable
    public CustomItem getItem(@NotNull String name) {
        return itemRegistry.get(name);
    }

    @NotNull
    public Set<String> getRegistered() {
        return Collections.unmodifiableSet(itemRegistry.keySet());
    }

    private void registerItemEvents(CustomItem item) throws CustomItemException {
        Set<Method> methods;
        //get all events in the custom item
        try {
            Method[] publicMethods = item.getClass().getMethods();
            Method[] privateMethods = item.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, privateMethods);
        } catch (NoClassDefFoundError e) {
            throw new CustomItemException("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + item.getClass() + " because " + e.getMessage() + " does not exist.");
        }


        for (final Method method : methods) {
            final ItemEventHandler eh = method.getAnnotation(ItemEventHandler.class);
            if (eh == null) continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }

            //ensure the first parameter exists and the parameter is a custom item event
            if (method.getParameterTypes().length != 1 || !CustomItemEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                throw new CustomItemException(plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + item.getClass());
            }

            if(method.getGenericParameterTypes().length!=1 || !(method.getGenericParameterTypes()[0] instanceof ParameterizedType))
                throw new CustomItemException(plugin.getDescription().getFullName()+" attempted to register an invalid an Invalid Custom Item Event Handler. "+method.toGenericString()+" in "+item.getClass() + ". It is invalid as the argument is not parameterized");

            ParameterizedType type = (ParameterizedType) method.getGenericParameterTypes()[0];
            Class<? extends Event> eventClass;
            try {
                if(type.getActualTypeArguments().length!=1) {
                    throw new CustomItemException(plugin.getDescription().getFullName()+" attempted to register an invalid an Invalid Custom Item Event Handler. "+method.toGenericString()+" in "+item.getClass() + ". It is invalid as the argument is not parameterized");
                }
                if(!Event.class.isAssignableFrom((Class<?>) type.getActualTypeArguments()[0])) {
                    throw new CustomItemException(plugin.getDescription().getFullName()+" attempted to register an invalid an Invalid Custom Item Event Handler. "+method.toGenericString()+" in "+item.getClass() + ". It is invalid as the type parameter is not an event!");
                }
                eventClass = ((Class<?>) type.getActualTypeArguments()[0]).asSubclass(Event.class);
            } catch (MalformedParameterizedTypeException | TypeNotPresentException e) {
                throw new CustomItemException("Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + item.getClass() + " because " + e.getMessage() + " does not exist.");
            } catch (ClassCastException e) {
                throw new CustomItemException(plugin.getDescription().getFullName()+" attempted to register an invalid an Invalid Custom Item Event Handler. "+method.toGenericString()+" in "+item.getClass() + ". It is invalid as the parameter is not an event");
            }
            method.setAccessible(true);

            register(eventClass,item,eh,method);
        }

        try {
            item.onRegister();
        } catch (Throwable e) {
            throw new CustomItemException("An error occurred when running forRegister() for item '"+item.getName()+"'",e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void register(Class<T> eventClass, CustomItem item, ItemEventHandler eh, Method method) throws CustomItemException {
        try {
            CustomItemEventListener.register(plugin, eventClass, item, EventPredicates.getPredicate(eh.condition()), (CIEventWrapper<T>) EventWrappers.get(eventClass,eh.wrapper()), method, eh.playerOnly(),eh.priority(),eh.ignoreCancelled());
        } catch (ClassCastException e) {
            throw new CustomItemException(plugin.getDescription().getFullName()+" attempted to register an invalid an Invalid Custom Item Event Handler. "+method.toGenericString()+" in "+item.getClass() + ". It is invalid as the event wrapper does not match the event class type");
        } catch (UnsupportedOperationException | IllegalStateException e) {
            throw new CustomItemException("Cannot register event handler for method '"+method.toGenericString()+"' in '"+item.getClass().getSimpleName()+"' because '"+e.getMessage()+"'");
        } catch (IllegalArgumentException e) {
            throw new CustomItemException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Registry",itemRegistry)
                .toString();
    }
}
