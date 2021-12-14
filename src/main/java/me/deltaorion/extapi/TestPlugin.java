package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BungeePlugin;
import me.deltaorion.extapi.test.dependency.BungeeDependencyTest;
import me.deltaorion.extapi.test.locale.LocaleTestBungee;
import me.deltaorion.extapi.test.sender.SenderTestBungee;
import me.deltaorion.extapi.test.server.ServerTestBungee;
import me.deltaorion.extapi.test.version.BungeeVersionTest;

public class TestPlugin extends BungeePlugin {

    @Override
    public void onEnable() {
        System.out.println("Enabling Test Plugin");
        getProxy().getPluginManager().registerCommand(this,new BungeeVersionTest(this));
        getProxy().getPluginManager().registerCommand(this,new SenderTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new ServerTestBungee(this));
        getProxy().getPluginManager().registerCommand(this, new BungeeDependencyTest(this));
        getProxy().getPluginManager().registerCommand(this, new LocaleTestBungee(this));
    }
}
