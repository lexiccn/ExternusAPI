package me.deltaorion.extapi.common.scheduler;

import net.md_5.bungee.api.plugin.Plugin;

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
        final int taskID = bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin,task).getId();
        return () -> { bungeePlugin.getProxy().getScheduler().cancel(taskID); };
    }

    @Override
    public SchedulerTask runTaskAsynchronously(Runnable task) {
        final int taskID = bungeePlugin.getProxy().getScheduler().runAsync(bungeePlugin,task).getId();
        return () -> { bungeePlugin.getProxy().getScheduler().cancel(taskID); };
    }

    @Override
    public SchedulerTask runTaskLater(Runnable task, long delay, TimeUnit unit) {
        final int taskID = bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,unit).getId();
        return () -> { bungeePlugin.getProxy().getScheduler().cancel(taskID); };
    }

    @Override
    public SchedulerTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit) {
        final int taskID = bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,unit).getId();
        return () -> { bungeePlugin.getProxy().getScheduler().cancel(taskID); };
    }

    @Override
    public SchedulerTask runTaskTimer(Runnable task, long delay, long period,TimeUnit unit) {
        final int taskID =  bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,period,unit).getId();
        return () -> {bungeePlugin.getProxy().getScheduler().cancel(taskID);};
    }

    @Override
    public SchedulerTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit) {
        final int taskID = bungeePlugin.getProxy().getScheduler().schedule(bungeePlugin,task,delay,period,unit).getId();
        return () -> {bungeePlugin.getProxy().getScheduler().cancel(taskID); };
    }
}
