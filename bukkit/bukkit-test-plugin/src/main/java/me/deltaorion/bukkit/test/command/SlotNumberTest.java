package me.deltaorion.bukkit.test.command;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlotNumberTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public SlotNumberTest(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND);
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
