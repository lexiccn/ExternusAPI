package me.deltaorion.extapi.test.locale;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LocaleTestBukkit implements CommandExecutor {

    private final EPlugin plugin;

    public LocaleTestBukkit(EPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>0) {
            if(args[0].equals("reload")) {
                plugin.getTranslator().reload();
                return true;
            }
        }
        JointTests.translationTest(plugin.wrapSender(sender));
        return true;
    }
}
