package me.deltaorion.common.test.command;

import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.factory.AnimationFactories;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.ChatColor;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.test.animation.ChatAnimation;

public class ChatAnimationTestCommand extends AbstractAnimationTestCommand<String, Sender> {
    public ChatAnimationTestCommand(ApiPlugin plugin) {
        super(plugin, new MinecraftAnimation<>(
                plugin,
                AnimationFactories.SCHEDULE_ASYNC(),
                new ChatAnimation()
        ));
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        String str = command.getArgOrDefault(0,"Hello World").asString();
        int time  = command.getArgOrBlank(1).asIntOrDefault(400);
        animation.clearFrames();
        for(int i=0;i<str.length();i++) {
            for(int j=0;j<9;j++) {
                animation.addFrame(new MinecraftFrame<>("",0));
            }
            animation.addFrame(new MinecraftFrame<>(ChatColor.GOLD + "" + ChatColor.BOLD + str.charAt(i),time));
        }
        for(int j=0;j<9;j++) {
            animation.addFrame(new MinecraftFrame<>("",0));
        }
        animation.start(command.getSender());

    }
}
