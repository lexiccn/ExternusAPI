package me.deltaorion.extapi.item.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public interface CIEventWrapper<T extends Event> {

    @Nullable
    public Player getPlayer(T event);
}
