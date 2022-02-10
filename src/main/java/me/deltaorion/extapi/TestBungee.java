package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BungeePlugin;
import me.deltaorion.extapi.test.command.*;
import me.deltaorion.extapi.test.commandold.dependency.BungeeDependencyTest;
import me.deltaorion.extapi.test.commandold.sender.SenderTestBungee;
import me.deltaorion.extapi.test.commandold.server.ServerTestBungee;
import me.deltaorion.extapi.test.commandold.version.BungeeVersionTest;
import me.deltaorion.extapi.test.unit.ArgTest;
import me.deltaorion.extapi.test.unit.LocaleTest;
import me.deltaorion.extapi.test.unit.generic.McTestTest;
import me.deltaorion.extapi.test.unit.generic.McTester;

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

    private void registerCommands() {
        registerCommand(new MessageCommand(this),"msg","whisper","tell");
        registerCommand(new TestCommand(),"testcommand");
        registerCommand(new ShouldBeRunAsyncCommand(),"lifemeaning");
        registerCommand(new FailCommand(),"failcommand");
        registerCommand(new AnimationTest(this),"animationtest");
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
    }
}
