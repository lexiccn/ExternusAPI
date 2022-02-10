package me.deltaorion.extapi.test.commandold.sender;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.sent.MessageErrors;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.server.EServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Assert;

public class SenderTestBukkit implements CommandExecutor {

    private final ApiPlugin plugin;

    public SenderTestBukkit(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString());
            return true;
        }
        Sender s = plugin.wrapSender(sender);
        s.sendMessage("Name: " + s.getName());
        if(sender instanceof Player) {
            Assert.assertEquals(s.getName(),sender.getName());
        } else {
            Assert.assertEquals(s.getName(), EServer.CONSOLE_NAME);
        }
        s.sendMessage("UUID: " + s.getUniqueId());
        if(sender instanceof Player) {
            Assert.assertEquals(s.getUniqueId(),((Player) sender).getUniqueId());
        } else {
            Assert.assertEquals(EServer.CONSOLE_UUID,s.getUniqueId());
        }
        s.sendMessage("Permission abc? " +s.hasPermission("abc"));
        Assert.assertEquals(s.hasPermission("abc"),sender.hasPermission("abc"));
        s.sendMessage("OP: " + s.isOP());
        Assert.assertEquals(sender.isOp(),s.isOP());
        s.sendMessage("Valid: " + s.isValid());
        Assert.assertTrue(s.isValid());
        s.sendMessage("Locale: "+s.getLocale());
        s.sendMessage("Dispatching Command... ");
        s.performCommand("help");
        return true;
    }
}
