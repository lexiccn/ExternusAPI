package me.deltaorion.extapi.test.dependency;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import me.deltaorion.testdepend.TestDepend;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BukkitDependencyTest implements CommandExecutor {

    private final EPlugin plugin;
    private final String DEPENDENCY = "TestDepend";

    public BukkitDependencyTest(EPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Sender s = plugin.wrapSender(sender);

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

        s.sendMessage("Server Check: "+plugin.getEServer().isPluginEnabled(DEPENDENCY));
        s.sendMessage("Active: " + plugin.getDependency(DEPENDENCY).isActive());
        s.sendMessage("Required: " + plugin.getDependency(DEPENDENCY).isRequired());
        s.sendMessage("Check the console IF the depedency is active");

        if(plugin.getDependency(DEPENDENCY).isActive()) {
            TestDepend testDepend = (TestDepend) plugin.getDependency(DEPENDENCY).getPlugin().getPluginObject();
            testDepend.test();
        }
        return true;
    }
}
