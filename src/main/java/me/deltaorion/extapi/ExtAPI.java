package me.deltaorion.extapi;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.test.dependency.BukkitDependencyTest;
import me.deltaorion.extapi.test.sender.SenderTest;
import me.deltaorion.extapi.test.server.ServerTest;
import me.deltaorion.extapi.test.version.VersionTest;

public final class ExtAPI extends BukkitPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("versiontest").setExecutor(new VersionTest(this));
        getCommand("sendertest").setExecutor(new SenderTest(this));
        getCommand("servertest").setExecutor(new ServerTest(this));
        getCommand("dependtest").setExecutor(new BukkitDependencyTest(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
