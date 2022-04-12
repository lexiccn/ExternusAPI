package me.deltaorion.bukkit;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.test.command.*;
import me.deltaorion.bukkit.test.command_old.*;
import me.deltaorion.bukkit.test.unit.*;
import me.deltaorion.common.config.yaml.YamlAdapter;
import me.deltaorion.common.test.command.*;
import me.deltaorion.common.test.generic.AsyncFailTest;
import me.deltaorion.common.test.generic.McTester;
import me.deltaorion.common.test.unit.ArgTest;
import me.deltaorion.common.test.unit.ConfigurationTest;
import me.deltaorion.common.test.unit.LocaleTest;
import me.deltaorion.common.test.unit.McTestTest;
import org.bukkit.plugin.java.JavaPluginLoader;

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
        registerCommand(new ChatAnimationTestCommand(this),"animationtestchat");
        registerCommand(new CobblestoneAnimationTestCommand(this),"animationtestcobble");
        registerCommand(new ScoreboardTest(this),"scoreboardtest");
        registerAsyncCommand(new ActionBarCommand(this),"ActionBarCommand");
        registerCommand(new FakeWitherTest(this),"withertest");
        registerCommand(new BossBarTest(this),"bossbartest");
        registerCommand(new LocaleCommand(),"localetest");
        registerCommand(new EMaterialCommand(this),"materialtest");
        registerCommand(new SlotNumberTest(this),"slotnumbertest");

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
        tester.addTest(new ItemTest(this));
        tester.addTest(new CustomItemTest(this));
        tester.addTest(new EMaterialTest(this));
        tester.addTest(new PTest(this));
        tester.addTest(new ScoreboardingTest(this));
        tester.addTest(new ConfigurationTest(new YamlAdapter()));
        tester.addAsyncTest(new ActionBarTest(this));
        tester.addAsyncTest(new AsyncFailTest());
    }

    @Override
    public void onPluginDisable() {
        tester.shutdown();
    }
}
