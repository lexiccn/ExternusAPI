package me.deltaorion.extapi.animation;

import me.deltaorion.extapi.common.plugin.ApiPlugin;

public interface AnimationFactory {

    public <T,S> RunningAnimation get(MinecraftAnimation<T,S> animation, ApiPlugin plugin, long taskID);
}
