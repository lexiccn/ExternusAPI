package me.deltaorion.bukkit.test.command;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.command.tabcompletion.CompletionSupplier;
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
        registerArgument("match",command -> {
            if(command.getSender().isConsole())
                return;

            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            EMaterial material = EMaterial.matchMaterial(player.getItemInHand());
            command.getSender().sendMessage(material);
            command.getSender().sendMessage("dura dependent: " + material.isDurabilityDependent());
            command.getSender().sendMessage("duplicates: "+material.getDuplicates());
        });
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
