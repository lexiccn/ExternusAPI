import me.deltaorion.extapi.command.CommandArg;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.TestEnum;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import org.bukkit.Material;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

public class ArgTest {

    @Test
    public void basicArgTest() {
        TestServer eServer = new TestServer();
        TestPlugin plugin = new TestPlugin(eServer);

        CommandArg arg = new CommandArg(plugin,"hello",0);
        try {
            arg.asBoolean();
            fail();
        } catch (Exception e) {

        }

        assertEquals("hello",arg.asString());
        assertEquals("hello",arg.toString());

        CommandArg argBool = new CommandArg(plugin,"trUe",0);
        CommandArg argBool1 = new CommandArg(plugin,"falSe",0);
        CommandArg argBool3 = new CommandArg(plugin,"yEs",0);
        CommandArg argBool4 = new CommandArg(plugin,"nO",0);

        try {
            assertTrue(argBool.asBoolean());
            assertFalse(argBool1.asBoolean());
            assertTrue(argBool3.asBoolean());
            assertFalse(argBool4.asBoolean());
            assertEquals(1,argBool1.asIntOrDefault(1));
            assertEquals(3.5f,argBool1.asFloatOrDefault(3.5f),0.001);
            assertEquals(4.7,argBool1.asDoubleOrDefault(4.7),0.001);
            assertEquals(Material.ACACIA_DOOR,argBool1.asEnumOrDefault(Material.class,Material.ACACIA_DOOR));
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

        CommandArg arg4 = new CommandArg(plugin,"IRON_INgOT",0);
        try {
            assertEquals(Material.IRON_INGOT,arg4.asEnum(Material.class,"material"));
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
}
