package me.deltaorion.extapi.test.locale;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.JointTests;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class LocaleTestBungee extends Command {

    private final EPlugin plugin;

    public LocaleTestBungee(EPlugin plugin) {
        super("localetestbungee");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        JointTests.translationTest(plugin.wrapSender(sender));
    }
}
