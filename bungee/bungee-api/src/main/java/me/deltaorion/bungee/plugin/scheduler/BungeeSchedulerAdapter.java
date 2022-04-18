package me.deltaorion.bungee.plugin.scheduler;

import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.scheduler.SchedulerTask;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class BungeeSchedulerAdapter implements SchedulerAdapter {

    private final Plugin bungeePlugin;

    public BungeeSchedulerAdapter(Plugin bungeePlugin) {
        this.bungeePlugin = bungeePlugin;
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task) {
        bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin,task);
    }

    @Override
    public SchedulerTask runTask(Runnable task) {
        return runTaskAsynchronously(task);
    }

    @Override
    public SchedulerTask runTaskAsynchronously(Runnable task) {
        final ScheduledTask sTask = bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin,task);
        return sTask::cancel;
    }

    @Override
    public SchedulerTask runTaskLater(Runnable task, long delay, TimeUnit unit) {
        return runTaskLaterAsynchronously(task,delay,unit);
    }

    @Override
    public SchedulerTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit) {
        final ScheduledTask sTask = bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,unit);
        return sTask::cancel;
    }

    @Override
    public SchedulerTask runTaskTimer(Runnable task, long delay, long period,TimeUnit unit) {
        return runTaskTimerAsynchronously(task,delay,period,unit);
    }

    @Override
    public SchedulerTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit) {
        final ScheduledTask sTask =  bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,period,unit);
        return sTask::cancel;
    }
}
