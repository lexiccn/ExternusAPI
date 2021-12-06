package me.deltaorion.extapi.test.server;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ServerTestBungee extends Command {

    private final EPlugin plugin;

    public ServerTestBungee(EPlugin plugin) {
        super("servertestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JointTests.serverTest(plugin.getEServer(),plugin,plugin.wrapSender(sender));
    }
}
