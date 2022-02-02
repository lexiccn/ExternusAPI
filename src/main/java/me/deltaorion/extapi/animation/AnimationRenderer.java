package me.deltaorion.extapi.animation;

public interface AnimationRenderer<T,S> {

    public void render(MinecraftFrame<T> frame, S screen);

    public AnimationRenderer<T,S> copy();

    default boolean beforeCompletion(RunningAnimation<S> animation) {
        return false;
    }
}
