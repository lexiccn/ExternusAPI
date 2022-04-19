package me.deltaorion.common.test.animation;

import me.deltaorion.common.animation.AnimationRenderer;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.plugin.sender.Sender;
import org.jetbrains.annotations.NotNull;

public class ChatAnimation implements AnimationRenderer<String,Sender> {

    @Override
    public void render(@NotNull RunningAnimation<Sender> animation, @NotNull MinecraftFrame<String> frame, @NotNull Sender screen) {
        if(frame.getObject()==null)
            throw new NullPointerException("Frame cannot be null!");
        screen.sendMessage(frame.getObject());
    }

    @NotNull
    @Override
    public AnimationRenderer<String, Sender> getNewRenderer() {
        return new ChatAnimation();
    }

    @Override
    public boolean beforeCompletion(@NotNull RunningAnimation<Sender> animation) {
        for(Sender sender : animation.getScreens()) {
            sender.sendMessage("Animation Cancelled!");
        }
        return true;
    }
}
