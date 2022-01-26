package me.deltaorion.extapi.test.cmd.sender;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.test.cmd.JointTests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SenderTest implements CommandExecutor {

    private final ApiPlugin plugin;

    public SenderTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Sender s = plugin.wrapSender(sender);
        JointTests.testSender(s);
        return true;
    }
}
