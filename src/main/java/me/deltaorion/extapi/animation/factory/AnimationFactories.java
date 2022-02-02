package me.deltaorion.extapi.animation.factory;

import me.deltaorion.extapi.animation.AnimationFactory;
import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.running.ScheduleAsyncRunningAnimation;
import me.deltaorion.extapi.animation.running.SleepAsyncRunningAnimation;
import me.deltaorion.extapi.animation.running.SyncBukkitRunningAnimation;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import org.jetbrains.annotations.NotNull;

public class AnimationFactories {

    public static AnimationFactory SCHEDULE_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T,S> renderer, long taskID) {
                return new ScheduleAsyncRunningAnimation<>(animation,plugin, renderer, taskID);
            }
        };
    }

    public static AnimationFactory SLEEP_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T,S> renderer, long taskID) {
                return new SleepAsyncRunningAnimation<>(animation,plugin, renderer, taskID);
            }
        };
    }

    public static AnimationFactory SYNC_BUKKIT() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T, S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T, S> renderer, long taskID) {
                return new SyncBukkitRunningAnimation<>(animation,plugin,renderer,taskID);
            }
        };
    }
}

