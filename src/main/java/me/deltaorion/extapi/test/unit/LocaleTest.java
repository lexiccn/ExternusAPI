package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.FileConfiguration;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.Translator;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import org.bukkit.ChatColor;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class LocaleTest implements MinecraftTest {

    private final ApiPlugin plugin;

    public LocaleTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void testTranslationManager() {

        Configuration configFr = new FileConfiguration(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("fr.yml"),"translations/fr.yml");
        Configuration configPT = new FileConfiguration(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("en_PT.yml"),"translations/en_PT.yml");

        plugin.getTranslator().reload();

        try {
            assertEquals(Translator.getInstance().getDefaultTranslation("hello"),"Hello World");
            assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
            assertEquals(Translator.getInstance().getDefaultTranslation("world.hello"),"World");
            assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");
            assertEquals(Translator.getInstance().getDefaultTranslation("gamer"),"gamer");
        } catch (Exception e) {
            assertEquals(2,3);
        }
    }

    @McTest
    public void testMessage() {

        Configuration configFr = new FileConfiguration(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("fr.yml"),"translations/fr.yml");
        Configuration configPT = new FileConfiguration(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("en_PT.yml"),"translations/en_PT.yml");
        plugin.getTranslator().reload();

        assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");

        Message middle = Message.valueOf("Gam%srs Unite!");
        assertEquals(middle.toString("e"),"Gamers Unite!");

        Message nullable = Message.valueOf("Insert %s Null");
        String a = null;
        assertEquals(nullable.toString(a),"Insert null Null");

        Message end = Message.valueOf("Gamer%s");
        assertEquals(end.toString("s"),"Gamers");

        Message start = Message.valueOf("%s Unite!");
        assertEquals(start.toString("Gamers"),"Gamers Unite!");

        Message translatable = Message.valueOfTranslatable("hello");
        assertEquals(translatable.toString(),"Hello World");
        assertEquals(translatable.toString(Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(translatable.toString(Locale.FRANCE),"Bonjour");
        assertEquals(translatable.toString(Locale.CANADA_FRENCH),"Bonjour");

        Message everything = Message.valueOfBuilder(builder -> {
            builder.appendTranslatable("hello")
                    .append(" &e %s %s %s ")
                    .style(ChatColor.BLACK)
                    .append(" Gamer");
        });

        assertEquals(everything.toString("gamer %s",true,6.5),"Hello World §e gamer %s true 6.5 §0 Gamer");
        assertEquals(everything.toString("abc"),"Hello World §e abc %s %s §0 Gamer");

        Message defArgs = Message.valueOfBuilder( builder -> {
            builder.append("hello ")
                    .append("%s")
                    .defArg("world");
        });

        assertEquals(defArgs.toString(),"hello world");
        assertEquals(defArgs.toString("gamer"),"hello gamer");

        everything.setDefaults("a",7.5f,"b");

        assertEquals(everything.toString("abc"),"Hello World §e abc 7.5 b §0 Gamer");
        assertEquals(everything.toString("a.b","bc","e"),"Hello World §e a.b bc e §0 Gamer");
    }

    @Override
    public String getName() {
        return "Locale Test";
    }
}
