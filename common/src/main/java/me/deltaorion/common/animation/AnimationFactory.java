package me.deltaorion.common.animation;

import me.deltaorion.common.plugin.plugin.ApiPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A factory class that creates a new instance of a {@link AnimationRenderer}. Implementations can be found in
 * {@link me.deltaorion.common.animation.factory.AnimationFactories}
 */
public interface AnimationFactory {

    /**
     * Creates a new running animation
     *
     * @param animation the animation to play
     * @param plugin the plugin which the animation will use
     * @param renderer the thing that renders the animation
     * @param taskID a unique number to identify the task to ensure that tasks are unique and not equal
     * @param <T> The object type to be rendered
     * @param <S> The screen type to render to
     * @return A new running animation
     */
    <T,S> RunningAnimation<S> get(@NotNull MinecraftAnimation<T,S> animation, @NotNull ApiPlugin plugin, @NotNull AnimationRenderer<T,S> renderer, long taskID);
}
