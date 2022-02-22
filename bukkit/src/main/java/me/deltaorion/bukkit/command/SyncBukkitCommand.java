package me.deltaorion.bukkit.command;

import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * The following class acts as a driver for a command. It is to take a bukkit sent command and wrap it in the SentCommand adapter.
 * Then pass it into the command so that the logic can fall through.
 */
public class SyncBukkitCommand implements TabExecutor {

    @NotNull private final ApiPlugin plugin;
    @NotNull private final me.deltaorion.common.command.Command command;

    public SyncBukkitCommand(@NotNull ApiPlugin plugin, @NotNull me.deltaorion.common.command.Command command) {
        this.plugin = Objects.requireNonNull(plugin);
        this.command = Objects.requireNonNull(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.getEServer().wrapSender(sender),args,label,this.command.getUsage());
        this.command.onCommand(context);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.getEServer().wrapSender(sender),args,alias,this.command.getUsage());
        return this.command.onTabCompletion(context);
    }
}
