package me.deltaorion.common.command.parser;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.EServer;
import me.deltaorion.common.test.mock.TestEnum;

import java.util.Locale;

/**
 * Simple static class containing some common argument parsers
 */
public class ArgumentParsers {

    private ArgumentParsers() {
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

    /*
     * Takes a string, checks if there is a player sender online with that username (ignoring case).
     */
    public static ArgumentParser<Sender> SENDER_PARSER(EServer server) {
        return arg -> {
            Sender sender  = server.getSenderExact(arg);
            if(sender==null) {
                throw new CommandException(MessageErrors.NOT_ONLINE_PLAYER().toString(arg));
            }
            return sender;
        };
    }
}
