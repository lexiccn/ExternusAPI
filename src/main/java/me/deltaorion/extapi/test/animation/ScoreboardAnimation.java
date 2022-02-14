package me.deltaorion.extapi.test.animation;

import me.deltaorion.extapi.animation.AnimationRenderer;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ScoreboardAnimation implements AnimationRenderer<String, Player> {

    private final BukkitPlugin plugin;
    private final String scoreboardName;

    public ScoreboardAnimation(@NotNull String scoreboardName, BukkitPlugin plugin) {
        this.plugin = plugin;
        this.scoreboardName = scoreboardName;
    }

    @Override
    public void render(@NotNull RunningAnimation<Player> animation, @NotNull MinecraftFrame<String> frame, @NotNull Player screen) {
        if(!screen.isOnline()) {
            animation.removeScreen(screen);
            return;
        }

        if(frame.getObject()==null)
            throw new NullPointerException("Null frame");

        BukkitApiPlayer player = plugin.getBukkitPlayerManager().getPlayer(screen);
        if(player.getScoreboard()==null)
            return;

        if(player.getScoreboard().getName().equalsIgnoreCase(this.scoreboardName)) {
            player.getScoreboard().setTitle(frame.getObject());
            double tps;
            try {
                tps = plugin.getServer().spigot().getTPS()[0];
            } catch(NoSuchMethodError e) {
                tps = Math.random();
            }
            player.getScoreboard().setLineArgs("TPS",tps);
        }
    }

    @NotNull
    @Override
    public AnimationRenderer<String, Player> copy() {
        return new ScoreboardAnimation(scoreboardName,plugin);
    }

    @Override
    public boolean beforeCompletion(@NotNull RunningAnimation<Player> animation) {
        return true;
    }
}
