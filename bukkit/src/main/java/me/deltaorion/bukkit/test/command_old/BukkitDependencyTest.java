package me.deltaorion.bukkit.test.command_old;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.plugin.sender.Sender;
//import me.deltaorion.testdepend.TestDepend;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.junit.Assert;

import java.util.Locale;

public class BukkitDependencyTest implements CommandExecutor {

    private final ApiPlugin plugin;
    private final String DEPENDENCY = "TestDepend";

    public BukkitDependencyTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Sender s = plugin.getEServer().wrapSender(sender);
        if(!s.hasPermission(APIPermissions.COMMAND)) {
            s.sendMessage(MessageErrors.NO_PERMISSION());
            return true;
        }

        if(args.length==0) {
            s.sendMessage("Use the following parameters to test this command");
            s.sendMessage("essential - this assumes that the dependency is 100% required");
            s.sendMessage("nonessential - this assumes that the dependency is not required");
            return true;
        }

        String flag = args[0];
        boolean required = flag.equalsIgnoreCase("essential");

        if(!plugin.hasDependency(DEPENDENCY))
            plugin.registerDependency(DEPENDENCY,required);

        Assert.assertEquals(plugin.getDependency(DEPENDENCY),plugin.getDependency(DEPENDENCY.toLowerCase(Locale.ROOT)));

        s.sendMessage("Server Check: "+plugin.getEServer().isPluginEnabled(DEPENDENCY));
        s.sendMessage("Active: " + plugin.getDependency(DEPENDENCY).isActive());
        Assert.assertEquals(plugin.getEServer().isPluginEnabled(DEPENDENCY),plugin.getDependency(DEPENDENCY).isActive());
        s.sendMessage("Required: " + plugin.getDependency(DEPENDENCY).isRequired());
        s.sendMessage("Check the console IF the depedency is active");

        if(plugin.getDependency(DEPENDENCY).isActive()) {
            //TestDepend testDepend = (TestDepend) plugin.getDependency(DEPENDENCY).getDependency();
            //testDepend.test();
            s.sendMessage("Shutting down depend");
            plugin.getDependency(DEPENDENCY).getDependEPlugin().disablePlugin();
            Assert.assertFalse(plugin.getDependency(DEPENDENCY).isActive());
        }

        return true;
    }

}

