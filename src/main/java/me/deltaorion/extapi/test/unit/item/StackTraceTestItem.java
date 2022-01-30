package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.CustomItemEventListener;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.predicate.EventPredicates;
import me.deltaorion.extapi.item.wrapper.CustomEventWrapper;
import me.deltaorion.extapi.item.wrapper.EventWrappers;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StackTraceTestItem extends CustomItem {

    private final BukkitPlugin plugin;

    public StackTraceTestItem(BukkitPlugin plugin) {
        super("Stack_Trace_Test",new ItemBuilder(EMaterial.FURNACE)
        .setDisplayName("Stack Tracer").build());
        this.plugin = plugin;
    }

    @Override
    public void onRegister() {
        CustomItemEventListener.register(plugin,
                EntityDamageByEntityEvent.class,
                this,
                EventPredicates.MAIN_HAND(),
                EventWrappers.GET_DAMAGED(),
                this::damaging
                ,true,
                EventPriority.NORMAL,
                true);
    }

    @ItemEventHandler(wrapper = CustomEventWrapper.DAMAGE_ANOTHER_ENTITY)
    public void onDamage(CustomItemEvent<EntityDamageByEntityEvent> event) {
        System.out.println("Damaged another entity");
        throw new RuntimeException();
    }

    public void damaging(CustomItemEvent<EntityDamageByEntityEvent> event) {
        throw new RuntimeException();
    }
}
