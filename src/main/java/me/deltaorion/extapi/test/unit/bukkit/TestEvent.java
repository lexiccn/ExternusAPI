package me.deltaorion.extapi.test.unit.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TestEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final String message;

    public TestEvent(Player player, String message) {
        super(player);
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
