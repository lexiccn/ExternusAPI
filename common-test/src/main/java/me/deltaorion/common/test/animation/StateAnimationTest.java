package me.deltaorion.common.test.animation;

import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.animation.factory.AnimationFactories;
import me.deltaorion.common.plugin.ApiPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StateAnimationTest extends MinecraftAnimation<String, List<String>> {


    public StateAnimationTest(ApiPlugin plugin, CountDownLatch finishLatch) {
        super(plugin, AnimationFactories.SCHEDULE_ASYNC(),new StateAnimationRenderer(finishLatch));
    }

    private static class StateAnimationRenderer implements AnimationRenderer<String,List<String>> {

        private String lastFrame;
        private final CountDownLatch finishLatch;

        private StateAnimationRenderer(CountDownLatch finishLatch) {
            this.finishLatch = finishLatch;
        }

        @Override
        public void render(@NotNull RunningAnimation<List<String>> animation, @NotNull MinecraftFrame<String> frame, @NotNull List<String> screen) {

            if(frame.getObject()==null)
                throw new NullPointerException();

            screen.add(frame.getObject());
            if(lastFrame!=null) {
                screen.add(lastFrame);
            }
            lastFrame = frame.getObject();
        }

        @NotNull
        @Override
        public AnimationRenderer<String, List<String>> getNewRenderer() {
            return new StateAnimationRenderer(finishLatch);
        }

        @Override
        public boolean beforeCompletion(@NotNull RunningAnimation<List<String>> animation) {
            finishLatch.countDown();
            return false;
        }

    }
}
