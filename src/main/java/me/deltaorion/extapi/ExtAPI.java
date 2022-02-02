package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.test.cmd.item.CustomItemTestCommand;
import me.deltaorion.extapi.test.cmd.item.ItemTest;
import me.deltaorion.extapi.test.command.*;
import me.deltaorion.extapi.test.cmd.dependency.BukkitDependencyTest;
import me.deltaorion.extapi.test.cmd.locale.LocaleTestBukkit;
import me.deltaorion.extapi.test.cmd.playerparse.PlayerParseCommand;
import me.deltaorion.extapi.test.cmd.sender.SenderTest;
import me.deltaorion.extapi.test.cmd.server.ServerTest;
import me.deltaorion.extapi.test.unit.EMaterialTest;
import me.deltaorion.extapi.test.unit.LocaleTest;
import me.deltaorion.extapi.test.unit.generic.McTestTest;
import me.deltaorion.extapi.test.unit.generic.McTester;
import me.deltaorion.extapi.test.cmd.version.VersionTest;
import me.deltaorion.extapi.test.unit.ArgTest;
import me.deltaorion.extapi.test.unit.CustomItemTest;

public final class ExtAPI extends BukkitPlugin {

    private final McTester tester = new McTester();

    @Override
    public void onPluginEnable() {
        // Plugin startup logic
        getCommand("versiontest").setExecutor(new VersionTest(this));
        getCommand("sendertest").setExecutor(new SenderTest(this));
        getCommand("servertest").setExecutor(new ServerTest(this));
        getCommand("dependtest").setExecutor(new BukkitDependencyTest(this));
        getCommand("localetest").setExecutor(new LocaleTestBukkit(this));
        getCommand("parsetest").setExecutor(new PlayerParseCommand(this));

        registerCommand(new MessageCommand(this),"msgtest","whispertest","telltest");
        registerCommand(new TestCommand(),"testcommand");
        registerAsyncCommand(new ShouldBeRunAsyncCommand(),"lifemeaning");
        registerCommand(new ItemTest(this),"itemtest");
        registerCommand(new CustomItemTestCommand(this),"citest");
        registerCommand(new FailCommand(),"failcommand");
        registerCommand(new AnimationTest(this),"animationtest");

        getPluginLogger().info("This should work!");

        registerTests();
        tester.runTests();

    }

    private void registerTests() {
        tester.addTest(new McTestTest());
        tester.addTest(new ArgTest(this));
        tester.addTest(new me.deltaorion.extapi.test.unit.ItemTest(this));
        tester.addTest(new CustomItemTest(this));
        tester.addTest(new LocaleTest(this));
        tester.addTest(new EMaterialTest());
    }

    @Override
    public void onPluginDisable() {
        // Plugin shutdown logic
    }
}
