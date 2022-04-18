package me.deltaorion.common;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.test.mock.TestEnum;

import java.util.Locale;

public class TestArgumentParsers {

    private TestArgumentParsers() {
        throw new UnsupportedOperationException();
    }

    public static ArgumentParser<TestEnum> TEST_PARSER() {
        return arg -> {
            try {
                return TestEnum.valueOf(arg.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new CommandException("Cannot resolve test enum");
            }
        };
    }
}
