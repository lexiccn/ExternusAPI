package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

public class TitleTest extends FunctionalCommand {
    public TitleTest(BukkitPlugin plugin) {
        super(NO_PERMISSION);
        this.plugin = plugin;
    }

    private final BukkitPlugin plugin;

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
    }
}
