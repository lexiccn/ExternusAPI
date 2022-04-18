package me.deltaorion.bungee.test.command_old;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.version.VersionFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeVersionTest extends Command {

    private final Plugin plugin;

    public BungeeVersionTest(Plugin plugin) {
        super("versiontestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString());
            return;
        }
        sender.sendMessage(plugin.getProxy().getVersion());
        System.out.println(VersionFactory.parse(plugin.getProxy().getVersion()));
    }
}
