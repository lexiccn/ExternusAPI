package me.deltaorion.extapi.test.sender;

import me.deltaorion.extapi.ExtAPI;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SenderTest implements CommandExecutor {

    private final EPlugin plugin;

    public SenderTest(EPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Sender s = plugin.wrapSender(sender);
        JointTests.testSender(s);
        return true;
    }
}
