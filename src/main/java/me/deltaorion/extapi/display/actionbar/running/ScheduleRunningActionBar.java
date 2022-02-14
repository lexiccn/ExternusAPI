package me.deltaorion.extapi.display.actionbar.running;

import com.google.common.base.MoreObjects;
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
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScheduleRunningActionBar implements RunningActionBar {

    @NotNull
    private final ActionBar actionBar;

    @NotNull private final BukkitPlugin plugin;
    @NotNull private final BukkitApiPlayer player;
    @NotNull private final ActionBarManager manager;
    @NotNull private final ActionBarRenderer renderer;

    @NotNull private final AtomicBoolean dodgyRenderer = new AtomicBoolean(false);

    private long timeCounter;

    @GuardedBy("this") private volatile boolean cancelled = false;
    @GuardedBy("this") @Nullable private SchedulerTask runningTask;
    @NotNull private final CountDownLatch finishLatch;

    @NotNull private String toRender;

    private final long INTERVAL = Duration.of(2, ChronoUnit.SECONDS).toMillis();
    private final long PERFECT = Duration.of(3,ChronoUnit.SECONDS).toMillis();
    @NotNull private final String BLANK = "";

    public ScheduleRunningActionBar(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull ActionBarRenderer renderer) {
        this.actionBar = Objects.requireNonNull(actionBar);
        this.plugin = Objects.requireNonNull(plugin);
        this.player = Objects.requireNonNull(player);
        this.toRender = getToRender(args);
        this.manager = Objects.requireNonNull(manager);
        this.renderer = Objects.requireNonNull(renderer);
        this.finishLatch = new CountDownLatch(1);
    }

    @Override
    public void start() {
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
            //if it isn't running then the task has already been halted
            if(!isRunning())
                return;
        }
        halt();
        //if not overwriting then don't bother with clearing so the next can be played seamlessly
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
        //alert manager to avoid memory leak
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
                render();
            }
            //an action bar will fade and render perfectly if it lasts for 3 seconds.
            //However if the duration is less than 3 seconds that can never be guaranteed. A blank
            //screen must be played to give the illusion of lowered duration
            if(timeCounter<=PERFECT) {
                scheduleCancel(timeCounter);
                return;
            }
            long timeCopy = timeCounter;
            timeCopy = timeCopy-INTERVAL;
            //this here adjusts to make sure that we always end off with the perfect amount
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
            //if it has been cancelled dont schedule
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
        renderText(toRender);
    }

    @NotNull
    private String getToRender(Object... args) {
        return actionBar.getMessage().toString(player.getLocale(),args);
    }

    private void renderText(@NotNull String toRender) {
        try {
            renderer.render(player,toRender);
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

            this.toRender = getToRender(args);
        }
        render();
    }

    @NotNull
    @Override
    public CountDownLatch getFinishLatch() {
        return finishLatch;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Action-Bar",actionBar)
                .add("Running",isRunning())
                .add("Cancelled",cancelled)
                .add("as-displayed", toRender)
                .toString();
    }
}
