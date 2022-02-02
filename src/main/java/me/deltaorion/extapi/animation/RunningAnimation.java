package me.deltaorion.extapi.animation;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This abstract interface represents an animation that is running. A running animation will play the frames it was loaded
 * with in order they were added into the frames list. The difference in time between each frame being played is defined by
 * by each frame. If the time of the frame is 0ms then it will be played in loop immediately after the one before it.
 *
 * When a running animation is created by {@link MinecraftAnimation#start()} it will load all of the frames
 * into this running animation as they were when it was created. That means that if additional frames are added to the minecraft
 * animation it will have no affect on what is being displayed on this running animation instance.
 *
 * A running animation has several screens
 *
 * Implementations can be found in
 *  - {@link me.deltaorion.extapi.animation.factory.AnimationFactories}
 *
 * @param <S>
 */
public interface RunningAnimation<S> extends Runnable {

    /**
     * Irreversibly cancels the running animation. Once the animation has been cancelled it cannot
     * be restarted again. When cancellation occurs the RunningAnimation will stop as quickly as possible. However
     * the running animation should
     *   - allow any frames that are currently being rendered to be rendered
     *   - not display any new frames after if any that are currently being rendered fully render
     *   - ensure that the animation cannot be started again
     *   - run the renderer's {@link AnimationRenderer#beforeCompletion(RunningAnimation)} once
     */
    public void cancel();

    /**
     * @return Whether the animation is currently running or not
     */
    public boolean isRunning();

    /**
     * Starts the animation. The animation can only ever be started once. If the animation should be restarted
     * then the animation renderer should return true in {@link AnimationRenderer#beforeCompletion(RunningAnimation)).
     * To restart an animation that is currently running but has not finished one should use {@link #restart()}
     */
    public void start();

    /**
     * Restarts the animation. This will only work if the animation is currently running and has not finished. The animation
     * that is currently already running will be stopped but not cancelled. The restarted animation will use the existing set of frames it was loaded with.
     * If any new frames were added to the MinecraftAnimation it will not have any effect.
     */
    public void restart();

    /**
     * Adds a screen to the running animation. A screen i
     *
     * @param screen
     */
    public void addScreen(@NotNull S screen);

    public void removeScreen(@NotNull S screen);

    public void clearScreens();

    @NotNull
    public Collection<S> getScreens();
}
