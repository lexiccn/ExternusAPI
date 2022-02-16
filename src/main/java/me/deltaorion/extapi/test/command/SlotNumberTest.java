package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlotNumberTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public SlotNumberTest(BukkitPlugin plugin) {
        super(NO_PERMISSION);
        this.plugin = plugin;
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
        player.getInventory().setItem(command.getArgOrBlank(0).asIntOrDefault(0),new ItemStack(Material.STONE));
    }
}
