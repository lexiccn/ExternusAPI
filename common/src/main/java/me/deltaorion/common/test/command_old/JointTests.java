package me.deltaorion.common.test.command_old;

import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.EPlugin;
import me.deltaorion.common.plugin.scheduler.SchedulerTask;
import me.deltaorion.common.plugin.EServer;

import java.util.concurrent.TimeUnit;

public class JointTests {

    public static void serverTest(EServer eServer, EPlugin plugin, Sender sender) {
        plugin.getScheduler().runTaskLater(() -> sender.sendMessage("This happens 1 second later"),1, TimeUnit.SECONDS);

        plugin.getScheduler().runTaskAsynchronously(() -> sender.sendMessage("This runs async"));

        SchedulerTask schedulerTask =  plugin.getScheduler().runTaskTimer(() -> sender.sendMessage("This will happen 3 times with an interval of 1 second"),3L,1L,TimeUnit.SECONDS);

        plugin.getScheduler().runTaskLaterAsynchronously(schedulerTask::cancel,6L,TimeUnit.SECONDS);

        sender.sendMessage("Version: " + eServer.getServerVersion());
        sender.sendMessage("Brand: " + eServer.getServerBrand());
        sender.sendMessage("Online Players: " + eServer.getOnlinePlayers());
        sender.sendMessage("Max-Player: " + eServer.getMaxPlayer());
        sender.sendMessage("Player Online: " + eServer.isPlayerOnline(sender.getUniqueId()));
        sender.sendMessage("Grabbing Console ... - Check Console");
        Sender console = eServer.getConsoleSender();
        console.sendMessage("Hello World!");
    }

}
