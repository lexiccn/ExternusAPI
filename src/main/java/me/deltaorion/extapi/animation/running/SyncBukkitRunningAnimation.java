package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.AnimationException;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import me.deltaorion.extapi.common.server.BukkitServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SyncBukkitRunningAnimation<T,S> implements RunningAnimation {

    private boolean running = false;
    @NotNull private final MinecraftAnimation<T,S> animation;
    @NotNull private final Iterator<MinecraftFrame<T>> frameIterator;
    @NotNull private final EPlugin plugin;
    @Nullable private SchedulerTask task;
    @Nullable private MinecraftFrame<T> currentFrame = null;
    private int timeElapsed = 0;

    public SyncBukkitRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull EPlugin plugin) {
        this.animation = animation;
        this.frameIterator = animation.getFrames().iterator();
        this.plugin = plugin;
    }

    @Override
    public void cancel() {
        stop();
    }

    @Override
    public boolean isAlive() {
        return this.running;
    }

    @Override
    public void start() {
        if (this.running)
            return;
        this.running = true;
        plugin.getScheduler().runTaskTimer(this,0L,BukkitServer.MILLIS_PER_TICK, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if(Thread.currentThread().isInterrupted())
            return;

        if(!this.running)
            throw new IllegalStateException("Sync animation still calling run despite the animation being cancelled!");

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
                for (S screen : animation.getScreens()) {
                    try {
                        animation.render(currentFrame, screen);
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
            timeElapsed++;
        }
    }

    private long toTicks(long time) {
        return time / BukkitServer.MILLIS_PER_TICK;
    }

    private void stop() {
        this.running = false;
        this.task.cancel();
        this.task = null;
    }
}
