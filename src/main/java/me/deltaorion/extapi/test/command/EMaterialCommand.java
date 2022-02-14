package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.command.tabcompletion.CompletionSupplier;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EMaterialCommand extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public EMaterialCommand(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND);
        this.plugin = plugin;
        registerCompleters();
    }

    private void registerCompleters() {
        registerCompleter(1, new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                List<String> completions = new ArrayList<>();
                for(EMaterial material : EMaterial.valuesThisVersion()) {
                    completions.add(material.name().toLowerCase(Locale.ROOT));
                }
                return completions;
            }
        });
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;
        EMaterial material = command.getArgOrFail(0).asEnum(EMaterial.class,"Material");
        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());

        player.getInventory().addItem(new ItemBuilder(material).build());

    }

}
