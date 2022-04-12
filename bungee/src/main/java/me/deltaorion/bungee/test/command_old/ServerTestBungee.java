package me.deltaorion.bungee.test.command_old;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.test.command_old.JointTests;
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
        JointTests.serverTest(plugin.getEServer(),plugin,plugin.getEServer().wrapSender(sender));
    }
}
