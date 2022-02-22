package me.deltaorion.bukkit.test.command;

import me.deltaorion.bukkit.plugin.server.BukkitServer;
import me.deltaorion.bukkit.test.animation.CobbleLineAnimation;
import me.deltaorion.bukkit.test.animation.PolarVector;
import me.deltaorion.common.animation.MinecraftAnimation;
import me.deltaorion.common.animation.MinecraftFrame;
import me.deltaorion.common.animation.factory.AnimationFactories;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.test.command.AbstractAnimationTestCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CobblestoneAnimationTestCommand extends AbstractAnimationTestCommand<PolarVector, Location> {
    public CobblestoneAnimationTestCommand(ApiPlugin plugin) {
        super(
                plugin,
                new MinecraftAnimation<>(plugin,
                        AnimationFactories.SYNC_SERVER(BukkitServer.MILLIS_PER_TICK),
                        new CobbleLineAnimation(plugin))
        );
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        Player player = Bukkit.getPlayer(command.getSender().getUniqueId());
        int time = command.getArgOrBlank(0).asIntOrDefault(400);
        int length = command.getArgOrBlank(1).asIntOrDefault(35);

        animation.clearFrames();
        animation.addFrame(new MinecraftFrame<>(new PolarVector(length,0),1000));
        final double max = 2*Math.PI;
        final double increment = max/60;
        for(double theta =0;theta<max;theta+=increment) {
            animation.addFrame(new MinecraftFrame<>(new PolarVector(length,Math.min(theta,max)),time));
        }
        animation.addFrame(new MinecraftFrame<>(new PolarVector(length,max),time));
        animation.start(player.getLocation().clone());
    }
}
