package me.deltaorion.extapi.test.unit.arg;

import me.deltaorion.extapi.command.CommandArg;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.test.TestEnum;
import me.deltaorion.extapi.test.unit.McTest;
import me.deltaorion.extapi.test.unit.MinecraftTest;

public class ArgTest implements MinecraftTest {

    private final EPlugin plugin;

    public ArgTest(EPlugin plugin) {
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
            assetEquals(arg.asInt(),123);
        } catch (CommandException e) {
            fail();
        }

        CommandArg arg2 = new CommandArg(plugin,"hello",0);
        try {
            assetEquals(arg2.parse(TestEnum.class),TestEnum.HELLO);
        } catch (CommandException e) {
            fail();
        }
    }
}
