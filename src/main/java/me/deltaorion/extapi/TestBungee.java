package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BungeePlugin;
import me.deltaorion.extapi.test.command.*;
import me.deltaorion.extapi.test.cmdold.dependency.BungeeDependencyTest;
import me.deltaorion.extapi.test.cmdold.locale.LocaleTestBungee;
import me.deltaorion.extapi.test.cmdold.sender.SenderTestBungee;
import me.deltaorion.extapi.test.cmdold.server.ServerTestBungee;
import me.deltaorion.extapi.test.cmdold.version.BungeeVersionTest;

public class TestBungee extends BungeePlugin {

    @Override
    public void onPluginEnable() {
        System.out.println("Enabling Test Plugin");
        getProxy().getPluginManager().registerCommand(this,new BungeeVersionTest(this));
        getProxy().getPluginManager().registerCommand(this,new SenderTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new ServerTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeDependencyTest(this));
        getProxy().getPluginManager().registerCommand(this, new LocaleTestBungee(this));

        getPluginLogger().info("This should work!");
        registerCommand(new MessageCommand(this),"msg","whisper","tell");
        registerCommand(new TestCommand(),"testcommand");
        registerCommand(new ShouldBeRunAsyncCommand(),"lifemeaning");
        registerCommand(new FailCommand(),"failcommand");
        registerCommand(new AnimationTest(this),"animationtest");
    }
}
