package me.deltaorion.extapi.test.commandold.server;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.sent.MessageErrors;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.test.commandold.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ServerTestBungee extends Command {

    private final ApiPlugin plugin;

    public ServerTestBungee(ApiPlugin plugin) {
        super("servertestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString());
            return;
        }
        JointTests.serverTest(plugin.getEServer(),plugin,plugin.wrapSender(sender));
    }
}
