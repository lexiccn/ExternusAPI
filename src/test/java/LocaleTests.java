import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.TranslationManager;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.ChatColor;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class LocaleTests {

    @Test
    public void testTranslationManager() {
        try {
            URI directory = getClass().getClassLoader().getResource("translations").toURI();
            URL def = getClass().getClassLoader().getResource("en.yml");
            TranslationManager translationManager = new TranslationManager(new File(directory).toPath(),def);
            assertEquals(Translator.getDefaultTranslation("hello"),"Hello World");
            assertEquals(Translator.translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
            assertEquals(Translator.getDefaultTranslation("world.hello"),"World");
            assertEquals(Translator.translate("world.hello",Translator.parseLocale("en_PT")),"World");
            assertEquals(Translator.getDefaultTranslation("gamer"),"gamer");
        } catch (Exception e) {
            assertEquals(2,3);
        }
    }

    @Test
    public void testMessage() throws URISyntaxException {
        Path path = FileSystems.getDefault().getPath("C:\\Users\\User\\Documents\\abc\\translations");
        URL def = getClass().getClassLoader().getResource("en.yml");
        TranslationManager translationManager = new TranslationManager(path,def);
        assertEquals(Translator.translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(Translator.translate("world.hello",Translator.parseLocale("en_PT")),"World");

        Message middle = Message.valueOf("Gam%srs Unite!");
        assertEquals(middle.toString("e"),"Gamers Unite!");

        Message end = Message.valueOf("Gamer%s");
        assertEquals(end.toString("s"),"Gamers");

        Message start = Message.valueOf("%s Unite!");
        assertEquals(start.toString("Gamers"),"Gamers Unite!");

        Message translatable = Message.valueOfTranslatable("hello");
        assertEquals(translatable.toString(),"Hello World");
        assertEquals(translatable.toString(Translator.parseLocale("en_PT")),"Ahoy There Mateys");

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
}
