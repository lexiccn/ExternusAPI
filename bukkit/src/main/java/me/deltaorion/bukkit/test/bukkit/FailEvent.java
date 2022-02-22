package me.deltaorion.bukkit.test.bukkit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FailEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String message;

    public FailEvent(String message) {
        this.message = message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getMessage() {
        return message;
    }
}

