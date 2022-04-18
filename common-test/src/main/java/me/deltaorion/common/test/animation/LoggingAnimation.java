package me.deltaorion.common.test.animation;

import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.animation.factory.AnimationFactories;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.plugin.logger.PluginLogger;
import org.jetbrains.annotations.NotNull;

public class LoggingAnimation extends MinecraftAnimation<String, PluginLogger> {
    public LoggingAnimation(ApiPlugin plugin) {
        super(plugin, AnimationFactories.SCHEDULE_ASYNC(),new LoggingRenderer());
        addFrame(new MinecraftFrame<>("Gamer",0));
        addFrame(new MinecraftFrame<>("Hello",500));
        addFrame(new MinecraftFrame<>("World",500));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Wait",1000));
    }

    private static class LoggingRenderer implements AnimationRenderer<String,PluginLogger> {
        @Override
        public void render(@NotNull RunningAnimation<PluginLogger> animation, @NotNull MinecraftFrame<String> frame, @NotNull PluginLogger screen) {
            screen.info(frame.getObject());
        }

        @NotNull
        @Override
        public AnimationRenderer<String, PluginLogger> copy() {
            return new LoggingRenderer();
        }
    }
}
