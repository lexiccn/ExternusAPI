package me.deltaorion.extapi.test.cmdold.server;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.test.cmdold.JointTests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ServerTest implements CommandExecutor {

    private final ApiPlugin plugin;

    public ServerTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EServer eServer = plugin.getEServer();
        JointTests.serverTest(eServer,plugin,plugin.wrapSender(sender));
        return true;
    }
}
