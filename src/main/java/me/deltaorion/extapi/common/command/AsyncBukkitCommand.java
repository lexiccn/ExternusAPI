package me.deltaorion.extapi.common.command;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AsyncBukkitCommand implements TabExecutor {

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final Command command;

    public AsyncBukkitCommand(@NotNull BukkitPlugin plugin, @NotNull Command command) {
        this.plugin = plugin;
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.wrapSender(sender),args,label,this.command.getUsage());
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
        SentCommand context = new SentCommand(plugin,plugin.wrapSender(sender),args,alias,this.command.getUsage());
        return this.command.onTabCompletion(context);
    }
}
