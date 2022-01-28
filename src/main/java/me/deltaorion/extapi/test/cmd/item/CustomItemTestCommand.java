package me.deltaorion.extapi.test.cmd.item;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.test.unit.item.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CustomItemTestCommand extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public CustomItemTestCommand(BukkitPlugin plugin) {
        super(NO_PERMISSION,NO_USAGE);
        this.plugin = plugin;
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            throw new CommandException("Test as a player");

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());

        CustomItem bowSword = new BowSword(plugin);
        CustomItem parryEnchant = new ParryEnchant();
        CustomItem everything = new EverythingItem(plugin);
        CustomItem slotType = new SlotTypeTest();
        CustomItem TntSpawnEgg = new TNTSpawnEgg();

        plugin.getCustomItemManager().registerIfAbsent(bowSword);
        plugin.getCustomItemManager().registerIfAbsent(parryEnchant);
        plugin.getCustomItemManager().registerIfAbsent(everything);
        plugin.getCustomItemManager().registerIfAbsent(slotType);
        plugin.getCustomItemManager().registerIfAbsent(TntSpawnEgg);

        if(command.getArgOrBlank(0).asString().equals("entity")) {
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
            entity.getEquipment().setItemInHand(everything.newCustomItem());
            return;
        }

        if(command.getArgOrBlank(0).asString().equals("slottype")) {
            LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(),EntityType.ZOMBIE);
            entity.getEquipment().setItemInHand(slotType.newCustomItem());
            player.getInventory().addItem(slotType.newCustomItem());
            return;
        }

        player.getInventory().addItem(bowSword.newCustomItem());
        player.getInventory().addItem(everything.newCustomItem());
        player.getInventory().addItem(TntSpawnEgg.newCustomItem());
    }
}
