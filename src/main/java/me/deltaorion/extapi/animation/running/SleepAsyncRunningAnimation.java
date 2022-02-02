package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.*;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class SleepAsyncRunningAnimation<T,S> extends ScreenedRunningAnimation<S> {

    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private volatile Iterator<MinecraftFrame<T>> frameIterator;
    @GuardedBy("this") private volatile boolean running;
    @GuardedBy("this") private volatile boolean cancelled = false;
    @NotNull private final EPlugin plugin;
    @NotNull private final AnimationRenderer<T,S> animationRenderer;
    @Nullable private SchedulerTask runningTask = null;
    private final long taskID;

    public SleepAsyncRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull EPlugin plugin, @NotNull AnimationRenderer<T,S> animationRenderer, long taskID) {
        this.frameIterator = animation.getFrames();
        this.animation = animation;
        this.plugin = plugin;
        this.animationRenderer = animationRenderer;
        running = false;
        this.taskID = taskID;
    }

    @Override
    public void cancel() {

        //cant cancel something that isnt running
        synchronized (this) {
            if(this.cancelled)
                return;

            this.cancelled = true;

            if (!this.running)
                return;
        }
        halt();
        //tell the animation to cancel as soon as possible
    }

    private void halt() {
        synchronized (this) {
            if (this.runningTask != null) {
                runningTask.cancel();
            }
        }
    }

    //called once the animation has completely stopped due to cancellation
    //or otherwise
    private void stop() {
        //if something is rendering wait for that to happen first
        synchronized (this) {
            //in case this is called twice
            if (!this.running)
                return;
            this.running = false;
            this.runningTask = null;
        }
        boolean restart = false;
        try {
            restart = animationRenderer.beforeCompletion(this);
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when attempting to run beforeCompletion",e);
        }
        synchronized (this) {
            if (cancelled)
                restart = false;

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
    public synchronized boolean isRunning() {
        return this.running;
    }

    @Override
    public void start() {
        //if it is already running we cant start
        synchronized (this) {
            if (this.running)
                return;
        }

        //if the thread is interrupted to not start
        if(Thread.currentThread().isInterrupted())
            return;

        //begin the task
        synchronized (this) {
            this.runningTask = plugin.getScheduler().runTaskAsynchronously(this);
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

    @Override
    public void run() {
        //stop this from being called multiple times
        synchronized (this) {
            if (this.running)
                return;
            //start thread
            this.running = true;
        }

        try {
            animate();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Throwable e) {
            plugin.getPluginLogger().severe(e.getMessage(),e);
        } finally {
            stop();
        }
    }

    private void animate() throws InterruptedException, AnimationException {
        while(!Thread.currentThread().isInterrupted()) {
            //check each time before beginning the sleep that this hasn't been cancelled. This
            //is essential as cancelling the future wont actually interrupt the thread.
            if(cancelled || !this.running)
                return;

            if(!frameIterator.hasNext())
                return;
            //start at time 0
            MinecraftFrame<T> nextFrame = frameIterator.next();

            if(nextFrame.getTime()>0) {
                Thread.sleep(nextFrame.getTime());
            }
            //if it has been cancelled do not render the next frame
            if(cancelled || !this.running)
                return;

            Collection<S> screens = getScreens();
            for (S screen : screens) {
                try {
                    animationRenderer.render(Objects.requireNonNull(nextFrame), screen);
                } catch (Throwable e) {
                    throw new AnimationException("An error occurred whilst attempting to render this animation on frame '" + nextFrame + "' on screen '" + screen + "'", e);
                }
            }
        }
    }

}
