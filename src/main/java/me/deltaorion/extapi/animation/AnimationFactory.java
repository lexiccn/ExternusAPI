package me.deltaorion.extapi.animation;

import me.deltaorion.extapi.common.plugin.ApiPlugin;

public interface AnimationFactory {

    public <T,S> RunningAnimation<S> get(MinecraftAnimation<T,S> animation, ApiPlugin plugin,AnimationRenderer<T,S> renderer, long taskID);
}
