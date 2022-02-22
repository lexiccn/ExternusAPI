package me.deltaorion.common.test.animation;

import me.deltaorion.common.animation.*;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class TestAnimation extends MinecraftAnimation<String, List<String>> {

    @Nullable private volatile CountDownLatch finishLatch;
    @Nullable private volatile CountDownLatch frameLatch;
    @Nullable private volatile CountDownLatch stopAllLatch;

    private TestAnimation(ApiPlugin plugin, AnimationFactory factory, TestAnimationRender render) {
        super(plugin, factory, render);
    }

    public static TestAnimation create(ApiPlugin plugin, AnimationFactory factory, boolean repeat) {
        TestAnimationRender render = new TestAnimationRender(repeat);
        TestAnimation animation = new TestAnimation(plugin,factory,render);
        render.setAnimation(animation);
        return animation;
    }

    public void setFinishLatch(@Nullable CountDownLatch finishLatch) {
        this.finishLatch = finishLatch;
    }

    public void setFrameLatch(@Nullable CountDownLatch frameLatch) {
        this.frameLatch = frameLatch;
    }

    @Nullable
    public CountDownLatch getFinishLatch() {
        return finishLatch;
    }

    @Nullable
    public CountDownLatch getFrameLatch() {
        return frameLatch;
    }

    @Override
    public void stopAll() {
        super.stopAll();

        if(stopAllLatch==null)
            return;

        if(stopAllLatch.getCount()==0)
            return;

        stopAllLatch.countDown();
    }

    @Nullable
    public CountDownLatch getStopAllLatch() {
        return stopAllLatch;
    }

    public void setStopAllLatch(@Nullable CountDownLatch stopAllLatch) {
        this.stopAllLatch = stopAllLatch;
    }

    private static class TestAnimationRender implements AnimationRenderer<String, List<String>> {

        @Nullable private TestAnimation animation;
        private final boolean repeat;

        private TestAnimationRender(boolean repeat) {
            this.repeat = repeat;
        }

        private void setAnimation(@NotNull TestAnimation animation) {
            this.animation = Objects.requireNonNull(animation);
        }

        @Override
        public void render(@NotNull RunningAnimation<List<String>> animation , @NotNull MinecraftFrame<String> frame, @NotNull List<String> screen) {
            if (frame.getObject() == null) {
                throw new RuntimeException();
            }
            screen.add(frame.getObject());
            if (this.animation != null) {
                if (this.animation.getFrameLatch() != null) {
                    if (this.animation.getFrameLatch().getCount() > 0) {
                        this.animation.getFrameLatch().countDown();
                    }
                }
            }
        }

        @NotNull
        @Override
        public AnimationRenderer<String, List<String>> copy() {
            TestAnimationRender render = new TestAnimationRender(repeat);
            render.animation = animation;
            return render;
        }

        @Override
        public boolean beforeCompletion(@NotNull RunningAnimation<List<String>> animation) {
            if(this.animation==null)
                return repeat;


            if(this.animation.getFinishLatch()==null)
                return repeat;


            if(this.animation.getFinishLatch().getCount()==0)
                return repeat;

            this.animation.getFinishLatch().countDown();
            return repeat;
        }
    }
}
