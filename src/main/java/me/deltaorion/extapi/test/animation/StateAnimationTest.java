package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.plugin.ApiPlugin;

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
        public void render(MinecraftFrame<String> frame, List<String> screen) {

            if(frame.getObject()==null)
                throw new NullPointerException();

            screen.add(frame.getObject());
            if(lastFrame!=null) {
                screen.add(lastFrame);
            }
            lastFrame = frame.getObject();
        }

        @Override
        public AnimationRenderer<String, List<String>> copy() {
            return new StateAnimationRenderer(finishLatch);
        }

        @Override
        public boolean beforeCompletion(RunningAnimation<List<String>> animation) {
            finishLatch.countDown();
            return false;
        }

    }
}
