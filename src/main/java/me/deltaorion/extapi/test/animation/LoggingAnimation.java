package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.SimpleMinecraftAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.plugin.ApiPlugin;

public class LoggingAnimation extends SimpleMinecraftAnimation<String, PluginLogger> {
    public LoggingAnimation(ApiPlugin plugin) {
        super(plugin, AnimationFactories.SCHEDULE_ASYNC());
        addFrame(new MinecraftFrame<>("Gamer",0));
        addFrame(new MinecraftFrame<>("Hello",500));
        addFrame(new MinecraftFrame<>("World",500));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Quick",0));
        addFrame(new MinecraftFrame<>("Wait",1000));
    }

    @Override
    public void render(MinecraftFrame<String> frame, PluginLogger screen) {
        screen.info(frame.getObject());
    }
}
