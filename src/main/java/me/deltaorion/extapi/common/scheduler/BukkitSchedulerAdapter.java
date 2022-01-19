package me.deltaorion.extapi.common.scheduler;

import me.deltaorion.extapi.common.server.BukkitServer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class BukkitSchedulerAdapter implements SchedulerAdapter {

    private final Plugin plugin;

    public BukkitSchedulerAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,task);
    }

    @Override
    public SchedulerTask runTask(Runnable task) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTask(plugin).getTaskId();
        return runnable::cancel;
    }

    @Override
    public SchedulerTask runTaskAsynchronously(Runnable task) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTaskAsynchronously(plugin).getTaskId();
        return runnable::cancel;
    }

    @Override
    public SchedulerTask runTaskLater(Runnable task, long delay, TimeUnit unit) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTaskLater(plugin,toTicks(delay,unit)).getTaskId();
        return runnable::cancel;
    }

    @Override
    public SchedulerTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTaskLaterAsynchronously(plugin,toTicks(delay,unit)).getTaskId();
        return runnable::cancel;
    }

    @Override
    public SchedulerTask runTaskTimer(Runnable task, long delay, long period, TimeUnit unit) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTaskTimer(plugin,toTicks(delay,unit),toInterval(period,unit)).getTaskId();
        return runnable::cancel;
    }

    @Override
    public SchedulerTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit) {
        BukkitRunnable runnable = toBukkitRunnable(task);
        runnable.runTaskTimerAsynchronously(plugin,toTicks(delay,unit),toInterval(period,unit)).getTaskId();
        return runnable::cancel;
    }

    private BukkitRunnable toBukkitRunnable(Runnable runnable) {
        if(runnable instanceof BukkitRunnable) {
            return (BukkitRunnable) runnable;
        }

        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private long toTicks(long delay, TimeUnit unit) {
        long ms = TimeUnit.MILLISECONDS.convert(delay,unit);
        return ms/ BukkitServer.MILLIS_PER_TICK;
    }

    private long toInterval(long interval, TimeUnit unit) {
        return Math.max(1,toTicks(interval,unit));
    }
}

