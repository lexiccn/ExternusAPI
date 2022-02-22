package me.deltaorion.bukkit.item.custom;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;

/**
 * A CustomItemEventListener will physically listen to the event. Once all the checks have been run then foreign code needs to be
 * run. This abstract interface should define how to run the foreign code, whether it be a {@link java.lang.reflect.Method} extracted
 * from the object or whether it be a consumer defined.
 *
 * @param <T> The event class that is being listened to
 */
public interface ItemEventConsumer<T extends Event> {

    /**
     * Runs the foreign code, and if there is any unchecked thrown exception in the foreign code. Then this should return
     * an EventException.
     *
     * @param item The custom that this event is related to
     * @param event The item event that was fired.
     * @throws EventException If the foreign code throws any kind of unchecked exception.
     */
    public void onEvent(CustomItem item, CustomItemEvent<T> event) throws EventException;
}
