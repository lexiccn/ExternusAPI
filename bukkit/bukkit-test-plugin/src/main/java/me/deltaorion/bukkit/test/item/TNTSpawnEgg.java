package me.deltaorion.bukkit.test.item;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.custom.CustomItemEvent;
import me.deltaorion.bukkit.item.custom.ItemEventHandler;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.predicate.EventCondition;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.util.Vector;

import java.util.Objects;

public class TNTSpawnEgg extends CustomItem {

    public TNTSpawnEgg() {
        super("TNT_SPAWN_EGG", new ItemBuilder(EMaterial.MOOSHROOM_SPAWN_EGG)
                                        .setDisplayName(ChatColor.WHITE + "TNT Spawn Egg")
                                        .hideAll()
                                        .build());

    }

    @ItemEventHandler(ignoreCancelled = true)
    public void onInteract(CustomItemEvent<PlayerInteractEvent> event) {
        if(event.getEvent().getClickedBlock()==null)
            return;

        event.getEvent().setCancelled(true);
        spawn(event);
    }

    @ItemEventHandler(condition = EventCondition.OFF_HAND, ignoreCancelled = true)
    public void onOffHand(CustomItemEvent<PlayerInteractEvent> event) {
        if(event.getEvent().getClickedBlock()==null)
            return;

        if(!event.getEvent().getHand().equals(EquipmentSlot.OFF_HAND))
            return;

        Player player = event.getEvent().getPlayer();

        if(!(player.getInventory().getItemInMainHand()!=null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
            if(player.getInventory().getItemInMainHand().getType().isBlock() ||
                    player.getInventory().getItemInMainHand().getItemMeta() instanceof SpawnEggMeta) {
                return;
            }

            if(player.getInventory().getItemInMainHand().getType().isEdible()) {
                if(player.getFoodLevel()<20)
                    return;
            }
        }

        event.getEvent().setCancelled(true);
        spawn(event);
    }

    private void spawn(CustomItemEvent<PlayerInteractEvent> event) {
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
