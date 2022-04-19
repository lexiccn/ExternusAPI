package me.deltaorion.common.command.parser;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.plugin.EServer;
import me.deltaorion.common.plugin.sender.Sender;

/**
 * Simple static class containing some common argument parsers
 */
public class ArgumentParsers {

    private ArgumentParsers() {
        throw new UnsupportedOperationException();
    }

    /*
     * Takes a string, checks if there is a player sender online with that username (ignoring case).
     */
    public static ArgumentParser<Sender> SENDER_PARSER(EServer server) {
        return (sender, arg) -> {
            Sender senderFromArg  = server.getSenderExact(arg);
            if(sender==null) {
                throw new CommandException(MessageErrors.NOT_ONLINE_PLAYER().toString(arg));
            }
            return senderFromArg;
        };
    }
}
