package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.common.sender.Sender;

public class ChatAnimation implements AnimationRenderer<String,Sender> {

    @Override
    public void render(MinecraftFrame<String> frame, Sender screen) {
        if(frame.getObject()==null)
            throw new NullPointerException("Frame cannot be null!");
        screen.sendMessage(frame.getObject());
    }

    @Override
    public AnimationRenderer<String, Sender> copy() {
        return new ChatAnimation();
    }

    @Override
    public boolean beforeCompletion(RunningAnimation<Sender> animation) {
        for(Sender sender : animation.getScreens()) {
            sender.sendMessage("Animation Cancelled!");
        }
        return true;
    }
}
