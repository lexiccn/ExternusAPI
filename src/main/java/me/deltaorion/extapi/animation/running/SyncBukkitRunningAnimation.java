package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import me.deltaorion.extapi.common.server.BukkitServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SyncBukkitRunningAnimation<T,S> extends ScreenedRunningAnimation<S> {

    private boolean running = false;
    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private Iterator<MinecraftFrame<T>> frameIterator;
    @NotNull private final EPlugin plugin;
    @NotNull private final AnimationRenderer<T,S> renderer;
    @Nullable private SchedulerTask task;
    @Nullable private MinecraftFrame<T> currentFrame = null;
    private boolean paused = false;
    private boolean cancelled = false;
    private final long taskId;
    private int timeElapsed = 0;

    public SyncBukkitRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull EPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskId) {
        this.animation = animation;
        this.frameIterator = animation.getFrames();
        this.plugin = plugin;
        this.renderer = renderer;
        this.taskId = taskId;
    }

    @Override
    public void cancel() {
        this.cancelled = true;
        stop();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void start() {
        if (this.running || this.cancelled)
            return;

        this.running = true;
        this.task = plugin.getScheduler().runTaskTimer(this,0L,BukkitServer.MILLIS_PER_TICK, TimeUnit.MILLISECONDS);
    }

    @Override
    public void pause() {
        this.paused = true;
    }

    @Override
    public void play() {
        this.paused = false;
    }

    @Override
    public void run() {
        if(Thread.currentThread().isInterrupted())
            return;

        if(!this.running || this.cancelled)
            return;

        try {
            animate();
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when running a sync bukkit animation",e);
            stop();
        }

    }

    private void animate() throws AnimationException {
        //init
        if(this.currentFrame==null) {
            if(!frameIterator.hasNext()) {
                stop();
                return;
            }

            currentFrame = frameIterator.next();
        }

        long convertedTime = toTicks(currentFrame.getTime());
        if(timeElapsed==convertedTime) {
            do {
                if(!this.running || this.cancelled) {
                    stop();
                    return;
                }

                for (S screen : getScreens()) {
                    try {
                        renderer.render(this,Objects.requireNonNull(currentFrame), screen);
                    } catch (Throwable e) {
                        throw new AnimationException("An error occurred when running the animation on frame '" + currentFrame + "' rendering to screen '" + screen + "'", e);
                    }
                }
                timeElapsed = 0;
                if (!frameIterator.hasNext()) {
                    stop();
                    return;
                }
                currentFrame = frameIterator.next();
            } while (toTicks(currentFrame.getTime())==0);
        } else {
            if(!paused) {
                timeElapsed++;
            }
        }
    }

    private long toTicks(long time) {
        return time / BukkitServer.MILLIS_PER_TICK;
    }

    private void stop() {
        this.running = false;
        this.timeElapsed = 0;
        if(this.task!=null) {
            this.task.cancel();
        }
        this.task = null;
        boolean restart = false;
        try {
            restart = renderer.beforeCompletion(this);
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when attempting to run beforeCompletion",e);
        }

        if(cancelled)
            restart = false;

        if(!restart)
            cancelled = true;

        if(restart) {
            frameIterator = animation.getFrames();
            start();
        } else {
            animation.onComplete(this);
        }
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Task",taskId)
                .add("Running",running)
                .toString();
    }

}
