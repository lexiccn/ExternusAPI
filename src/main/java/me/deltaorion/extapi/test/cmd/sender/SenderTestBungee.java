package me.deltaorion.extapi.test.cmd.sender;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.cmd.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SenderTestBungee extends Command {

    private final ApiPlugin plugin;

    public SenderTestBungee(ApiPlugin plugin) {
        super("sendertestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender s = plugin.wrapSender(sender);
        JointTests.testSender(s);
    }
}
