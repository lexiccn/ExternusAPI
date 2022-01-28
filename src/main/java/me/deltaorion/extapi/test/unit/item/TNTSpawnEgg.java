package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.position.InventoryItem;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class TNTSpawnEgg extends CustomItem {

    public TNTSpawnEgg() {
        super("TNT_SPAWN_EGG", new ItemBuilder(EMaterial.SPAWN_MOOSHROOM_EGG)
                                        .setDisplayName(ChatColor.WHITE + "TNT Spawn Egg")
                                        .hideAll()
                                        .build());

    }

    @ItemEventHandler
    public void onInteract(CustomItemEvent<PlayerInteractEvent> event) {
        if(event.getEvent().getClickedBlock()!=null) {
            event.getEvent().setCancelled(true);
            InventoryItem item = event.getItemStacks().get(0);
            Player player = event.getEvent().getPlayer();
            if(player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                if (Objects.requireNonNull(item.getItemStack()).getAmount() == 1) {
                    item.removeItem();
                } else {
                    item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
                }
            }
            event.getEntity().getWorld().spawnEntity(event.getEvent().getClickedBlock().getLocation().add(new Vector(event.getEvent().getBlockFace().getModX(),event.getEvent().getBlockFace().getModY(),event.getEvent().getBlockFace().getModZ())), EntityType.PRIMED_TNT);
        }
    }
}
