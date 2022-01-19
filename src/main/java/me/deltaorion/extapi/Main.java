package me.deltaorion.extapi;

import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.TestSender;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.PluginConfiguration;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.TranslationManager;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.ChatColor;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

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

        TranslationManager manager = new TranslationManager(FileSystems.getDefault().getPath("C:\\Users\\User\\Documents\\abc\\translations"),"en.yml",getClass());
        manager.reload();
        Sender sender = new TestSender("Gamer", UUID.randomUUID(),true,false, Locale.ENGLISH,"abc");
        sender.sendMessage(Message.valueOfTranslatable("hello-arg"),"Gamer!");
    }
}
