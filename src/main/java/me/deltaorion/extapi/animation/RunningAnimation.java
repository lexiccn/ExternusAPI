package me.deltaorion.extapi.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
 * Current Implementations
 *  - {@link me.deltaorion.extapi.animation.running.ScheduleAsyncRunningAnimation}
 *  - {@link me.deltaorion.extapi.animation.running.SleepAsyncRunningAnimation}
 *  - {@link me.deltaorion.extapi.animation.running.SyncBukkitRunningAnimation}
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
     * Adds a screen to the running animation. If a screen function is set this will do nothing
     *
     * @param screen The screen to add
     */
    public void addScreen(@NotNull S screen);

    /**
     * Removes the specified screen from the animation. If a screen function is set this will do nothing.
     *
     * @param screen The screen to remove
     */
    public void removeScreen(@NotNull S screen);

    /**
     * Removes all screens from the animation
     */
    public void clearScreens();


    /**
     * Instead of the animation keeping its own list of screen. The animation retrieves its screens
     * from a predetermined function. This is very useful if you want to avoid memory leaks or managing
     * the storage of screens on this animation.
     *
     * if this is set then
     *   - the existing list of screens will be cleared
     *   - no new screens can be added or removed
     *
     *  If set to null
     *    - the screen function will be removed.
     *    - screens will be retrieved from a list of screens instead
     *
     * @param screenFunction the function used to retrieve all the screens
     */
    public void setScreenFunction(@Nullable Supplier<Collection<S>> screenFunction);

    default void removeScreenFunction() {
        setScreenFunction(null);
    }

    public boolean hasScreenFunction();

    /**
     * @return Returns a collection of all the screens that are currently hooked to the animation
     */
    @NotNull
    public Collection<S> getScreens();

    /**
     * Pauses the animation. The animation can be played again with {@link #play()}
     *    - If the animation has been cancelled this will not do anything.
     *    - The animation will pause as soon as it can, any frames that are currently being rendered will render first
     *    - While paused the animation can still cancel
     *
     *    Note if the animation is paused it will be your responsibility to ensure that the animation is shutdown
     *    correctly when the server shuts down. You should not leave paused animations hanging on server shutdown!
     */
    public void pause();

    /**
     * Unpauses the animation. The animation can be paused with {@link #pause()}
     *   - If the animation has been cancelled this will not do anything.
     *   - The animation will resume as soon as possible.
     */
    public void play();
}
