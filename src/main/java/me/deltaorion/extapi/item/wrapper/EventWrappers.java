package me.deltaorion.extapi.item.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

//unit testing
public class EventWrappers {

    @NotNull
    public static CIEventWrapper<? extends Event> get(@NotNull Class<? extends Event> clazz, @NotNull CustomEventWrapper wrapper) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(wrapper);

        switch (wrapper) {
            case ROOT:
                return getRoot(clazz);
            case PLAYER_EVENT:
                return PLAYER_EVENT();
            case PLAYED_DAMAGES_ENTITY:
                return DAMAGE_ANOTHER_ENTITY();
            case PLAYER_DAMAGED_BY_ENTITY:
                return GET_DAMAGED();
            default:
                throw new UnsupportedOperationException("Unknown Event wrapper for '"+wrapper+"'");
        }
    }

    public static CIEventWrapper<PlayerEvent> PLAYER_EVENT() {
        return new CIEventWrapper<PlayerEvent>() {
            @Nullable
            @Override
            public Player getPlayer(PlayerEvent event) {
                return event.getPlayer();
            }
        };
    }

    public static CIEventWrapper<EntityDamageByEntityEvent> DAMAGE_ANOTHER_ENTITY() {
        return new CIEventWrapper<EntityDamageByEntityEvent>() {
            @Nullable
            @Override
            public Player getPlayer(EntityDamageByEntityEvent event) {
                if(!(event.getDamager() instanceof Player))
                    return null;

                return (Player) event.getDamager();
            }
        };
    }

    public static CIEventWrapper<EntityDamageByEntityEvent> GET_DAMAGED() {
        return new CIEventWrapper<EntityDamageByEntityEvent>() {
            @Nullable
            @Override
            public Player getPlayer(EntityDamageByEntityEvent event) {
                if(!(event.getEntity() instanceof Player))
                    return null;

                return (Player) event.getEntity();
            }
        };
    }

    public static CIEventWrapper<EntityEvent> ENTITY_EVENT() {
        return new CIEventWrapper<EntityEvent>() {
            @Nullable
            @Override
            public Player getPlayer(EntityEvent event) {
                if(!(event.getEntity() instanceof Player))
                    return null;

                return (Player) event.getEntity();
            }
        };
    }



    @NotNull
    private static CIEventWrapper<? extends Event> getRoot(@NotNull Class<? extends Event> clazz) {
        List<CustomEventWrapper> wrapperList = CustomEventWrapper.fromClass(clazz);
        if(wrapperList == null || wrapperList.size()==0)
            throw new UnsupportedOperationException("Cannot find a suitable event wrapper for class '"+clazz.getSimpleName()+"' Please specify a custom event wrapper");

        CustomEventWrapper wrapper = wrapperList.get(0);
        if(wrapper.equals(CustomEventWrapper.ROOT))
            throw new IllegalStateException("Fatal Error, Root is tied to a clazz or type or the entered class was null.");

        return get(clazz,wrapper);
    }
}
