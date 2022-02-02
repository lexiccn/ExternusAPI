package me.deltaorion.extapi.animation;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;



/**
 * Represents an abstract playable animation in Minecraft. An animation can create motion in the world. This is achieved by
 * playing multiple frames one after another and rendering said frames to the world.
 *
 * Each {@link MinecraftFrame} contains information about what the next state should look like. Then {@link AnimationRenderer} should then take that information
 * and depending on that information render it to the screen accordingly.
 *
 * This class is not an animation that can be played, paused or cancelled but is rather a manager for running animations. This class
 * stores the frames and when required can be used to create animations with {@link #get(Object)} or {@link #start()}. These animations
 * are then played rendering the frames one after the next.
 *
 * The timing on frames is relative. Frames are played in the order in which they are added. The next frame will be played x milliseconds
 * after the previous
 *
 * Cancel all animations with {@link #stopAll()}
 * Start a new with {@link #start(Object)} )}
 * Add frames with {@link #addFrame(MinecraftFrame)}
 *
 * @param <T> The type that represents the information to be rendered
 * @param <S> the type of the screen to render the information to
 */
public class MinecraftAnimation<T,S> {
    @GuardedBy("this") private final List<MinecraftFrame<T>> frames; //will receive a lot of writing at the start, then only read,
    @GuardedBy("this") private final List<RunningAnimation<S>> runningAnimations; //will be written to and seldom read
    private final ApiPlugin plugin;
    private final AnimationFactory factory;
    private final AnimationRenderer<T,S> renderer;
    private final AtomicLong animationCount = new AtomicLong();

    /**
     *
     * @param plugin The plugin this animation will run on
     * @param factory The factory used to create the running animations
     * @param renderer An object that describes how to render an animation
     */
    public MinecraftAnimation(@NotNull ApiPlugin plugin,@NotNull AnimationFactory factory,@NotNull AnimationRenderer<T, S> renderer) {
        this.plugin = plugin;
        this.factory = factory;
        this.renderer = renderer;
        this.runningAnimations = new ArrayList<>();
        this.frames = new ArrayList<>();
    }

    /**
     * Creates a new Animation and starts it. This animation will have NO SCREENS and will thus
     * render nothing. More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @return A running animation with no screens
     */
    @NotNull
    public RunningAnimation<S> start() {
        return start(Collections.emptyList());
    }

    /**
     * Creates a new animation and starts it. This animation will have one singular screen.
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screen The screen to render to
     * @return A new animation with the specified screen
     */
    @NotNull
    public RunningAnimation<S> start(S screen) {
        return start(ImmutableList.of(screen));
    }

    /**
     * Creates a new animation and starts it. This animation will have the specified screens
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screens The screens to render to
     * @return A new animation with the specified screen
     */
    @SafeVarargs
    public final RunningAnimation<S> start(S... screens) {
        return start(Arrays.asList(screens));
    }

    /**
     * Creates a new animation and starts it. This animation will have the specified screens
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screens The screens to render to
     * @return A new animation with the specified screen
     */
    @NotNull
    public RunningAnimation<S> start(@NotNull Iterable<S> screens) {
        RunningAnimation<S> animation = get(screens);
        animation.start();
        return animation;
    }

    /**
     * Creates a new running animation. This animation will NOT be running. This animation will have NO SCREENS and will thus
     * render nothing. More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @return A running animation with no screens
     */
    public RunningAnimation<S> get() {
        return get(Collections.emptyList());
    }

    /**
     * Creates a new running animation. This animation will NOT be running. This animation will have one singular screen.
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screen The screen to render to
     * @return A new animation with the specified screen
     */
    public RunningAnimation<S> get(S screen) {
        return get(ImmutableList.of(screen));
    }

    /**
     * Creates a new running animation. This animation will NOT be running. This animation will have the screens specified
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screens The screen to render to
     * @return A new animation with the specified screen
     */
    @SafeVarargs
    public final RunningAnimation<S> get(S... screens) {
        return get(Arrays.asList(screens));
    }

    /**
     * Creates a new running animation. This animation will NOT be running. This animation will have the screens specified
     * More screens can be added later with {@link RunningAnimation#addScreen(Object)}
     *
     * @param screens The screen to render to
     * @return A new animation with the specified screen
     */
    public RunningAnimation<S> get(@NotNull Iterable<S> screens) {
        Objects.requireNonNull(screens);
        RunningAnimation<S> animation;
        animation = factory.get(this,plugin,renderer.copy(),animationCount.getAndIncrement());
        for(S screen : screens) {
            animation.addScreen(screen);
        }
        //cannot add a running animation is some other manipulation is being executed
        synchronized (this.runningAnimations) {
            this.runningAnimations.add(animation);
        }
        return animation;
    }

    /**
     * Cancels all and any running animations. As the animations will be cancelled they will not restart.
     */
    public void stopAll() {
        //while we are stopping all no new animations can be added.
        synchronized (this.runningAnimations) {
            List<RunningAnimation<S>> copy = new ArrayList<>(this.runningAnimations);
            for(RunningAnimation<S> animation : copy) {
                animation.cancel();
            }
        }
    }

    /**
     * @return A collection of all currently running animations
     */
    @NotNull
    public Collection<RunningAnimation<S>> getCurrentlyRunning() {
        return Collections.unmodifiableList(this.runningAnimations);
    }

    /**
     * Adds a frame to the collection of frames. Frames are played in the order that they are added. This frame
     * will be played the specified amount of time after the previous frame.
     *
     * @param frame The next frame to add to the collection
     */
    public void addFrame(@NotNull MinecraftFrame<T> frame) {
        Objects.requireNonNull(frame);
        synchronized (frames) {
            frames.add(frame);
        }
    }

    /**
     * Removes all frames from the collection of frames.
     */
    public void clearFrames() {
        synchronized (frames) {
            this.frames.clear();
        }
    }

    /**
     * @return Returns an iterator allowing a running animation to loop through all the frames. The iterator gives
     * frames in the order they were sent. This method could be theoretically overridden to send frames one after
     * another from a fileinputstream or from a mathematical formulae.
     */
    @NotNull
    public Iterator<MinecraftFrame<T>> getFrames() {
        synchronized (frames) {
            return Collections.unmodifiableList(new ArrayList<>(frames)).iterator();
        }
    }

    /**
     * Should be called by any running animation once it has terminated for good. Once it has fully and completely terminated
     * this must be called to remove it from the collection of running animations
     *
     * @param animation The animation that is currently running
     */
    public void onComplete(@NotNull RunningAnimation<S> animation) {
        Objects.requireNonNull(animation);
        synchronized (runningAnimations) {
            runningAnimations.remove(animation);
        }
    }

    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Animation Count",animationCount.get())
                .add("Currently Running",runningAnimations.size())
                .add("Renderer",renderer)
                .toString();
    }

}
