package me.deltaorion.extapi.common.command;

import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.plugin.EPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class BungeeCommand extends Command implements TabExecutor {

    private final ApiPlugin plugin;
    private final me.deltaorion.extapi.command.Command command;
    private final String name;

    public BungeeCommand(@NotNull ApiPlugin plugin, @NotNull me.deltaorion.extapi.command.Command command, @NotNull String... names) {
        super(names[0],null, Arrays.copyOfRange(names,1,names.length));
        this.plugin = Objects.requireNonNull(plugin);
        this.command = Objects.requireNonNull(command);
        this.name = names[0];
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.wrapSender(sender),args,name,command.getUsage());
        command.onCommand(context);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        SentCommand context = new SentCommand(plugin,plugin.wrapSender(sender),args,name,command.getUsage());
        return command.onTabCompletion(context);
    }
}
