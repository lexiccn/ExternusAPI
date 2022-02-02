package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.RunningAnimation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ScreenedRunningAnimation<S> implements RunningAnimation<S> {

    private final List<S> screens; //screens will be read a lot and probably wont be changed much

    protected ScreenedRunningAnimation() {
        this.screens = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addScreen(@NotNull S screen) {
        this.screens.add(screen);
    }

    @Override
    public void removeScreen(@NotNull S screen) {
        this.screens.remove(screen);
    }

    public void clearScreens() {
        this.screens.clear();
    }

    @NotNull
    @Override
    public Collection<S> getScreens() {
        return Collections.unmodifiableList(screens);
    }
}
