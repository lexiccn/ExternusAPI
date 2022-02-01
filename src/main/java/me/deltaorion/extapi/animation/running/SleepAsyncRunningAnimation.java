package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.common.plugin.EPlugin;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class SleepAsyncRunningAnimation<T,S> implements RunningAnimation {

    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private final Iterator<MinecraftFrame<T>> frameIterator;
    @GuardedBy("this") private volatile boolean running;
    @NotNull private final EPlugin plugin;
    private final long taskID;

    public SleepAsyncRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull EPlugin plugin, long taskID) {
        this.frameIterator = animation.getFrames().iterator();
        this.animation = animation;
        this.plugin = plugin;
        running = false;
        this.taskID = taskID;
    }

    @Override
    public void cancel() {
        synchronized (this) {
            if (!this.running)
                return;
            this.running = false;
        }
        stop();
        Thread.currentThread().interrupt();
    }

    public synchronized void stop() {
        this.running = false;
        animation.onComplete(this);
    }

    @Override
    public synchronized boolean isAlive() {
        return this.running;
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
            if(!frameIterator.hasNext())
                return;
            //start at time 0
            MinecraftFrame<T> nextFrame = frameIterator.next();

            if(nextFrame.getTime()>0) {
                Thread.sleep(nextFrame.getTime());
            }

            Collection<S> screens = animation.getScreens();
            for(S screen : screens) {
                try {
                    animation.render(nextFrame, screen);
                } catch (Throwable e) {
                    throw new AnimationException("An error occurred whilst attempting to render this animation on frame '"+nextFrame+"' on screen '"+screen+"'",e);
                }
            }
        }
    }

}
