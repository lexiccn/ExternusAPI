package me.deltaorion.extapi.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Each minecraft frame contains information of type T. The minecraft renderer should take this information
 * and render it to a screen of type S.
 *
 * The screen may be anything, but is likely to be some object in the world such as a player or a location. The information
 * to render may be something like a coordinate, a material an itemstack or a message. The renderer then should do something
 * meaningful with this.
 *
 * Each running animation keeps a copy of an animation renderer. This means that the renderer does NOT need to be stateless. The
 * next state could derive from the previous state. This does not guarantee that there will be an animation renderer for each individual
 * screen however.
 *
 * @param <T> The type of the information to render
 * @param <S> the type of the screen to render to.
 */
public interface AnimationRenderer<T,S> {

    /**
     * Render the object stored in the frame to the given screen.
     *
     * @param frame The frame to render
     * @param screen The screen to render to
     */
    public void render(@NotNull MinecraftFrame<T> frame, @Nullable S screen);

    /**
     * Create a new copy of the animation renderer. This should not clone the current renderer but should rather
     * give a new copy as if this was being constructed for the first time.
     *
     * @return A new and fresh copy of this renderer
     */
    @NotNull
    public AnimationRenderer<T,S> copy();

    /**
     * Logic to be run before the animation is fully complete.
     *
     * If false is returned then the animation will not restart. If true is returned then the animation will restart
     * after it is complete
     *
     * @param animation The animation
     * @return true if the animation should restart, false if otherwise.
     */
    default boolean beforeCompletion(@NotNull RunningAnimation<S> animation) {
        return false;
    }
}
