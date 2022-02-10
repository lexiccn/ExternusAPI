package me.deltaorion.extapi.test.commandold.sender;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.sent.MessageErrors;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.server.EServer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.junit.Assert;

public class SenderTestBungee extends Command {

    private final ApiPlugin plugin;

    public SenderTestBungee(ApiPlugin plugin) {
        super("sendertestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender s = plugin.wrapSender(sender);
        if(!sender.hasPermission(APIPermissions.COMMAND)) {
            sender.sendMessage(MessageErrors.NO_PERMISSION().toString());
            return;
        }
        s.sendMessage("Name: " + s.getName());
        if(sender instanceof ProxiedPlayer) {
            Assert.assertEquals(s.getName(),sender.getName());
        } else {
            Assert.assertEquals(s.getName(), EServer.CONSOLE_NAME);
        }
        s.sendMessage("UUID: " + s.getUniqueId());
        if(sender instanceof ProxiedPlayer) {
            Assert.assertEquals(s.getUniqueId(),((ProxiedPlayer) sender).getUniqueId());
        } else {
            Assert.assertEquals(EServer.CONSOLE_UUID,s.getUniqueId());
        }
        s.sendMessage("Permission abc? " +s.hasPermission("abc"));
        Assert.assertEquals(s.hasPermission("abc"),sender.hasPermission("abc"));
        s.sendMessage("OP: " + s.isOP());
        if(sender instanceof ProxiedPlayer) {
            Assert.assertFalse(s.isOP());
        } else {
            Assert.assertTrue(s.isOP());
        }
        s.sendMessage("Valid: " + s.isValid());
        Assert.assertTrue(s.isValid());
        s.sendMessage("Locale: "+s.getLocale());
        s.sendMessage("Dispatching Command... ");
        s.performCommand("help");
    }
}
