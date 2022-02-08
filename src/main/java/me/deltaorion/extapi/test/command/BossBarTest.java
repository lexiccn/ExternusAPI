package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.entity.Player;

public class BossBarTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public BossBarTest(BukkitPlugin plugin) {
        super(NO_PERMISSION);
        this.plugin = plugin;
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
