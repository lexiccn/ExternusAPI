package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.test.cmd.dependency.BukkitDependencyTest;
import me.deltaorion.extapi.test.cmd.locale.LocaleTestBukkit;
import me.deltaorion.extapi.test.cmd.playerparse.PlayerParseCommand;
import me.deltaorion.extapi.test.cmd.sender.SenderTest;
import me.deltaorion.extapi.test.cmd.server.ServerTest;
import me.deltaorion.extapi.test.unit.McTestTest;
import me.deltaorion.extapi.test.unit.McTester;
import me.deltaorion.extapi.test.cmd.version.VersionTest;
import me.deltaorion.extapi.test.unit.arg.ArgTest;

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

        getPluginLogger().info("This should work!");

        registerTests();
        tester.runTests();
    }

    private void registerTests() {
        tester.addTest(new McTestTest());
        tester.addTest(new ArgTest(this));
    }

    @Override
    public void onPluginDisable() {
        // Plugin shutdown logic
    }
}
