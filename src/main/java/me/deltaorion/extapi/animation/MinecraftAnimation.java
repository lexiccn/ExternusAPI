package me.deltaorion.extapi.animation;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 *
 * @param <T> The type of object to render
 * @param <S> The screen to render to
 */
public interface MinecraftAnimation<T,S> {

    //runs a new animation, this can be called multiple times
    @NotNull
    public RunningAnimation start();

    //cancels all the animations running
    public void stopAll();

    //add frame, what to do if it is running
    public void addFrame(@NotNull MinecraftFrame<T> frame);

    public void clearFrames();

    public void addScreen(@NotNull S screen);

    public boolean removeScreen(@NotNull S screen);

    public void clearScreens();

    //how to render a frame, must be stateless,
    public void render(MinecraftFrame<T> frame, S screen);

    @NotNull
    public Collection<MinecraftFrame<T>> getFrames();

    @NotNull
    public Collection<S> getScreens();

    public void onComplete(RunningAnimation animation);

}
