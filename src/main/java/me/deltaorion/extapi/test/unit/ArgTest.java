package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.command.sent.CommandArg;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.test.TestEnum;
import me.deltaorion.extapi.test.command.FailCommand;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;

import static org.junit.Assert.*;

public class ArgTest implements MinecraftTest {

    private final ApiPlugin plugin;

    public ArgTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void basicTest() {
        CommandArg arg = new CommandArg(plugin,"123",0);
        try {
            arg.asBoolean();
            fail();
        } catch (Exception e) {

        }
        try {
            assertEquals(arg.asInt(),123);
        } catch (CommandException e) {
            fail();
        }

        CommandArg arg2 = new CommandArg(plugin,"hello",0);
        try {
            assertEquals(arg2.parse(TestEnum.class),TestEnum.HELLO);
        } catch (CommandException e) {
            fail();
        }
    }

    @McTest
    public void registerTest() {
        try {
            plugin.registerCommand(new FailCommand());
            fail();
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public String getName() {
        return "Command Argument Test";
    }
}
