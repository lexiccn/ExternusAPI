package me.deltaorion.extapi.animation;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MinecraftAnimation<T,S> {

    @GuardedBy("this") private final List<MinecraftFrame<T>> frames; //will receive a lot of writing at the start, then only read,
    @GuardedBy("this") private final List<RunningAnimation<S>> runningAnimations; //will be written to and seldom read
    private final ApiPlugin plugin;
    private final AnimationFactory factory;
    private final AnimationRenderer<T,S> renderer;
    private final AtomicLong animationCount = new AtomicLong();

    public MinecraftAnimation(ApiPlugin plugin, AnimationFactory factory, AnimationRenderer<T, S> renderer) {
        this.plugin = plugin;
        this.factory = factory;
        this.renderer = renderer;
        this.runningAnimations = new ArrayList<>();
        this.frames = new ArrayList<>();
    }

    @NotNull
    public RunningAnimation<S> start() {
        return start(Collections.emptyList());
    }

    @NotNull
    public RunningAnimation<S> start(S screen) {
        return start(ImmutableList.of(screen));
    }

    @SafeVarargs
    public final RunningAnimation<S> start(S... screens) {
        return start(Arrays.asList(screens));
    }

    @NotNull
    public RunningAnimation<S> start(@NotNull Iterable<S> screens) {
        RunningAnimation<S> animation = get(screens);
        animation.start();
        return animation;
    }

    public RunningAnimation<S> get(S screen) {
        return get(ImmutableList.of(screen));
    }

    @SafeVarargs
    public final RunningAnimation<S> get(S... screens) {
        return get(Arrays.asList(screens));
    }

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

    //stops all runnings, does not care if the animation wants to restart
    public void stopAll() {
        //while we are stopping all no new animations can be added.
        synchronized (this.runningAnimations) {
            List<RunningAnimation<S>> copy = new ArrayList<>(this.runningAnimations);
            for(RunningAnimation<S> animation : copy) {
                animation.cancel();
            }
        }
    }

    public void addFrame(@NotNull MinecraftFrame<T> frame) {
        Objects.requireNonNull(frame);
        synchronized (frames) {
            frames.add(frame);
        }
    }

    public void clearFrames() {
        synchronized (frames) {
            this.frames.clear();
        }
    }

    @NotNull
    public Collection<MinecraftFrame<T>> getFrames() {
        synchronized (frames) {
            return Collections.unmodifiableList(new ArrayList<>(frames));
        }
    }

    public void onComplete(@NotNull RunningAnimation<S> animation, boolean restart) {
        Objects.requireNonNull(animation);
        synchronized (runningAnimations) {
            if(restart) {
                //if we are stopping all, we cannot restart the animation
                animation.restart();
                return;
            }
            runningAnimations.remove(animation);
        }
    }

}
