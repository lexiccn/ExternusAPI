package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.test.command.item.CustomItemTestCommand;
import me.deltaorion.extapi.test.command.item.ItemTestCommand;
import me.deltaorion.extapi.test.command.*;
import me.deltaorion.extapi.test.commandold.dependency.BukkitDependencyTest;
import me.deltaorion.extapi.test.commandold.playerparse.PlayerParseCommand;
import me.deltaorion.extapi.test.commandold.sender.SenderTestBukkit;
import me.deltaorion.extapi.test.commandold.server.ServerTest;
import me.deltaorion.extapi.test.unit.*;
import me.deltaorion.extapi.test.unit.generic.McTestTest;
import me.deltaorion.extapi.test.unit.generic.McTester;
import me.deltaorion.extapi.test.commandold.version.VersionTest;

public final class ExtAPI extends BukkitPlugin {

    private final McTester tester = new McTester(this);

    @Override
    public void onPluginEnable() {
        // Plugin startup logic
        registerOldCommands();
        registerCommands();

        getPluginLogger().info("This should work!");

        registerTests();
        tester.runTests();


    }

    private void registerCommands() {
        registerCommand(new MessageCommand(this),"msgtest","whispertest","telltest");
        registerCommand(new TestCommand(),"testcommand");
        registerAsyncCommand(new ShouldBeRunAsyncCommand(),"lifemeaning");
        registerCommand(new ItemTestCommand(this),"itemtest");
        registerCommand(new CustomItemTestCommand(this),"citest");
        registerCommand(new FailCommand(),"failcommand");
        registerCommand(new AnimationTest(this),"animationtest");
        registerCommand(new ScoreboardTest(this),"scoreboardtest");
        registerAsyncCommand(new ActionBarCommand(this),"ActionBarCommand");
        registerCommand(new FakeWitherTest(this),"withertest");
        registerCommand(new BossBarTest(this),"bossbartest");
        registerCommand(new LocaleCommand(),"localetest");
        registerCommand(new EMaterialCommand(this),"materialtest");
    }

    private void registerOldCommands() {
        getCommand("versiontest").setExecutor(new VersionTest(this));
        getCommand("sendertest").setExecutor(new SenderTestBukkit(this));
        getCommand("servertest").setExecutor(new ServerTest(this));
        getCommand("dependtest").setExecutor(new BukkitDependencyTest(this));
        getCommand("parsetest").setExecutor(new PlayerParseCommand(this));
    }

    private void registerTests() {
        tester.addTest(new McTestTest());
        tester.addTest(new ArgTest(this));
        tester.addTest(new LocaleTest(this));
        tester.addTest(new me.deltaorion.extapi.test.unit.ItemTest(this));
        tester.addTest(new CustomItemTest(this));
        tester.addTest(new EMaterialTest(this));
        tester.addTest(new PTest(this));
        tester.addTest(new ScoreboardingTest(this));
        tester.addTest(new ActionBarTest(this));
    }

    @Override
    public void onPluginDisable() {
        // Plugin shutdown logic
    }
}
