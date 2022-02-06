package me.deltaorion.extapi.animation.running;

import me.deltaorion.extapi.animation.RunningAnimation;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ScreenedRunningAnimation<S> implements RunningAnimation<S> {

    private final List<S> screens; //screens will be read a lot and probably wont be changed much
    @GuardedBy("this") private Supplier<Collection<S>> screenFunction;
    private final Object screenLock = new Object();

    protected ScreenedRunningAnimation() {
        this.screens = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addScreen(@NotNull S screen) {
        if(hasScreenFunction())
            return;
        this.screens.add(screen);
    }

    @Override
    public void removeScreen(@NotNull S screen) {
        if(hasScreenFunction())
            return;
        this.screens.remove(screen);
    }

    @Override
    public void clearScreens() {
        this.screens.clear();
    }

    @Override
    public void setScreenFunction(@Nullable Supplier<Collection<S>> screenFunction) {
        synchronized (screenLock) {
            this.screenFunction = screenFunction;
            if(this.screenFunction!=null) {
                clearScreens();
            }
        }
    }

    @Override
    public boolean hasScreenFunction() {
        return this.screenFunction!=null;
    }

    @NotNull
    @Override
    public Collection<S> getScreens() {
        synchronized (screenLock) {
            if (this.screenFunction == null) {
                return Collections.unmodifiableList(screens);
            }
        }
        //drop lock before running their function, we have no clue how poorly optimised this is
        try {
            return Collections.unmodifiableCollection(screenFunction.get());
        } catch (Throwable e) {
            //if it fails alert the user and return empty list, lets not brick the animation or whoever is trying
            //to call this function!
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
