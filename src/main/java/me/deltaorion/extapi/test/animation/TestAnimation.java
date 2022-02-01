package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationFactory;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.SimpleMinecraftAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.plugin.ApiPlugin;

import java.util.List;

public class TestAnimation extends SimpleMinecraftAnimation<String, List<String>> {

    public TestAnimation(ApiPlugin plugin, AnimationFactory factory) {
        super(plugin, factory);
    }

    @Override
    public void render(MinecraftFrame<String> frame, List<String> screen) {
        if(frame.getObject()==null) {
            throw new RuntimeException();
        }
        screen.add(frame.getObject());
    }
}
