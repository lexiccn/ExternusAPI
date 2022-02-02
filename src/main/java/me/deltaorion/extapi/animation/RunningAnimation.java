package me.deltaorion.extapi.animation;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This abstract interface represents an animation that is running. A running animation will play the frames it was loaded
 * with in order they were added into the frames list. The difference in time between each frame being played is defined by
 * by each frame. If the time of the frame is 0ms then it will be played in loop immediately after the one before it.
 *
 * When a running animation is created by {@link MinecraftAnimation#start()} it will load all of the frames
 * into this running animation as they were when it was created. However if the animation is restarted and any changes were made
 * to the frames, the restart will reflect said changes.
 *
 * A running animation has several screens. When a frame is to be rendered. The running animation will render the frame to all
 * screens that are currently hooked to this running animation. See more at {@link AnimationRenderer}. One can immediately
 * add screens using {@link MinecraftAnimation#start(Iterable)}
 *
 * Implementations can be found in
 *  - {@link me.deltaorion.extapi.animation.factory.AnimationFactories}
 *
 * @param <S> The screen's type
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
     * Adds a screen to the running animation.
     *
     * @param screen The screen to add
     */
    public void addScreen(@NotNull S screen);

    /**
     * Removes the specified screen from the animation
     *
     * @param screen The screen to remove
     */
    public void removeScreen(@NotNull S screen);

    /**
     * Removes all screens from the animation
     */
    public void clearScreens();

    /**
     * @return Returns a collection of all the screens that are currently hooked to the animation
     */
    @NotNull
    public Collection<S> getScreens();
}
