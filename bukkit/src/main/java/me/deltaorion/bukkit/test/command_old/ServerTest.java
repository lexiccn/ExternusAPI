package me.deltaorion.bukkit.test.command_old;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.plugin.server.EServer;
import me.deltaorion.common.test.command_old.JointTests;
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
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString());
            return true;
        }
        EServer eServer = plugin.getEServer();
        JointTests.serverTest(eServer,plugin,plugin.getEServer().wrapSender(sender));
        return true;
    }
}
