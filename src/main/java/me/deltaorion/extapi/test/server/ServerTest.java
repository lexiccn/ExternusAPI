package me.deltaorion.extapi.test.server;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.test.JointTests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class ServerTest implements CommandExecutor {

    private final EPlugin plugin;

    public ServerTest(EPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        EServer eServer = plugin.getEServer();
        JointTests.serverTest(eServer,plugin,plugin.wrapSender(sender));
        return true;
    }
}
