package me.deltaorion.extapi.display.actionbar.running;

import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import me.deltaorion.extapi.display.actionbar.ActionBar;
import me.deltaorion.extapi.display.actionbar.ActionBarManager;
import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.actionbar.RunningActionBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduleRunningActionBar implements RunningActionBar {

    @NotNull
    private final ActionBar actionBar;
    @GuardedBy("this") private Object[] args;

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final BukkitApiPlayer player;
    private final ActionBarManager manager;
    private final ActionBarRenderer renderer;

    private AtomicBoolean dodgyRenderer = new AtomicBoolean(false);

    private long timeCounter;

    @GuardedBy("this") private volatile boolean cancelled = false;
    @GuardedBy("this") @Nullable private SchedulerTask runningTask;
    @NotNull private final CountDownLatch finishLatch;

    private final long INTERVAL = Duration.of(2, ChronoUnit.SECONDS).toMillis();
    private final long PERFECT = Duration.of(3,ChronoUnit.SECONDS).toMillis();
    private final Message BLANK = Message.valueOf("");

    public ScheduleRunningActionBar(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, ActionBarManager manager, ActionBarRenderer renderer) {
        this.actionBar = actionBar;
        this.plugin = plugin;
        this.player = player;
        this.args = args;
        this.manager = manager;
        this.renderer = renderer;
        this.finishLatch = new CountDownLatch(1);
    }

    @Override
    public void start() {
        if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
            throw new MissingDependencyException("Cannot Render action bar as dependency '"+BukkitAPIDepends.PROTOCOL_LIB.getName() +"' is missing!");

        synchronized (this) {
            if(isRunning() || this.cancelled)
                return;

            timeCounter = actionBar.getTime();
            runningTask = plugin.getScheduler().runTaskAsynchronously(runnable);
        }
    }

    @Override
    public void cancel(boolean overwrite) {
        synchronized (this) {
            //cant cancel if already cancelled
            if(this.cancelled)
                return;

            this.cancelled = true;
            if(!isRunning())
                return;
        }
        halt();
        if(!overwrite) {
            clear();
        }
        stop();
    }

    private void halt() {
        synchronized (this) {
            if(this.runningTask!=null) {
                this.runningTask.cancel();
            }
        }
    }

    private void stop() {
        synchronized (this) {
            if(!this.isRunning())
                return;

            this.runningTask = null;
            this.cancelled = true;
        }
        this.manager.removeActionBar();
        finishLatch.countDown();
        //notify that this has stopped
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                if(cancelled || !isRunning() || Thread.currentThread().isInterrupted()) {
                    stop();
                    return;
                }
            }

            render();

            if(timeCounter<=PERFECT) {
                scheduleCancel(timeCounter);
                return;
            }
            long timeCopy = timeCounter;
            timeCopy = timeCopy-INTERVAL;
            if(timeCopy<PERFECT) {
                long diff = timeCounter-PERFECT;
                timeCounter = PERFECT;
                scheduleNext(diff);
            } else {
                timeCounter = timeCopy;
                scheduleNext(INTERVAL);
            }
        }
    };

    private void scheduleCancel(long cancelWhen) {
        if(cancelWhen<0)
            throw new IllegalArgumentException("Cannot cancel in less than 0 millis! Received '"+cancelWhen+"'");

        synchronized (this) {
            if(this.cancelled || Thread.currentThread().isInterrupted()) {
                return;
            }

            runningTask = plugin.getScheduler().runTaskLaterAsynchronously(new Runnable() {
                @Override
                public void run() {
                    clear();
                    stop();
                }
            },cancelWhen,TimeUnit.MILLISECONDS);
        }

    }

    private void clear() {
        renderText(BLANK);
    }

    private void scheduleNext(long whenNext) {
        if(whenNext<0)
            throw new IllegalArgumentException("Cannot schedule for less than 0 second!");
        synchronized (this) {
            if(this.cancelled || Thread.currentThread().isInterrupted()) {
                return;
            }

            runningTask = plugin.getScheduler().runTaskLaterAsynchronously(runnable,whenNext,TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return runningTask!=null;
    }

    private void render() {
        renderText(actionBar.getMessage(),args);
    }

    private void renderText(Message message, Object... args) {
        try {
            //all renders must be done in order through the render lock to stop race conditions
            renderer.render(player,message,args);
        } catch (Throwable e) {
            if(!dodgyRenderer.get()) {
                plugin.getPluginLogger().severe("An error occurred when rendering action bar '" + actionBar + "'", e);
                dodgyRenderer.set(true);
            }
            cancel(true);
        }
    }

    @NotNull @Override
    public ActionBar getActionBar() {
        return actionBar;
    }

    @Override
    public void setArgs(Object... args) {
        synchronized (this) {
            if(this.cancelled || !this.isRunning())
                return;

            this.args = args;
        }
        render();
    }

    @Override
    public CountDownLatch getFinishLatch() {
        return finishLatch;
    }
}
