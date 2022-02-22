import com.google.common.collect.ImmutableList;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.command.sent.CommandArg;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.plugin.plugin.EPlugin;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.sender.TestSender;
import me.deltaorion.common.test.command.MessageCommand;
import me.deltaorion.common.test.command.TestCommand;
import me.deltaorion.common.test.mock.TestEnum;
import me.deltaorion.common.test.mock.TestPlugin;
import me.deltaorion.common.test.mock.TestServer;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

public class CommandTests {


    private final TestServer eServer = new TestServer();
    private final TestPlugin plugin = new TestPlugin(eServer);

    public CommandTests() {
        eServer.addPlugin("Test",plugin);
    }

    @Test
    public void basicArgTest() {

        CommandArg arg = new CommandArg(plugin,"hEllo",0);
        try {
            arg.asBoolean();
            fail();
        } catch (CommandException e) {

        }

        try {
            arg.asInt();
            fail();
        } catch (CommandException e) {

        }

        try {
            arg.asDuration();
            fail();
        } catch (CommandException e) {

        }


        assertEquals("hEllo",arg.asString());
        assertEquals("hEllo",arg.toString());

        CommandArg argBool = new CommandArg(plugin,"trUe",0);
        CommandArg argBool1 = new CommandArg(plugin,"falSe",0);
        CommandArg argBool3 = new CommandArg(plugin,"yEs",0);
        CommandArg argBool4 = new CommandArg(plugin,"nO",0);

        try {
            assertEquals(argBool.asString(),argBool.toString());
            assertTrue(argBool.asBoolean());
            assertFalse(argBool1.asBoolean());
            assertTrue(argBool3.asBoolean());
            assertFalse(argBool4.asBoolean());
            assertEquals(1,argBool1.asIntOrDefault(1));
            assertEquals(3.5f,argBool1.asFloatOrDefault(3.5f),0.001);
            assertEquals(4.7,argBool1.asDoubleOrDefault(4.7),0.001);
            Assert.assertEquals(TestEnum.WORLD,argBool1.asEnumOrDefault(TestEnum.class,TestEnum.WORLD));
            assertEquals(Duration.of(3,ChronoUnit.SECONDS),argBool1.asDurationOrDefault(Duration.of(3, ChronoUnit.SECONDS)));
            assertEquals(1,argBool1.asIntOrElse(str -> { return 1;}));
        } catch (Exception e) {
            fail();
        }

        CommandArg arg3 = new CommandArg(plugin,"1",0);
        try {
            assertEquals(1,arg3.asInt());
            assertEquals(1.0f,arg3.asFloat(),0.001);
            assertEquals(1.0,arg3.asDouble(),0.001);
        } catch (Exception e) {
            fail();
        }

        CommandArg arg4 = new CommandArg(plugin,"HeLlO",0);
        try {
            Assert.assertEquals(TestEnum.HELLO,arg4.asEnum(TestEnum.class,"Test Enum"));
        } catch (Exception e) {
            fail();
        }

        CommandArg arg5 = new CommandArg(plugin,"7d",0);
        try {
            assertEquals(Duration.of(7,ChronoUnit.DAYS),arg5.asDuration());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void parserTest() {
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
        TestEnum testEnum2 = plug.parseOrDefault(TestEnum.class,null);
        assertEquals(TestEnum.HELLO,testEnum);
        assertNull(testEnum2);
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
        assertEquals(TestEnum.DELTAORION,testEnum2);
        try {
            EPlugin a = plug.parse(TestPlugin.class);
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    public void sentTest() {
        Sender sender = plugin.getEServer().getConsoleSender();

        String[] args = {"Hello","World","Gamer"};
        SentCommand command = new SentCommand(plugin,sender,args,"Gamer","/gamer Hello World Gamer");

        try {
            assertEquals(command.getArgOrFail(0).toString(),"Hello");
            assertEquals(command.getArgOrFail(2).toString(),"Gamer");
        } catch (CommandException e) {
            fail();
        }

        try {
            command.getArgOrFail(3);
            fail();
        } catch (CommandException e) {

        }

        assertEquals(command.getArgOrDefault(3,"aaaa").toString(),"aaaa");
        assertEquals(command.getArgOrDefault(2,"aaaa").toString(),"Gamer");
        assertNull(command.getArgOrNull(-1));

        command = command.reduce("/gamer World Gamer");
        try {
            assertEquals(command.getArgOrFail(0).toString(),"World");
            assertEquals(command.getArgOrFail(1).toString(),"Gamer");
        } catch (CommandException e) {
            fail();
        }

        try {
            command.getArgOrFail(2);
            fail();
        } catch (CommandException e) {

        }

        command = command.reduce("/gamer Gamer");
        try {
            assertEquals("Gamer",command.getArgOrFail(0).toString());
        } catch (CommandException e) {
            e.printStackTrace();
        }

        assertEquals("",command.getArgOrBlank(1).asString());
        command = command.reduce("/gamer");
        assertEquals(0,command.argCount());
        try {
            command.getArgOrFail(0);
        } catch (CommandException e) {

        }
        command = command.reduce("/gamer");
        assertEquals(0,command.argCount());
        try {
            command.getArgOrFail(0);
        } catch (CommandException e) {

        }
        command = command.reduce("/gamer");
    }

    @Test
    public void commandTest() {
        TestServer server = new TestServer();
        TestPlugin plugin = new TestPlugin(server);
        server.addPlugin("Test",plugin);

        Sender permissionLess = new TestSender("jimmy", UUID.randomUUID(),false,false, Locale.ENGLISH);

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


        String[] args = {"gamer1"};
        SentCommand cMsg = new SentCommand(plugin,server.getOnlineSenders().get(3),args,"testcommand",command.getUsage());
        MessageCommand msg = new MessageCommand(plugin);
        assertEquals(ImmutableList.of("Gamer1","Gamer10","Gamer11","Gamer12","Gamer13","Gamer14","Gamer15","Gamer16","Gamer17","Gamer18","Gamer19"),msg.onTabCompletion(cMsg));
        String[] args2 = {"gamr"};
        SentCommand cMsg2 = new SentCommand(plugin,server.getOnlineSenders().get(3),args2,"testcommand",command.getUsage());
        assertEquals(Collections.emptyList(),msg.onTabCompletion(cMsg2));
        String[] args3 = {"Gamer1",""};
        SentCommand cMsg3 = new SentCommand(plugin,server.getOnlineSenders().get(3),args3,"testcommand",command.getUsage());
        assertEquals(Collections.emptyList(),msg.onTabCompletion(cMsg3));

        String[] args4 = {"path"};
        SentCommand tMsg = new SentCommand(plugin,server.getOnlineSenders().get(3),args4,"testcommand",command.getUsage());
        assertEquals(Collections.emptyList(),command.onTabCompletion(tMsg));
        SentCommand tMsg1 = new SentCommand(plugin,server.getOnlineSenders().get(6),args4,"testcommand",command.getUsage());
        assertEquals(ImmutableList.of("patha"),command.onTabCompletion(tMsg1));
        SentCommand tMsg2 = new SentCommand(plugin,server.getOnlineSenders().get(16),args4,"testcommand",command.getUsage());
        assertEquals(ImmutableList.of("patha","pathb","pathc"),command.onTabCompletion(tMsg2));
        String[] args5 = {""};
        SentCommand tMsg3 = new SentCommand(plugin,server.getOnlineSenders().get(16),args5,"testcommand",command.getUsage());
        assertEquals(ImmutableList.of("help","patha","pathb","pathc","0","1","2","3","4","5","6","7","8","9","10"),command.onTabCompletion(tMsg3));
        String[] args6 = {"PathC",""};
        SentCommand tMsg4 = new SentCommand(plugin,server.getOnlineSenders().get(6),args6,"testcommand",command.getUsage());
        assertEquals(ImmutableList.of("true","false"),command.onTabCompletion(tMsg4));
        SentCommand tMsg5 = new SentCommand(plugin,server.getOnlineSenders().get(18),args6,"testcommand",command.getUsage());
        assertEquals(ImmutableList.of("pathca","pathcb"),command.onTabCompletion(tMsg5));
        SentCommand tMsg6 = new SentCommand(plugin,permissionLess,args6,"testcommand",command.getUsage());
        assertEquals(Collections.emptyList(),command.onTabCompletion(tMsg6));
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
