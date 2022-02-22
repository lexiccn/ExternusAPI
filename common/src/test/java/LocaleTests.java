import me.deltaorion.common.locale.ChatColor;
import me.deltaorion.common.locale.message.Message;
import me.deltaorion.common.locale.translator.TranslationManager;
import me.deltaorion.common.locale.translator.Translator;
import me.deltaorion.common.test.shared.LocalizationTest;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleTests {

    private final LocalizationTest test = new LocalizationTest();

    @Test
    public void testTranslationManager() {
        try {
            URI directory = getClass().getClassLoader().getResource("translations").toURI();
            TranslationManager translationManager = new TranslationManager(new File(directory).toPath(), "en.properties");
            translationManager.reload();
            test.testTranslationManager();
        } catch (Exception e) {
            fail();
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
        TranslationManager translationManager = new TranslationManager(path, "en.properties");
        translationManager.reload();
        test.testMessage();
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
