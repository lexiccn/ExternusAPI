package me.deltaorion.extapi;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.command.tabcompletion.Trie;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.TestSender;
import me.deltaorion.extapi.test.command.MessageCommand;
import me.deltaorion.extapi.test.command.TestCommand;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.net.URISyntaxException;
import java.util.*;

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

        Sender permissionLess = new TestSender("jimmy",UUID.randomUUID(),false,false, Locale.ENGLISH);

        TestCommand command = new TestCommand();
        FunctionalCommand fromBuild = new FunctionalCommand.Builder()
                .setPermission("1")
                .onCommand(c -> {
                    c.getSender().sendMessage("Hello World");
                })
                .addArgument("hello", builder -> {
                    builder.setPermission("2")
                            .onCommand(c -> {
                                throw new CommandException("Hahahaha");
                            })
                            .addArgument("world", world -> {
                                world.setPermission("3");
                                world.onCommand(c -> {
                                    c.getSender().sendMessage("Super Secret Command");
                                });
                            });
                }).build();


        String[] args = {"game"};
        SentCommand c = new SentCommand(plugin,server.getOnlineSenders().get(3),args,"testcommand",command.getUsage());
        System.out.println(command.onTabCompletion(c));
        fromBuild.onCommand(c);
        System.out.println(fromBuild.onTabCompletion(c));

        MessageCommand msg = new MessageCommand(plugin);
        msg.onCommand(c);
        System.out.println(msg.onTabCompletion(c));

        List<String> words= new ArrayList<>();
        for(int i=0;i<100000;i++) {
            words.add(randomString());
        }

        long now = System.currentTimeMillis();
        Trie trie = new Trie();
        trie.insert(words);
        System.out.println("Time -- "+(System.currentTimeMillis()-now));

        now = System.currentTimeMillis();
        List<String> autocomplete = trie.search("aaa");
        System.out.println("Time -- "+(System.currentTimeMillis()-now));
        System.out.println(autocomplete);

        autocomplete = new ArrayList<>();
        now = System.currentTimeMillis();
        for(String word : words) {
            if(word.startsWith("aaa")) {
                autocomplete.add(word);
            }
        }
        System.out.println("Time -- "+(System.currentTimeMillis()-now));
        System.out.println(autocomplete);
    }

    public String randomString() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }


}
