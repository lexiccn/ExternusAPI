package me.deltaorion.extapi.command.implementation;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The following class acts as a driver for a command. It is to take a bukkit sent command and wrap it in the SentCommand adapter.
 * Then pass it into the command so that the logic can fall through. This however will run the task asynchronously. it is
 * crucial that all code in the command is threadsafe.
 */
public class AsyncBukkitCommand implements TabExecutor {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Command command;

    public AsyncBukkitCommand(@NotNull BukkitPlugin plugin, @NotNull Command command) {
        this.plugin = plugin;
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.getEServer().wrapSender(sender),args,label,this.command.getUsage());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AsyncBukkitCommand.this.command.onCommand(context);
            }
        };
        plugin.runCommandAsync(runnable);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.getEServer().wrapSender(sender),args,alias,this.command.getUsage());
        return this.command.onTabCompletion(context);
    }
}
