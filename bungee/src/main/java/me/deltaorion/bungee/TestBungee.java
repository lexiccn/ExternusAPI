package me.deltaorion.bungee;

import me.deltaorion.bungee.configuration.YamlAdapter;
import me.deltaorion.bungee.plugin.plugin.BungeePlugin;
import me.deltaorion.bungee.test.command_old.BungeeDependencyTest;
import me.deltaorion.bungee.test.command_old.BungeeVersionTest;
import me.deltaorion.bungee.test.command_old.SenderTestBungee;
import me.deltaorion.bungee.test.command_old.ServerTestBungee;
import me.deltaorion.common.test.command.*;
import me.deltaorion.common.test.generic.McTester;
import me.deltaorion.common.test.unit.ArgTest;
import me.deltaorion.common.test.unit.ConfigurationTest;
import me.deltaorion.common.test.unit.LocaleTest;
import me.deltaorion.common.test.unit.McTestTest;

public class TestBungee extends BungeePlugin {

    private final McTester tester = new McTester(this);

    @Override
    public void onPluginEnable() {
        getPluginLogger().info("Plugin Initialising");

        registerOldCommands();
        registerCommands();

        registerTests();
        tester.runTests();
    }

    @Override
    public void onPluginDisable() {
        tester.shutdown();
    }

    private void registerCommands() {
        registerCommand(new MessageCommand(this),"msg","whisper","tell");
        registerCommand(new TestCommand(),"testcommand");
        registerCommand(new ShouldBeRunAsyncCommand(),"lifemeaning");
        registerCommand(new FailCommand(),"failcommand");
        registerCommand(new ChatAnimationTestCommand(this),"animationtestchat");
        registerCommand(new LocaleCommand(),"localetest");
    }

    private void registerOldCommands() {
        getProxy().getPluginManager().registerCommand(this,new BungeeVersionTest(this));
        getProxy().getPluginManager().registerCommand(this,new SenderTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new ServerTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeDependencyTest(this));
    }

    private void registerTests() {
        tester.addTest(new McTestTest());
        tester.addTest(new ArgTest(this));
        tester.addTest(new LocaleTest(this));
        tester.addTest(new ConfigurationTest(new YamlAdapter()));
    }
}
