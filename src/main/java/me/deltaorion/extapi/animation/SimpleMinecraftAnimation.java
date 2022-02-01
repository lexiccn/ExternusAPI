package me.deltaorion.extapi.animation;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public abstract class SimpleMinecraftAnimation<T,S> implements MinecraftAnimation<T,S> {

    @GuardedBy("this") private final List<MinecraftFrame<T>> frames; //will receive a lot of writing at the start, then only read,
    private final Set<S> screens; //screens will be read a lot and probably wont be changed much
    @GuardedBy("this") private final List<RunningAnimation> runningAnimations; //will be written to and seldom read
    private final ApiPlugin plugin;
    private final AnimationFactory factory;
    private final AtomicLong animationCount = new AtomicLong();

    public SimpleMinecraftAnimation(ApiPlugin plugin, AnimationFactory factory) {
        this.plugin = plugin;
        this.factory = factory;
        this.runningAnimations = new ArrayList<>();
        this.screens = new CopyOnWriteArraySet<>();
        this.frames = new ArrayList<>();
    }

    @NotNull @Override
    public RunningAnimation start() {
        RunningAnimation animation;
        synchronized (runningAnimations) {
            animation = factory.get(this,plugin,animationCount.getAndIncrement());
            animation.start();
            this.runningAnimations.add(animation);
        }
        return animation;
    }

    @Override
    public void stopAll() {
        synchronized (runningAnimations) {
            for(RunningAnimation animation : runningAnimations) {
                animation.cancel();
            }

            runningAnimations.clear();
        }
    }

    @Override
    public void addFrame(@NotNull MinecraftFrame<T> frame) {
        synchronized (frames) {
            frames.add(frame);
        }
    }


    @Override
    public void clearFrames() {
        synchronized (frames) {
            this.frames.clear();
        }
    }

    @Override
    public void addScreen(@NotNull S screen) {
        this.screens.add(screen);
    }

    @Override
    public boolean removeScreen(@NotNull S screen) {
        return this.screens.remove(screen);
    }

    public void clearScreens() {
        this.screens.clear();
    }

    @NotNull
    @Override
    public Collection<MinecraftFrame<T>> getFrames() {
        synchronized (frames) {
            return Collections.unmodifiableList(new ArrayList<>(frames));
        }
    }

    @NotNull
    @Override
    public Collection<S> getScreens() {
        return Collections.unmodifiableSet(screens);
    }

    @Override
    public void onComplete(RunningAnimation animation) {
        boolean cancel = false;
        try {
            cancel = beforeCompletion(animation);
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when attempting to run beforeCompletion",e);
        }
        if(cancel) {
            animation.start();
            return;
        }
        synchronized (runningAnimations) {
            runningAnimations.remove(animation);
        }
    }

    public boolean beforeCompletion(RunningAnimation animation) {
        return false;
    }
}
