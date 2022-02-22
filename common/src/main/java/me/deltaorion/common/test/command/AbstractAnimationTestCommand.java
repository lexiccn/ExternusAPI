package me.deltaorion.common.test.command;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.plugin.plugin.ApiPlugin;

public abstract class AbstractAnimationTestCommand<T,S> extends FunctionalCommand {

    protected final MinecraftAnimation<T,S> animation;

    public AbstractAnimationTestCommand(ApiPlugin plugin, MinecraftAnimation<T, S> animation) {
        super(APIPermissions.COMMAND);
        this.animation = animation;

        registerArgument("cancel",command -> {
            animation.stopAll();
        });

        registerArgument("pause",command -> {
            animation.pauseAll();;
        });

        registerArgument("play",command -> {
            animation.playAll();
        });

        registerArgument("speed",command -> {
            for(RunningAnimation<?> runningAnimation : animation.getCurrentlyRunning()) {
                runningAnimation.setPlaySpeed(command.getArgOrBlank(0).asFloatOrDefault(1.0f));
            }
        });
    }
}
