package me.deltaorion.extapi.item.wrapper;

import com.google.gson.reflect.TypeToken;
import me.deltaorion.extapi.test.unit.bukkit.TestEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public enum CustomEventWrapper {



    ROOT(null),
    PLAYER_DAMAGED_BY_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    PLAYED_DAMAGES_ENTITY(TypeToken.get(EntityDamageByEntityEvent.class)),
    PLAYER_EVENT(TypeToken.get(PlayerEvent.class)),
    ENTITY_EVENT(TypeToken.get(EntityEvent.class)),
    ;

    private final TypeToken<?> typeToken;
    private final static Map<TypeToken<?>,List<CustomEventWrapper>> byType;

    static {
        Map<TypeToken<?>,List<CustomEventWrapper>> temp = new HashMap<>();
        for(CustomEventWrapper wrapper : CustomEventWrapper.values()) {
            if(wrapper==CustomEventWrapper.ROOT)
                continue;

            List<CustomEventWrapper> wrappers;
            if(!temp.containsKey(wrapper.typeToken)) {
                wrappers = new ArrayList<>();
                temp.put(wrapper.typeToken,wrappers);
            } else {
                wrappers = temp.get(wrapper.typeToken);
            }
            wrappers.add(wrapper);
        }
        byType = Collections.unmodifiableMap(temp);
    }

    CustomEventWrapper(TypeToken<?> typeToken) {
        this.typeToken = typeToken;
    }

    @Nullable
    public static List<CustomEventWrapper> fromClass(Class<?> clazz) {
        TypeToken<?> typeToken = TypeToken.get(clazz);
        if(byType.containsKey(typeToken)) {
            return byType.get(typeToken);
        } else {
            for(Map.Entry<TypeToken<?>,List<CustomEventWrapper>> entry : byType.entrySet()) {
                if(entry.getKey().getRawType().isAssignableFrom(clazz)) {
                    return entry.getValue();
                }
            }

            return null;
        }
    }
}
