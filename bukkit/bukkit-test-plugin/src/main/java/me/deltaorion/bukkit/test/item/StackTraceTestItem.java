package me.deltaorion.bukkit.test.item;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.custom.CustomItemEvent;
import me.deltaorion.bukkit.item.custom.CustomItemEventListener;
import me.deltaorion.bukkit.item.custom.ItemEventHandler;
import me.deltaorion.bukkit.item.predicate.EventPredicates;
import me.deltaorion.bukkit.item.wrapper.CustomEventWrapper;
import me.deltaorion.bukkit.item.wrapper.EventWrappers;
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
