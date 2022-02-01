package me.deltaorion.extapi.animation.factory;

import me.deltaorion.extapi.animation.AnimationFactory;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.running.ScheduleAsyncRunningAnimation;
import me.deltaorion.extapi.animation.running.SleepAsyncRunningAnimation;
import me.deltaorion.extapi.common.plugin.ApiPlugin;

public class AnimationFactories {

    public static AnimationFactory SCHEDULE_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation get(MinecraftAnimation<T, S> animation, ApiPlugin plugin, long taskID) {
                return new ScheduleAsyncRunningAnimation<>(animation,plugin,taskID);
            }
        };
    }

    public static AnimationFactory SLEEP_ASYNC() {
        return new AnimationFactory() {
            @Override
            public <T, S> RunningAnimation get(MinecraftAnimation<T, S> animation, ApiPlugin plugin, long taskID) {
                return new SleepAsyncRunningAnimation<>(animation,plugin,taskID);
            }
        };
    }
}

