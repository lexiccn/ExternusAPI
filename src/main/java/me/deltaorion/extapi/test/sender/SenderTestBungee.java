package me.deltaorion.extapi.test.sender;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SenderTestBungee extends Command {

    private final EPlugin plugin;

    public SenderTestBungee(EPlugin plugin) {
        super("sendertestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender s = plugin.wrapSender(sender);
        JointTests.testSender(s);
    }
}
