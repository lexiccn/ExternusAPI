package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.test.animation.ChatAnimation;
import me.deltaorion.extapi.test.animation.CobbleLineAnimation;
import me.deltaorion.extapi.test.animation.PolarVector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AnimationTest extends FunctionalCommand {

    private final MinecraftAnimation<PolarVector, Location> animation;
    private final MinecraftAnimation<String,Sender> animation2;

    public AnimationTest(ApiPlugin plugin) {
        super(APIPermissions.COMMAND);
        animation = new MinecraftAnimation<>(
                plugin,
                AnimationFactories.SYNC_BUKKIT(),
                new CobbleLineAnimation(plugin)
        );
        animation2 = new MinecraftAnimation<>(
                plugin,
                AnimationFactories.SCHEDULE_ASYNC(),
                new ChatAnimation()
        );
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {

        Player player = Bukkit.getPlayer(command.getSender().getUniqueId());
        int time = command.getArgOrBlank(0).asIntOrDefault(400);
        int length = command.getArgOrBlank(1).asIntOrDefault(35);

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("cancel")) {
            animation.stopAll();
            animation2.stopAll();
            return;
        }

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("pause")) {
            for(RunningAnimation<?> animation : animation.getCurrentlyRunning()) {
                animation.pause();
            }
            animation2.pauseAll();
            return;
        }

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("play")) {
            for(RunningAnimation<?> animation : animation.getCurrentlyRunning()) {
                animation.play();
            }
            animation2.playAll();
            return;
        }

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("speed")) {
            for(RunningAnimation<?> animation : animation.getCurrentlyRunning()) {
                animation.setPlaySpeed(command.getArgOrBlank(1).asFloatOrDefault(1.0f));
            }

            for(RunningAnimation<?> animation : animation2.getCurrentlyRunning()) {
                animation.setPlaySpeed(command.getArgOrBlank(1).asFloatOrDefault(1.0f));
            }
            return;
        }
        /*
        animation.clearFrames();
        animation.addFrame(new MinecraftFrame<>(new PolarVector(length,0),1000));
        final double max = 2*Math.PI;
        final double increment = max/60;
        for(double theta =0;theta<max;theta+=increment) {
            animation.addFrame(new MinecraftFrame<>(new PolarVector(length,Math.min(theta,max)),time));
        }
        animation.addFrame(new MinecraftFrame<>(new PolarVector(length,max),time));
        animation.start(player.getLocation().clone());

        */
        String str = command.getArgOrDefault(0,"Hello World").asString();
        animation2.clearFrames();
        for(int i=0;i<str.length();i++) {
            for(int j=0;j<9;j++) {
                animation2.addFrame(new MinecraftFrame<>("",0));
            }
            animation2.addFrame(new MinecraftFrame<>(ChatColor.GOLD + "" + ChatColor.BOLD + str.charAt(i),time));
        }
        for(int j=0;j<9;j++) {
            animation2.addFrame(new MinecraftFrame<>("",0));
        }
        animation2.start(command.getSender());

    }
}
