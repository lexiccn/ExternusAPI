package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.APIPermissions;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bossbar.BarColor;
import me.deltaorion.extapi.display.bossbar.BarFlag;
import me.deltaorion.extapi.display.bossbar.BarStyle;
import me.deltaorion.extapi.display.bossbar.BossBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.protocol.WrapperPlayServerBoss;
import org.bukkit.entity.Player;

import java.util.*;

public class BossBarTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public BossBarTest(BukkitPlugin plugin) {
        super(APIPermissions.COMMAND);
        this.plugin = plugin;
        registerArgument("message",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setMessage(command.getArgOrDefault(0,"Gamer").asString());
        });

        registerArgument("translatable",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setMessage(Message.valueOfTranslatable("hello-arg"),player.getName());
        });

        registerArgument("visible",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setVisible(!bossBar.isVisible());
        });

        registerArgument("progress",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;

            bossBar.setProgress(command.getArgOrBlank(0).asFloatOrDefault(1));
        });

        registerArgument("remove",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            plugin.getBukkitPlayerManager().getPlayer(player).removeBossBar();
        });

        registerArgument("args",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;
            List<String> args = new ArrayList<>(command.getRawArgs());
            bossBar.setArgs(args.toArray());
        });

        registerArgument("color", command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;
            BarColor color = command.getArgOrBlank(0).asEnumOrDefault(BarColor.class,BarColor.PINK);
            bossBar.setColor(color);
        });

        registerArgument("style", command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;
            BarStyle color = command.getArgOrBlank(0).asEnumOrDefault(BarStyle.class, BarStyle.PROGRESS);
            bossBar.setStyle(color);
        });

        registerArgument("flags",command -> {
            Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
            BossBar bossBar = plugin.getBukkitPlayerManager().getPlayer(player).getBossBar();
            if(bossBar==null)
                return;
            boolean add = command.getArgOrBlank(0).asBooleanOrDefault(true);
            List<String> args = new ArrayList<>(command.getRawArgs());
            if(args.size()>0)
                args.remove(0);
            Set<BarFlag> flags = new HashSet<>();
            for(String arg : args) {
                flags.add(BarFlag.valueOf(arg.toUpperCase(Locale.ROOT)));
            }
            if(add) {
                bossBar.addFlags(flags.toArray(new BarFlag[0]));
            } else {
                bossBar.removeFlags(flags.toArray(new BarFlag[0]));
            }
        });
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());
        String name = command.getArgOrDefault(0,"Gamer").asString();
        float progress = command.getArgOrBlank(1).asFloatOrDefault(1f);
        BukkitApiPlayer p = plugin.getBukkitPlayerManager().getPlayer(player);
        BossBar bossBar = p.setBossBar("bb-1");
        bossBar.setMessage(name);
        bossBar.setProgress(progress);
    }
}
