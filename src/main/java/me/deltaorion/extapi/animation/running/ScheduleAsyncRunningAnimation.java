package me.deltaorion.extapi.animation.running;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ScheduleAsyncRunningAnimation<T,S> extends ScreenedRunningAnimation<S> {

    @NotNull private volatile Iterator<MinecraftFrame<T>> frameIterator;
    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private final ApiPlugin plugin;
    @NotNull private final AtomicReference<SchedulerTask> currentTask = new AtomicReference<>();
    @NotNull private final AnimationRenderer<T,S> renderer;
    @GuardedBy("this") private volatile boolean running;
    @GuardedBy("this") private volatile boolean cancelled = false;
    @NotNull private CountDownLatch pauseLatch = new CountDownLatch(1);
    @NotNull private final Object pauseLock = new Object();
    @GuardedBy("this") private volatile boolean paused = false;
    @NotNull private final Object runningLock = new Object();
    @GuardedBy("this") private boolean executing = false;
    private final AtomicDouble playBackSpeed;
    private final long taskID;

    public ScheduleAsyncRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskID) {
        this.animation = Objects.requireNonNull(animation);
        this.frameIterator = Objects.requireNonNull(animation.getFrames());
        this.plugin = Objects.requireNonNull(plugin);
        this.renderer = renderer;
        this.running = false;
        this.taskID = taskID;
        this.playBackSpeed = new AtomicDouble(1);
    }

    @Override
    public void cancel() {
        synchronized (this) {
            //do not cancel if it is currently being cancelled
            if(this.cancelled)
                return;
            //do not cancel if it is not running
            this.cancelled = true;
            forcePlay();
            synchronized (runningLock) {
                if(!this.executing && running) {
                    //halting will delete the task from existence
                    halt();
                    stop();
                }
            }
        }
    }

    private void halt() {
        //If a task has been scheduled it will no longer be scheduled,
        //thus we must use the stop function here
        synchronized (this) {
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
        }

        if(this.cancelled || Thread.currentThread().isInterrupted()) {
            animation.onComplete(this);
            return;
        }

        currentTask.set(plugin.getScheduler().runTaskAsynchronously(this));
    }

    @Override
    public void pause() {
        synchronized (this) {
            //if the animation has been cancelled then do nothing
            if(this.cancelled)
                return;
        }
        synchronized (pauseLock) {
            if(this.paused)
                return;

            paused = true;
            pauseLatch = new CountDownLatch(1);
        }

    }

    @Override
    public void play() {
        synchronized (this) {
            if(this.cancelled || !this.running)
                return;
        }
        forcePlay();
    }

    @Override
    public void setPlaySpeed(float modifier) {
        Preconditions.checkArgument(modifier>=0);
        if(modifier==0) {
            pause();
            return;
        }

        playBackSpeed.set(1/modifier);
    }

    private void forcePlay() {
        synchronized (pauseLock) {
            if(!paused)
                return;

            this.paused = false;
            if(this.pauseLatch.getCount()>0) {
                pauseLatch.countDown();
            }
        }
    }

    private boolean isPaused() {
        synchronized (pauseLock) {
            return this.paused;
        }
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
        synchronized (this) {
            this.running = false;
            this.currentTask.set(null);
            this.executing = false;
        }
        //drop the lock before cleanup
        boolean restart = false;
        try {
            restart = renderer.beforeCompletion(this);
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when attempting to run beforeCompletion",e);
        }
        synchronized (this) {
            if (this.cancelled) {
                restart = false;
            }

            if(!restart)
                cancelled = true;
        }
        if(restart) {
            frameIterator = animation.getFrames();
            start();
        } else {
            animation.onComplete(this);
        }
    }

    @Override
    public void run() {

        synchronized (this) {
            if (this.running)
                return;
            this.running = true;
        }

        synchronized (runningLock) {
            this.executing = true;
        }

        if(!frameIterator.hasNext()) {
            stop();
            return;
        }

        if(this.cancelled || Thread.currentThread().isInterrupted()) {
            stop();
            return;
        }

        synchronized (runningLock) {
            this.executing = false;
        }

        MinecraftFrame<T> frame = frameIterator.next();
        this.currentTask.set(plugin.getScheduler().runTaskLaterAsynchronously(new AnimationRunnable<>(
                frame,
                animation,
                frameIterator,
                this,
                plugin
        ),getTime(frame),TimeUnit.MILLISECONDS));
    }

    private int getTime(@NotNull MinecraftFrame<?> frame) {
        return (int) (frame.getTime()*playBackSpeed.get());
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

            synchronized (runningAnimation.runningLock) {
                runningAnimation.executing = true;
            }

            do {
                //check for any kind of cancellation before rendering the next frames
                if (!runningAnimation.running || runningAnimation.cancelled || Thread.currentThread().isInterrupted()) {
                    runningAnimation.stop();
                    return;
                }

                for (S screen : runningAnimation.getScreens()) {
                    try {
                        runningAnimation.renderer.render(runningAnimation,Objects.requireNonNull(frame), screen);
                    } catch (Throwable e) {
                        throw new AnimationException("An error occurred whilst attempting to render this animation on frame '" + frame + "' on screen '" + screen + "'", e);
                    }
                }
                if (!frameIterator.hasNext()) {
                    runningAnimation.stop();
                    return;
                }

                frame = frameIterator.next();

            } while (frame.getTime() == 0);

            //check before scheduling next task
            if(runningAnimation.isPaused()) {
                try {
                    runningAnimation.pauseLatch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    runningAnimation.stop();
                    return;
                }
            }

            if (!runningAnimation.running || runningAnimation.cancelled) {
                runningAnimation.stop();
                return;
            }

            synchronized (runningAnimation.runningLock) {
                runningAnimation.executing = false;
            }

            runningAnimation.currentTask.set(plugin.getScheduler().runTaskLaterAsynchronously(new AnimationRunnable<>(
                    frame,
                    animation,
                    frameIterator,
                    runningAnimation,
                    plugin
            ), runningAnimation.getTime(frame), TimeUnit.MILLISECONDS));

        }

    }
}
