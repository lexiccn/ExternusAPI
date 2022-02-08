package me.deltaorion.extapi.test.cmdold.locale;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.test.cmdold.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class LocaleTestBungee extends Command {

    private final ApiPlugin plugin;

    public LocaleTestBungee(ApiPlugin plugin) {
        super("localetestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JointTests.translationTest(plugin.wrapSender(sender));
    }
}
