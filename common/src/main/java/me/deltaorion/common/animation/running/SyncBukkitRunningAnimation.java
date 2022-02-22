package me.deltaorion.common.animation.running;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.animation.AnimationException;
import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.plugin.plugin.EPlugin;
import me.deltaorion.common.plugin.scheduler.SchedulerTask;
import net.jcip.annotations.NotThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import me.deltaorion.common.animation.MinecraftAnimation;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A sync bukkit running animation is an animation that synchronously runs with the bukkit scheduler.
 *   - All logic and control is done synchronously
 *   - All frames are rendered synchronously
 *   - The animation starts a sync runnable that is called once per tick, the runnable keeps track
 *     of the elapsed time per frame and renders frames if necessary
 *   - If the time between this frame and the next is 0, they will be rendered in a loop.
 *  - This running animation is NOT THREAD SAFE. Although methods can be called off the main thread you should
 *    take thread safety precautions. This class provides no synchronisation.
 *
 * @param <T> The information type for each frame
 * @param <S> The screen to render the information to
 */
@NotThreadSafe
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
    private float playBackSpeed;

    private final long MILLIS_PER_TICK;

    public SyncBukkitRunningAnimation(@NotNull MinecraftAnimation<T, S> animation, @NotNull EPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskId, long millisPerTick) {
        this.animation = animation;
        this.frameIterator = animation.getFrames();
        this.plugin = plugin;
        this.renderer = renderer;
        this.taskId = taskId;
        playBackSpeed = 1;
        this.MILLIS_PER_TICK = millisPerTick;
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
        this.task = plugin.getScheduler().runTaskTimer(this,0L,MILLIS_PER_TICK, TimeUnit.MILLISECONDS);
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
    public void setPlaySpeed(float modifier) {
        if(modifier==0) {
            pause();
            return;
        }
        playBackSpeed = 1/modifier;
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

    //this loop is called once per tick
    private void animate() throws AnimationException {
        //init
        if(this.currentFrame==null) {
            if(!frameIterator.hasNext()) {
                stop();
                return;
            }

            currentFrame = frameIterator.next();
        }
        //the smallest unit of time is a tick or 50ms. Thus all control should be done using ticks
        long convertedTime = toTicks(getTime(currentFrame));
        //check if it is time to render a frame
        if(timeElapsed>=convertedTime) {
            do {
                //before rendering frames check if the animation has been cancelled
                if(!this.running || this.cancelled) {
                    stop();
                    return;
                }

                //render the frames
                for (S screen : getScreens()) {
                    try {
                        renderer.render(this,Objects.requireNonNull(currentFrame), screen);
                    } catch (Throwable e) {
                        throw new AnimationException("An error occurred when running the animation on frame '" + currentFrame + "' rendering to screen '" + screen + "'", e);
                    }
                }
                timeElapsed = 0;
                //get the next frame
                if (!frameIterator.hasNext()) {
                    stop();
                    return;
                }
                currentFrame = frameIterator.next();
            } while (toTicks(getTime(currentFrame))==0);
        } else {
            //otherwise tell the animation that time has elapsed.
            //Time cannot elapse if the animation is paused
            if(!paused) {
                timeElapsed++;
            }
        }
    }

    private long getTime(MinecraftFrame<T> currentFrame) {
        return (long) (currentFrame.getTime()*playBackSpeed);
    }


    private long toTicks(long time) {
        return time / MILLIS_PER_TICK;
    }

    private void stop() {
        this.running = false;
        this.timeElapsed = 0;
        //check if we need to restart the animation
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
        //you cannot restart if cancelled.
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
        return MoreObjects.toStringHelper(this)
                .add("Task",taskId)
                .add("Running",running)
                .toString();
    }

}
