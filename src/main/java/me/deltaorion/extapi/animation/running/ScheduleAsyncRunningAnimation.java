package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ScheduleAsyncRunningAnimation<T,S> implements RunningAnimation {

    @NotNull private final Iterator<MinecraftFrame<T>> frameIterator;
    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private final ApiPlugin plugin;
    @NotNull private final AtomicReference<SchedulerTask> currentTask = new AtomicReference<>();
    @GuardedBy("this") private boolean running;
    private final long taskID;

    public ScheduleAsyncRunningAnimation(@NotNull MinecraftAnimation<T,S> animation , @NotNull ApiPlugin plugin, long taskID) {
        this.animation = Objects.requireNonNull(animation);
        this.frameIterator = Objects.requireNonNull(animation.getFrames().iterator());
        this.plugin = Objects.requireNonNull(plugin);
        this.running = false;
        this.taskID = taskID;
    }

    @Override
    public synchronized void cancel() {
        if(!this.running)
            return;

        if (this.currentTask.get() != null) {
            currentTask.get().cancel();
        }
        stop();
    }

    @Override
    public synchronized boolean isAlive() {
        return running;
    }

    @Override
    public void start() {
        synchronized (this) {
            if (this.running)
                return;
        }

        if(Thread.currentThread().isInterrupted())
            return;

        plugin.getScheduler().runTaskAsynchronously(this);
    }


    @Override
    public void run() {

        if(Thread.currentThread().isInterrupted())
            return;

        synchronized (this) {
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

        plugin.getScheduler().runTaskLaterAsynchronously(new AnimationRunnable<>(
                frame,
                animation,
                frameIterator,
                this,
                plugin
        ),frame.getTime(),TimeUnit.MILLISECONDS);
    }

    private synchronized void stop() {
        this.currentTask.set(null);
        this.running = false;
        this.animation.onComplete(this);
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
                runningAnimation.stop();
                plugin.getPluginLogger().severe(e.getMessage(),e);
            }
        }

        private void animate() throws AnimationException {

            MinecraftFrame<T> frame = this.frame;

            do {
                for (S screen : animation.getScreens()) {
                    try {
                        animation.render(frame, screen);
                    } catch (Throwable e) {
                        throw new AnimationException("An error occurred whilst attempting to render this animation on frame '" + frame + "' on screen '" + screen + "'", e);
                    }
                }

                if (!frameIterator.hasNext()) {
                    runningAnimation.stop();
                    return;
                }

                if (Thread.currentThread().isInterrupted()) {
                    runningAnimation.stop();
                    return;
                }

                if (!runningAnimation.running) {
                    return;
                }

                frame = frameIterator.next();

            } while(frame.getTime()==0);

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
