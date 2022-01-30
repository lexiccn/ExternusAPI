package me.deltaorion.extapi.item.custom;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;

public interface ItemEventConsumer<T extends Event> {

    public void onEvent(CustomItem item, CustomItemEvent<T> event) throws EventException;
}
