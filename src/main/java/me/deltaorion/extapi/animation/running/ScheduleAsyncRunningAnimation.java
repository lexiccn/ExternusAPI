package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ScheduleAsyncRunningAnimation<T,S> extends ScreenedRunningAnimation<S> {

    @NotNull private volatile Iterator<MinecraftFrame<T>> frameIterator;
    @NotNull private final Collection<MinecraftFrame<T>> frames;
    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private final ApiPlugin plugin;
    @NotNull private final AtomicReference<SchedulerTask> currentTask = new AtomicReference<>();
    @NotNull private final AnimationRenderer<T,S> renderer;
    @GuardedBy("this") private volatile boolean running;
    @GuardedBy("this") private volatile boolean cancelled = false;
    @GuardedBy("this") private volatile boolean restarting = false;
    private final Object renderLock = new Object();
    private final Object restartLock = new Object();
    private final long taskID;

    public ScheduleAsyncRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskID) {
        this.animation = Objects.requireNonNull(animation);
        this.frames = animation.getFrames();
        this.frameIterator = Objects.requireNonNull(animation.getFrames().iterator());
        this.plugin = Objects.requireNonNull(plugin);
        this.renderer = renderer;
        this.running = false;
        this.taskID = taskID;
    }

    @Override
    public void cancel() {
        synchronized (this) {
            //do not cancel if it is currently being cancelled
            if(this.cancelled)
                return;
            //do not cancel if it is not running

            this.cancelled = true;
            //atomic do if not absent
            if(!this.running)
                return;
        }
        halt();
        stop();
    }

    private void halt() {
        synchronized (currentTask) {
            //If a task has been scheduled it will no longer be scheduled,
            //thus we must use the stop function here
            if (currentTask.get() != null) {
                currentTask.get().cancel();
            }
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        synchronized (this) {
            if (this.running)
                return;

            if(this.cancelled)
                return;
        }

        if(Thread.currentThread().isInterrupted())
            return;

        currentTask.set(plugin.getScheduler().runTaskAsynchronously(this));
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Task",taskID)
                .add("Running",running)
                .add("Cancelled",cancelled)
                .toString();
    }

    private void stop() {
        //Wait until any frames that are being rendered are fully rendered before running
        //the on complete
        synchronized (renderLock) {
            synchronized (this) {
                //this could theoretically be called twice
                if (!this.running)
                    return;

                this.currentTask.set(null);
                this.running = false;
            }
        }
        //drop the lock before cleanup
        boolean restart = false;
        try {
            restart = renderer.beforeCompletion(this);
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when attempting to run beforeCompletion",e);
        }
        synchronized (this) {
            if (restarting)
                restart = true;

            if (this.cancelled)
                restart = false;

            if(!restart)
                cancelled = true;
        }

        this.animation.onComplete(this,restart);
    }

    @Override
    public void restart() {
        synchronized (restartLock) {
            if (this.running) {
                halt();
                restarting = true;
                stop();
                restarting = false;
            }

            this.frameIterator = frames.iterator();
            start();
        }
    }

    @Override
    public void run() {

        if(Thread.currentThread().isInterrupted())
            return;

        synchronized (this) {
            if(this.cancelled)
                return;

            if (this.running)
                return;
            this.running = true;
        }

        if(!frameIterator.hasNext()) {
            synchronized (this) {
                this.running = false;
            }
            return;
        }

        MinecraftFrame<T> frame = frameIterator.next();

        this.currentTask.set(plugin.getScheduler().runTaskLaterAsynchronously(new AnimationRunnable<>(
                frame,
                animation,
                frameIterator,
                this,
                plugin
        ),frame.getTime(),TimeUnit.MILLISECONDS));
    }

    private static class AnimationRunnable<T,S> implements Runnable {
        private final MinecraftFrame<T> frame;
        private final MinecraftAnimation<T,S> animation;
        private final Iterator<MinecraftFrame<T>> frameIterator;
        private final ScheduleAsyncRunningAnimation<T,S> runningAnimation;
        private final ApiPlugin plugin;

        private AnimationRunnable(MinecraftFrame<T> frame, MinecraftAnimation<T, S> animation, Iterator<MinecraftFrame<T>> frameIterator, ScheduleAsyncRunningAnimation<T, S> runningAnimation, ApiPlugin plugin) {
            this.frame = frame;
            this.animation = animation;
            this.frameIterator = frameIterator;
            this.runningAnimation = runningAnimation;
            this.plugin = plugin;
        }

        @Override
        public void run() {

            try {
                animate();
            } catch (Throwable e) {
                plugin.getPluginLogger().severe(e.getMessage(),e);
                runningAnimation.stop();
            }
        }

        private void animate() throws AnimationException {

            MinecraftFrame<T> frame = this.frame;

            do {
                //check for any kind of cancellation before rendering the next frames
                if (Thread.currentThread().isInterrupted()) {
                    runningAnimation.stop();
                    return;
                }

                if (!runningAnimation.running || runningAnimation.cancelled) {
                    runningAnimation.stop();
                    return;
                }

                synchronized (runningAnimation.renderLock) {
                    //if the cancellation occurs before this
                    if(!runningAnimation.running || runningAnimation.cancelled)
                        return;

                    for (S screen : runningAnimation.getScreens()) {
                        try {
                            runningAnimation.renderer.render(frame, screen);
                        } catch (Throwable e) {
                            throw new AnimationException("An error occurred whilst attempting to render this animation on frame '" + frame + "' on screen '" + screen + "'", e);
                        }
                    }
                }

                if (!frameIterator.hasNext()) {
                    runningAnimation.stop();
                    return;
                }

                frame = frameIterator.next();

            } while (frame.getTime() == 0);

            runningAnimation.currentTask.set(plugin.getScheduler().runTaskLaterAsynchronously(new AnimationRunnable<>(
                    frame,
                    animation,
                    frameIterator,
                    runningAnimation,
                    plugin
            ), frame.getTime(), TimeUnit.MILLISECONDS));

        }

    }
}
