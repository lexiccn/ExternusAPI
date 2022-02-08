package me.deltaorion.extapi.test.cmdold;

import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerTask;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.ChatColor;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class JointTests {

    public static void testSender(Sender sender) {
        sender.sendMessage("Name: " + sender.getName());
        sender.sendMessage("UUID: " + sender.getUniqueId());
        sender.sendMessage("Permission abc? " +sender.hasPermission("abc"));
        sender.sendMessage("OP: " + sender.isOP());
        sender.sendMessage("Valid: " + sender.isValid());
        sender.sendMessage("Locale: "+sender.getLocale());
        sender.sendMessage("Dispatching Command... ");
        sender.performCommand("help");
    }

    public static void serverTest(EServer eServer, EPlugin plugin, Sender sender) {
        plugin.getScheduler().runTaskLater(() -> {
            sender.sendMessage("This happens 1 second later");
        },1, TimeUnit.SECONDS);

        plugin.getScheduler().runTaskAsynchronously(() -> {
            sender.sendMessage("This runs async");
        });

        SchedulerTask schedulerTask =  plugin.getScheduler().runTaskTimer(() -> {
            sender.sendMessage("This will happen 3 times with an interval of 1 second");
        },3L,1L,TimeUnit.SECONDS);

        plugin.getScheduler().runTaskLaterAsynchronously(schedulerTask::cancel,6L,TimeUnit.SECONDS);

        sender.sendMessage("Version: " + eServer.getServerVersion());
        sender.sendMessage("Brand: " + eServer.getServerBrand());
        sender.sendMessage("Online Players: " + eServer.getOnlinePlayers());
        sender.sendMessage("Max-Player: " + eServer.getMaxPlayer());
        sender.sendMessage("Player Online: " + eServer.isPlayerOnline(sender.getUniqueId()));
        sender.sendMessage("Grabbing Console ... - Check Console");
        Sender console = eServer.getConsoleSender();
        console.sendMessage("Hello World!");
    }

    public static void translationTest(Sender sender) {
        sender.sendMessage(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")));
        sender.sendMessage(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")));

        Message middle = Message.valueOf("Gam%srs Unite!");
        sender.sendMessage(middle.toString("e"));

        Message end = Message.valueOf("Gamer%s");
        sender.sendMessage(end.toString("s"));

        Message start = Message.valueOf("%s Unite!");
        sender.sendMessage(start.toString("Gamers"));

        Message translatable = Message.valueOfTranslatable("hello");
        sender.sendMessage(translatable);
        sender.sendMessage(translatable.toString(Translator.parseLocale("en_PT")));
        sender.sendMessage(translatable.toString(Locale.CANADA_FRENCH));

        Message everything = Message.valueOfBuilder(builder -> {
            builder.appendTranslatable("hello")
                    .append(" &e %s %s %s ")
                    .style(ChatColor.BLACK)
                    .append(" Gamer");
        });

        sender.sendMessage(everything.toString("gamer %s",true,6.5));
        sender.sendMessage(everything.toString("abc"));

        Message defArgs = Message.valueOfBuilder( builder -> {
            builder.append("hello ")
                    .append("%s")
                    .defArg("world");
        });

        sender.sendMessage(defArgs);
        sender.sendMessage(defArgs.toString("gamer"));

        everything.setDefaults("a",7.5f,"b");

        sender.sendMessage(everything.toString("abc"));
        sender.sendMessage(everything.toString("a.b","bc","e"));
    }

}
