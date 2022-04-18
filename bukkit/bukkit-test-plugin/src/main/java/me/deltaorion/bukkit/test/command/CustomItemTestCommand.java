package me.deltaorion.bukkit.test.command;

import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.plugin.plugin.BukkitAPIDepends;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.test.item.*;
import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CustomItemTestCommand extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public CustomItemTestCommand(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND,NO_USAGE);
        this.plugin = plugin;
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            throw new CommandException("Test as a player");

        if(!plugin.getDependency(BukkitAPIDepends.NBTAPI).isActive())
            throw new CommandException("Please enable the NBT API on the test server to use this command!");

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());

        CustomItem bowSword = new BowSword(plugin);
        CustomItem parryEnchant = new ParryEnchant();
        CustomItem everything = new EverythingItem(plugin);
        CustomItem slotType = new SlotTypeTest();
        CustomItem TntSpawnEgg = new TNTSpawnEgg();
        CustomItem stackTracer = new StackTraceTestItem(plugin);

        plugin.getCustomItemManager().registerIfAbsent(bowSword);
        plugin.getCustomItemManager().registerIfAbsent(parryEnchant);
        plugin.getCustomItemManager().registerIfAbsent(everything);
        plugin.getCustomItemManager().registerIfAbsent(slotType);
        plugin.getCustomItemManager().registerIfAbsent(TntSpawnEgg);
        plugin.getCustomItemManager().registerIfAbsent(stackTracer);

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
        player.getInventory().addItem(stackTracer.newCustomItem());
    }
}
