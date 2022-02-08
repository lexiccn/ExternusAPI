package me.deltaorion.extapi.test.cmdold.version;

import me.deltaorion.extapi.common.version.VersionFactory;
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
        sender.sendMessage(plugin.getProxy().getVersion());
        System.out.println(VersionFactory.parse(plugin.getProxy().getVersion()));
    }
}
