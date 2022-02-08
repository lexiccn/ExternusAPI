package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import me.deltaorion.extapi.display.actionbar.ActionBar;
import me.deltaorion.extapi.display.actionbar.RejectionPolicy;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActionBarCommand extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public ActionBarCommand(BukkitPlugin plugin) {
        super(NO_PERMISSION);
        this.plugin = plugin;
        registerArgument("tps",new TPSCommand(plugin));
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            return;

        if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
            throw new CommandException("Please run this command with ProtocolLib Enabled!");

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
        BukkitApiPlayer apiPlayer = plugin.getBukkitPlayerManager().getPlayer(player);

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("cancel")) {
            apiPlayer.getActionBarManager().removeActionBar();
            return;
        }

        String message = command.getArgOrDefault(0,"Hello World").asString();
        Duration duration = command.getArgOrDefault(1,"e").asDurationOrDefault(Duration.of(5,ChronoUnit.SECONDS));
        String policy = command.getArgOrDefault(2,"OVERWRITE").asString().toUpperCase(Locale.ROOT);

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("long")) {
            message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        }

        RejectionPolicy pol = null;
        switch (policy) {
            case "FAIL":
                pol = RejectionPolicy.FAIL();
                break;
            case "SILENT":
                pol = RejectionPolicy.SILENT_REJECT();
                break;
            case "QUEUE":
                pol = RejectionPolicy.QUEUE();
                break;
            default:
                pol = RejectionPolicy.OVERWRITE();
        }

        ActionBar bar = new ActionBar(message, duration);
        apiPlayer.getActionBarManager().send(bar,pol);
    }

    private static class TPSCommand extends FunctionalCommand {

        private final BukkitPlugin plugin;
        private SchedulerTask task;

        protected TPSCommand(BukkitPlugin plugin) {
            super(NO_PERMISSION);
            this.plugin = plugin;
        }

        @Override
        public void commandLogic(SentCommand command) throws CommandException {
            if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
                throw new CommandException("Please run this command with ProtocolLib Enabled!");

            if(command.getArgOrBlank(0).asString().equalsIgnoreCase("cancel")) {
                task.cancel();
                return;
            }

            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BukkitApiPlayer apiPlayer = plugin.getBukkitPlayerManager().getPlayer(player);

            apiPlayer.getActionBarManager().send(new ActionBar(Message.valueOf("&eTPS: &f%s"),Duration.of(100,ChronoUnit.DAYS),"TPSBar"));

            if(task!=null)
                task.cancel();

            task = plugin.getScheduler().runTaskTimerAsynchronously(new Runnable() {
                @Override
                public void run() {
                    ActionBar actionBar = apiPlayer.getActionBarManager().getCurrentActionBar();
                    if(actionBar==null)
                        return;

                    if(!Objects.equals(actionBar.getName(),"TPSBar"))
                        return;

                    apiPlayer.getActionBarManager().setArgs(plugin.getServer().spigot().getTPS()[0]);
                }
            },0,500, TimeUnit.MILLISECONDS);

        }
    }

}
