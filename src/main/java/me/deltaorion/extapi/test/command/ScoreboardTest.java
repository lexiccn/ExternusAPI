package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.animation.MinecraftAnimation;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.scoreboard.WrapperScoreboard;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.test.animation.ScoreboardAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ScoreboardTest extends FunctionalCommand {

    @Nullable private RunningAnimation<Player> runningAnimation;
    @Nullable private MinecraftAnimation<String,Player> animation;
    private final BukkitPlugin plugin;

    public ScoreboardTest(BukkitPlugin plugin) {
        super(NO_PERMISSION);
        this.plugin = plugin;
    }

    private WrapperScoreboard scoreboard;

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;

        final String scoreboardName = "sb-1";
        Player player = Bukkit.getPlayer(command.getSender().getUniqueId());

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("cancel")) {
            if(runningAnimation!=null)
                runningAnimation.cancel();
            return;
        }

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("pause")) {
            if(runningAnimation!=null) {
                runningAnimation.pause();
            }
            return;
        }

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("play")) {
            if(runningAnimation!=null)
                runningAnimation.play();
            return;
        }

        if(scoreboard==null) {
            scoreboard = new WrapperScoreboard(scoreboardName, plugin, 3);
            final String title = "Hello World";
            scoreboard.setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + title);
            scoreboard.setLine(ChatColor.GRAY + "Test Server", 0);
            scoreboard.setLine(ChatColor.WHITE + "abcdefghijklmnopqrstuvwxyz32", 1);
            scoreboard.setLine(Message.valueOf(ChatColor.GOLD + "TPS: " + ChatColor.WHITE + "%s"), 2, "TPS", plugin.getServer().spigot().getTPS()[0]);
            scoreboard.setPlayer(player);

            animation = new MinecraftAnimation<>(plugin,
                    AnimationFactories.SCHEDULE_ASYNC(),
                    new ScoreboardAnimation(scoreboardName,plugin));

            String altColor = ChatColor.YELLOW + "" + ChatColor.BOLD;
            animation.addFrame(new MinecraftFrame<>(ChatColor.WHITE + "" + ChatColor.BOLD + title,400));
            for(int i=0;i<title.length();i++) {
                String splitA = title.substring(0,i);
                String letter = title.substring(i,i+1);
                String splitB = title.substring(i+1);
                String entry = ChatColor.WHITE + "" + ChatColor.BOLD + splitA + altColor + letter + ChatColor.WHITE + ChatColor.BOLD + "" + splitB;
                animation.addFrame(new MinecraftFrame<>(entry,400));
            }
            animation.addFrame(new MinecraftFrame<>(altColor + title,400));
            this.runningAnimation = animation.start(new Supplier<Collection<Player>>() {
                @Override
                public Collection<Player> get() {
                    return new ArrayList<>(plugin.getServer().getOnlinePlayers());
                }
            });
        }
    }
}
