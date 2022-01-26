package me.deltaorion.extapi.test.cmd.item;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.test.unit.item.BowSword;
import me.deltaorion.extapi.test.unit.item.ParryEnchant;
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

        CustomItem bowSword = new BowSword();
        CustomItem parryEnchant = new ParryEnchant();

        plugin.getCustomItemManager().registerIfAbsent(bowSword);
        plugin.getCustomItemManager().registerIfAbsent(parryEnchant);

        player.getInventory().addItem(bowSword.newCustomItem());
    }
}
