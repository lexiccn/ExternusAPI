package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.predicate.EventCondition;
import me.deltaorion.extapi.util.BukkitMetadataUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BowSword extends CustomItem {

    private final BukkitPlugin plugin;

    public BowSword(BukkitPlugin plugin) {
        super("Bow_Sword",
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+"Bow Sword!")
                        .addLoreLine("")
                        .addLoreLine(ChatColor.WHITE+"A bow with a twist!")
                        .hideAll().build());
        this.plugin = plugin;
    }

    @ItemEventHandler(condition = EventCondition.MAIN_HAND, priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onRightClick(CustomItemEvent<PlayerInteractEvent> event) {

        if(!(event.getEvent().getAction().equals(Action.RIGHT_CLICK_AIR) || event.getEvent().getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        if(event.getEvent().getAction().equals(Action.RIGHT_CLICK_AIR)) {
            event.getEvent().setCancelled(true);
        }

        event.getEntity().launchProjectile(Arrow.class,event.getEntity().getLocation().getDirection().normalize().multiply(3));
    }

    @ItemEventHandler
    public void onProjectile(CustomItemEvent<ProjectileLaunchEvent> event) {
        Entity entity = event.getEvent().getEntity();
        if(entity instanceof Arrow) {
            ((Arrow) entity).setCritical(true);
            entity.setFireTicks(1000);
            BukkitMetadataUtil.setMetadata(entity,plugin,getName(),"");
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(event.getEntity().hasMetadata(getName())) {
            if(Math.random() > 0.9f) {
                event.getEntity().getLocation().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.ZOMBIE);
            }
        }
    }
}
