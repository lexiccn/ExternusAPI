package me.deltaorion.extapi;

import me.deltaorion.extapi.command.CommandArg;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.SimpleParserRegistry;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.TestSender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.PluginConfiguration;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.locale.translator.TranslationManager;
import me.deltaorion.extapi.locale.translator.Translator;
import me.deltaorion.extapi.test.TestEnum;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import org.bukkit.ChatColor;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
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

        TestServer server = new TestServer();
        TestPlugin plugin = new TestPlugin(server);
        server.addPlugin("Test",plugin);

        CommandArg pass = new CommandArg(plugin,"HELLO",0);
        CommandArg fail = new CommandArg(plugin,"DeltaOrion",1);
        CommandArg plug = new CommandArg(plugin,"abc",2);

        plugin.registerParser(TestEnum.class, new ArgumentParser<TestEnum>() {
            @Override
            public TestEnum parse(String arg) throws CommandException {
                try {
                    return TestEnum.valueOf(arg);
                } catch (IllegalArgumentException e) {
                    throw new CommandException("Cannot resolve test enum");
                }
            }
        });

        plugin.registerParser(TestPlugin.class, new ArgumentParser<TestPlugin>() {
            @Override
            public TestPlugin parse(String arg) throws CommandException {
                TestServer server = new TestServer();
                TestPlugin plugin = new TestPlugin(server);
                server.addPlugin(arg,plugin);
                return plugin;
            }
        });

        TestEnum testEnum = pass.parseOrDefault(TestEnum.class,null);
        TestEnum testEnum2 = fail.parseOrDefault(TestEnum.class,null);
        System.out.println(testEnum);
        System.out.println(testEnum2);
        plugin.registerParser(TestEnum.class, new ArgumentParser<TestEnum>() {
            @Override
            public TestEnum parse(String arg) throws CommandException {
                try {
                    return TestEnum.valueOf(arg.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new CommandException("cannot resolve test enum");
                }
            }
        });
        testEnum2 = fail.parseOrDefault(TestEnum.class,null);
        System.out.println(testEnum2);
        try {
            EPlugin a = plug.parse(TestPlugin.class);
            System.out.println(a);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }


}
