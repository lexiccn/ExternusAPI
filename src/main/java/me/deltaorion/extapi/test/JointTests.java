package me.deltaorion.extapi.test;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.server.EServer;

import java.util.concurrent.TimeUnit;

public class JointTests {
    public static void testSender(Sender sender) {
        sender.sendMessage("Name: " + sender.getName());
        sender.sendMessage("UUID: " + sender.getUniqueId());
        sender.sendMessage("Permission abc? " +sender.hasPermission("abc"));
        sender.sendMessage("OP: " + sender.isOP());
        sender.sendMessage("Valid: " + sender.isValid());
    }

    public static void serverTest(EServer eServer, EPlugin plugin, Sender sender) {
        plugin.getScheduler().runTaskLater(() -> {
            sender.sendMessage("This happens 1 second later");
        },1, TimeUnit.SECONDS);
        sender.sendMessage("Version: " + eServer.getVersion());
        sender.sendMessage("Brand: " + eServer.getBrand());
        sender.sendMessage("Online Players: " + eServer.getOnlinePlayers());
        sender.sendMessage("Max-Player: " + eServer.getMaxPlayer());
        sender.sendMessage("Player Online: " + eServer.isPlayerOnline(sender.getUniqueId()));
        plugin.getPluginLogger().warn("warn");
        plugin.getPluginLogger().severe("severe");
        plugin.getPluginLogger().info("info");
        sender.sendMessage(plugin.getDataDirectory());
    }
}
