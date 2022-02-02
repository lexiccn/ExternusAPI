package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
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
        public void render(@NotNull MinecraftFrame<String> frame, PluginLogger screen) {
            screen.info(frame.getObject());
        }

        @NotNull
        @Override
        public AnimationRenderer<String, PluginLogger> copy() {
            return new LoggingRenderer();
        }
    }
}
