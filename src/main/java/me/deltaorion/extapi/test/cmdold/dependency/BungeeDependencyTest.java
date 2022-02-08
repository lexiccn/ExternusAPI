package me.deltaorion.extapi.test.cmdold.dependency;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.testdepend.TestDependBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeDependencyTest extends Command {

    private final ApiPlugin plugin;

    public BungeeDependencyTest(ApiPlugin plugin) {
        super("dependencytestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Sender s = plugin.wrapSender(sender);

        if(args.length==0) {
            s.sendMessage("Use the following parameters to test this command");
            s.sendMessage("essential - this assumes that the dependency is 100% required");
            s.sendMessage("nonessential - this assumes that the dependency is not required");
            return;
        }

        String flag = args[0];
        boolean required = flag.equalsIgnoreCase("essential");

        String DEPENDENCY = "TestDepend";

        if(!plugin.hasDependency(DEPENDENCY))
            plugin.registerDependency(DEPENDENCY,required);

        s.sendMessage("Server Check: "+plugin.getEServer().isPluginEnabled(DEPENDENCY));
        s.sendMessage("Active: " + plugin.getDependency(DEPENDENCY).isActive());
        s.sendMessage("Required: " + plugin.getDependency(DEPENDENCY).isRequired());
        s.sendMessage("Check the console IF the depedency is active");

        if(plugin.getDependency(DEPENDENCY).isActive()) {
            TestDependBungee testDepend = (TestDependBungee) plugin.getDependency(DEPENDENCY).getDependency();
            testDepend.test();
            s.sendMessage("Shutting down depend");
            plugin.getDependency(DEPENDENCY).getDependEPlugin().disablePlugin();
            s.sendMessage("Should be false - " + plugin.getDependency(DEPENDENCY).isActive());
        }
    }
}
