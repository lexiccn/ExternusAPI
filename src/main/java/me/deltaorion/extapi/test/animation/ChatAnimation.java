package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationFactory;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.SimpleMinecraftAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;

public class ChatAnimation extends SimpleMinecraftAnimation<String, Sender> {
    public ChatAnimation(ApiPlugin plugin) {
        super(plugin, AnimationFactories.SCHEDULE_ASYNC());
    }

    @Override
    public void render(MinecraftFrame<String> frame, Sender screen) {
        if(frame.getObject()==null)
            throw new NullPointerException("Frame cannot be null!");
        screen.sendMessage(frame.getObject());
    }
}
