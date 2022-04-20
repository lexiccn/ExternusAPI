package me.deltaorion.common.test.shared;

import me.deltaorion.common.locale.IChatColor;
import me.deltaorion.common.locale.message.Message;
import me.deltaorion.common.locale.translator.Translator;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LocalizationTest {

    public void testTranslationManager() {
        try {
            assertEquals(Translator.getInstance().getDefaultTranslation("hello"),"Hello World");
            assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
            assertEquals(Translator.getInstance().getDefaultTranslation("world.hello"),"World");
            assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");
            assertEquals(Translator.getInstance().getDefaultTranslation("gamer"),"gamer");
        } catch (Exception e) {
            fail();
        }
    }

    public void testMessage() {
        assertEquals(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")),"World");

        Message middle = Message.valueOf("Gam{0}rs Unite!");
        assertEquals(middle.toString("e"),"Gamers Unite!");

        Message nullable = Message.valueOf("Insert {0} Null");
        String a = null;
        assertEquals("Insert null Null",nullable.toString(a));

        Message end = Message.valueOf("Gamer{0}");
        assertEquals(end.toString("s"),"Gamers");

        Message start = Message.valueOf("{0} Unite!");
        assertEquals(start.toString("Gamers"),"Gamers Unite!");

        Message translatable = Message.valueOfTranslatable("hello");
        assertEquals(translatable.toString(),"Hello World");
        assertEquals(translatable.toString(Translator.parseLocale("en_PT")),"Ahoy There Mateys");
        assertEquals(translatable.toString(Locale.FRANCE),"Bonjour");
        assertEquals(translatable.toString(Locale.CANADA_FRENCH),"Bonjour");

        Message trick = Message.valueOf("Gamer{{0}}s Unite");
        assertEquals("Gamer{arg}s Unite",trick.toString("arg"));

        Message trick2 = Message.valueOf("Gamer{0}{0} Unite");
        assertEquals("Gamerrr Unite",trick2.toString("r"));

        Message trick3 = Message.valueOf("Gamer{}s Unite");
        assertEquals("Gamer{}s Unite",trick3.toString("r"));

        Message trick4 = Message.valueOf("Gamer{ 0} Unite");
        assertEquals("Gamer{ 0} Unite",trick4.toString("reoijgr"));

        Message trick5 = Message.valueOf("Gamer{{0} Unite");
        assertEquals("Gamer{s Unite",trick5.toString("s"));

        Message trick6 = Message.valueOf("Gamer{0 Unite");
        assertEquals("Gamer{0 Unite",trick6.toString("{0}"));

        Message trick8 = Message.valueOf("Hello \\{0}");
        assertEquals("Hello {0}",trick8.toString("eropkgepork","eoiwkwe","eowkpfw"));

        Message trick9 = Message.valueOf("Hello {10}");
        assertEquals("Hello 10",trick9.toString("0","1","2","3","4","5","6","7","8","9","10"));

        Message trick7 = Message.valueOf("{{0}}}{1}{{1}{2}");
        assertEquals("{{0}}}{0} {1}{{0} {1}{}",trick7.toString("{0}","{0} {1}","{}"));
        assertEquals("{3}}3{33",trick7.toString(3,3,3,3,3,3,3,3,3,3,3));

        Message everything = Message.valueOfBuilder(builder -> {
            builder.appendTranslatable("hello")
                    .append(" &e {0} {1} {2} ")
                    .style(IChatColor.BLACK)
                    .append(" Gamer");
        });

        assertEquals(everything.toString("gamer \\{0}",true,6.5),"Hello World §e gamer \\{0} true 6.5 §0 Gamer");
        assertEquals(everything.toString("abc"),"Hello World §e abc {1} {2} §0 Gamer");

        Message defArgs = Message.valueOfBuilder( builder -> {
            builder.append("hello ")
                    .append("{0}")
                    .defArg("world");
        });

        assertEquals(defArgs.toString(),"hello world");
        assertEquals(defArgs.toString("gamer"),"hello gamer");

        everything.setDefaults("a",7.5f,"b");

        assertEquals(everything.toString("abc"),"Hello World §e abc 7.5 b §0 Gamer");
        assertEquals(everything.toString("a.b","bc","e"),"Hello World §e a.b bc e §0 Gamer");
    }
}
