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
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleTests {

    @Test
    public void testTranslationManager() {
        try {
            URI directory = getClass().getClassLoader().getResource("translations").toURI();
            TranslationManager translationManager = new TranslationManager(new File(directory).toPath(),"en.yml");
            translationManager.reload();
            assertEquals(Translator.getInstance().getDefaultTranslation("hello"),"Hello World");
            assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
            assertEquals(Translator.getInstance().getDefaultTranslation("world.hello"),"World");
            assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");
            assertEquals(Translator.getInstance().getDefaultTranslation("gamer"),"gamer");
        } catch (Exception e) {
            assertEquals(2,3);
        }
    }

    @Test
    public void testMessage() {
        Path path = null;
        try {
            path = new File(getClass().getClassLoader().getResource("translations").toURI()).toPath();
        } catch (Exception e) {
            fail();
        }
        TranslationManager translationManager = new TranslationManager(path,"en.yml");
        translationManager.reload();
        assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");

        Message middle = Message.valueOf("Gam%srs Unite!");
        assertEquals(middle.toString("e"),"Gamers Unite!");

        Message nullable = Message.valueOf("Insert %s Null");
        String a = null;
        assertEquals("Insert null Null",nullable.toString(a));

        Message end = Message.valueOf("Gamer%s");
        assertEquals(end.toString("s"),"Gamers");

        Message start = Message.valueOf("%s Unite!");
        assertEquals(start.toString("Gamers"),"Gamers Unite!");

        Message translatable = Message.valueOfTranslatable("hello");
        assertEquals(translatable.toString(),"Hello World");
        assertEquals(translatable.toString(Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(translatable.toString(Locale.FRANCE),"Bonjour");
        assertEquals(translatable.toString(Locale.CANADA_FRENCH),"Bonjour");

        Message trick = Message.valueOf("Gamer%rs Unite");
        assertEquals("Gamer%rs Unite",trick.toString("otij"));

        Message trick2 = Message.valueOf("Gamer%ss Unite");
        assertEquals("Gamerrs Unite",trick2.toString("r"));

        Message trick3 = Message.valueOf("Gamer%Ss Unite");
        assertEquals("Gamer%Ss Unite",trick3.toString("r"));

        Message trick4 = Message.valueOf("Gamer% s Unite");
        assertEquals("Gamer% s Unite",trick4.toString("reoijgr"));

        Message trick5 = Message.valueOf("Gamer%%s Unite");
        assertEquals("Gamer%s Unite",trick5.toString("s"));

        Message trick6 = Message.valueOf("Gamer%%s Unite");
        assertEquals("Gamer%%s Unite",trick6.toString("%s"));

        Message trick7 = Message.valueOf("%s%s%s%ss%%%ss%s");
        assertEquals("%s%s%s%ss%%%ss%s",trick7.toString("%s","%s","%s","%s","%s","%s","%s"));
        assertEquals("3333s%%3s3",trick7.toString(3,3,3,3,3,3,3,3,3,3,3));

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

    @Test
    public void testEquals() {
        Message message = Message.valueOf("");
        Message copy = Message.valueOf("");
        assertEquals(message, message);
        assertEquals(message,copy);

        Message integer = Message.valueOf(3);
        assertEquals(integer,integer);
        assertEquals(integer,Message.valueOf(3));

        Message stringify = Message.valueOf("hello");
        Message translatable = Message.valueOfTranslatable("hello");

        assertNotSame(stringify,translatable);
        assertNotSame(message,integer);
        assertEquals(translatable,translatable);
        assertEquals(translatable,Message.valueOfTranslatable("hello"));

        copy.setDefaults("Gamer");
        assertNotSame(copy,message);

        Message builder = Message.valueOfBuilder(builder1 -> {
            builder1.append(3)
                    .append("gamer")
                    .appendTranslatable("hello");
        });

        Message copyBuilder = Message.valueOfBuilder(builder1 -> {
            builder1.append(3)
                    .append("gamer")
                    .appendTranslatable("hello");
        });

        Message notCopyBuilder = Message.valueOfBuilder(builder1 -> {
            builder1.append(4)
                    .append("gamer")
                    .appendTranslatable("hello");
        });

        assertEquals(builder,copyBuilder);
        assertNotSame(copyBuilder,notCopyBuilder);
        copyBuilder.setDefaults("gamer");
        assertNotSame(builder,copyBuilder);

    }
}
