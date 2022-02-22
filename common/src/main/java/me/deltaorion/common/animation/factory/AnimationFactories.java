package me.deltaorion.common.animation.factory;

import me.deltaorion.common.animation.AnimationFactory;
import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.animation.running.ScheduleAsyncRunningAnimation;
import me.deltaorion.common.animation.running.SleepAsyncRunningAnimation;
import me.deltaorion.common.animation.running.SyncBukkitRunningAnimation;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import org.jetbrains.annotations.NotNull;

public class AnimationFactories {

    private AnimationFactories() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates an animation factory that returns a {@link ScheduleAsyncRunningAnimation}. This animation works by using the server scheduler
     * to control the timing of animations by scheduling the next task after the frames have been rendered.
     *
     * @return an animation factory that returns new {@link ScheduleAsyncRunningAnimation}
     */
    public static AnimationFactory SCHEDULE_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T,S> renderer, long taskID) {
                return new ScheduleAsyncRunningAnimation<>(animation,plugin, renderer, taskID);
            }
        };
    }

    /**
     * Creates an animation factory that returns a {@link SleepAsyncRunningAnimation} running animation. This is an async animation that takes a thread, then uses
     * {@link Thread#sleep(long)} to control the timing of the animation. It is recommended for async animations to use {@link #SCHEDULE_ASYNC()}
     * over this implementation as this will consume a thread potentially causing performance issues.
     *
     * @return A animation factory for sleep async animations
     */
    public static AnimationFactory SLEEP_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T,S> renderer, long taskID) {
                return new SleepAsyncRunningAnimation<>(animation,plugin, renderer, taskID);
            }
        };
    }

    /**
     * Creates an animation factory that returns {@link SyncBukkitRunningAnimation}. This animation type uses the bukkit scheduler to create
     * completely sync animations. None of the timing or control is done asynchronously. The frame render is done fully synchronously with
     * the server.
     *
     * @return An animation factory that returns new sync bukkit animations.
     */
    public static AnimationFactory SYNC_SERVER(long millisPerTick) {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskID) {
                return new SyncBukkitRunningAnimation<>(animation,plugin,renderer,taskID,millisPerTick);
            }
        };
    }
}

