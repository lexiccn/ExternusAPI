package me.deltaorion.bukkit.test.command;

import me.deltaorion.bukkit.display.bossbar.FakeWither;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;


public class FakeWitherTest extends FunctionalCommand {
    public FakeWitherTest(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND);
        this.plugin = plugin;

        registerArguments();
    }

    private void registerArguments() {
        registerArgument("toggle",sentCommand -> {
            if(wither==null)
                return;

            if(wither.isSpawned()) {
                wither.destroy();
            } else {
                wither.spawn();
            }
        });

        registerArgument("name",sentCommand -> {
            if(wither==null)
                return;
            wither.setCustomName(sentCommand.getArgOrDefault(0,"Gamer").asString());
        });

        registerArgument("health",sentCommand -> {
            if(wither==null)
                return;

            wither.setHealth(sentCommand.getArgOrBlank(0).asIntOrDefault(300));
        });

        registerArgument("visible",sentCommand -> {
            if(wither==null)
                return;

            wither.setVisible(!wither.isVisible());
        });

        registerArgument("teleport",sentCommand -> {
            if(wither==null)
                return;

            sentCommand.getSender().sendMessage("Teleporting!");
            Player player = plugin.getServer().getPlayer(sentCommand.getSender().getUniqueId());
            wither.teleport(player.getLocation());
        });
    }

    @Nullable
    private FakeWither wither;
    private final BukkitPlugin plugin;

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());

        if(wither==null) {
            wither = new FakeWither(player,player.getLocation());
            wither.spawn();
        }
    }
}
