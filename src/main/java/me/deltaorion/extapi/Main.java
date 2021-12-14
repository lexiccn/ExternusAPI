package me.deltaorion.extapi;

import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.StorageConfiguration;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.TranslationManager;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {

    public static Main main = new Main();

    public static void main(String[] args) {
        try {
            main.run();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void run() throws URISyntaxException {

        Path path = FileSystems.getDefault().getPath("C:\\Users\\User\\Documents\\abc\\translations");
        URL def = getClass().getClassLoader().getResource("en.yml");
        TranslationManager translationManager = new TranslationManager(path,def);
        System.out.println(Translator.translate("hello", Translator.parseLocale("en_PT")));
        System.out.println(Translator.translate("world.hello",Translator.parseLocale("en_PT")));

        Message middle = Message.valueOf("Gam%srs Unite!");
        System.out.println(middle.toString("e"));

        Message end = Message.valueOf("Gamer%s");
        System.out.println(end.toString("s"));

        Message start = Message.valueOf("%s Unite!");
        System.out.println(start.toString("Gamers"));

        Message translatable = Message.valueOfTranslatable("hello");
        System.out.println(translatable);
        System.out.println(translatable.toString(Translator.parseLocale("en_PT")));

        Message everything = Message.valueOfBuilder(builder -> {
            builder.appendTranslatable("hello")
                    .append(" &e %s %s %s ")
                    .style(ChatColor.BLACK)
                    .append(" Gamer");
        });

        System.out.println(everything.toString("gamer %s",true,6.5));
        System.out.println(everything.toString("abc"));

        Message defArgs = Message.valueOfBuilder( builder -> {
            builder.append("hello ")
                    .append("%s")
                    .defArg("world");
        });

        System.out.println(defArgs);
        System.out.println(defArgs.toString("gamer"));

        everything.setDefaults("a",7.5f,"b");

        System.out.println(everything.toString("abc"));
        System.out.println(everything.toString("a.b","bc","e"));

    }
}
