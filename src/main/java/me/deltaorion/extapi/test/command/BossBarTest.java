package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BossBarTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public BossBarTest(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND);
        this.plugin = plugin;
        registerArgument("message",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setMessage(command.getArgOrDefault(0,"Gamer").asString());
        });

        registerArgument("visible",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setVisible(!bossBar.isVisible());
        });

        registerArgument("progress",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setProgress(command.getArgOrBlank(0).asFloatOrDefault(1));
        });

        registerArgument("remove",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            plugin.getBukkitPlayerManager().getPlayer(player).setBossBar(null);
        });

        registerArgument("args",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;
            List<String> args = new ArrayList<>(command.getRawArgs());
            bossBar.setArgs(args.toArray());
        });
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
        String name = command.getArgOrDefault(0,"Gamer").asString();
        float progress = command.getArgOrBlank(1).asFloatOrDefault(1f);
        BukkitApiPlayer p = plugin.getBukkitPlayerManager().getPlayer(player);
        BossBar bossBar = new BossBar(plugin,player,"bb-1");
        bossBar.setMessage(name);
        bossBar.setProgress(progress);
    }
}
