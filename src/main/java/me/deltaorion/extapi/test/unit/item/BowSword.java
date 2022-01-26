package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.predicate.EventCondition;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BowSword extends CustomItem {

    public BowSword() {
        super("Bow_Sword",
                new ItemBuilder(Material.DIAMOND_SWORD)
                        .setDisplayName(ChatColor.GOLD.toString()+ChatColor.BOLD.toString()+"Bow Sword!")
                        .addLoreLine("")
                        .addLoreLine(ChatColor.WHITE+"A bow with a twist!")
                        .hideAll().build());
    }

    @ItemEventHandler(predicate = EventCondition.MAIN_HAND, priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onRightClick(CustomItemEvent<PlayerInteractEvent> event) {

        if(!(event.getEvent().getAction().equals(Action.RIGHT_CLICK_AIR) || event.getEvent().getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        if(event.getEvent().getAction().equals(Action.RIGHT_CLICK_AIR)) {
            event.getEvent().setCancelled(true);
        }

        event.getPlayer().sendMessage("Good job you right clicked!");
    }
}
