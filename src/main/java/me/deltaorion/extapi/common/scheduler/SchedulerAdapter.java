package me.deltaorion.extapi.common.scheduler;

import java.util.concurrent.TimeUnit;

public interface SchedulerAdapter {

    void scheduleSyncDelayedTask(Runnable task);

    SchedulerTask runTask(Runnable task);

    SchedulerTask runTaskAsynchronously(Runnable task);

    SchedulerTask runTaskLater(Runnable task, long delay, TimeUnit unit);

    SchedulerTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit);

    SchedulerTask runTaskTimer(Runnable task, long delay, long period, TimeUnit unit);

    SchedulerTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit);
}
