package me.deltaorion.bungee.command.sent;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.command.sent.MessageErrors;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ArgumentParsersBungee {

    private ArgumentParsersBungee() {
        throw new UnsupportedOperationException();
    }

    /*
     * Takes a string, checks if there is a proxied player online with that user`name (ignoring case).
     */
    public static ArgumentParser<ProxiedPlayer> BUNGEE_PLAYER_PARSER(net.md_5.bungee.api.plugin.Plugin plugin) {
        return (sender, arg) -> {
            for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                if (player.getName().equalsIgnoreCase(arg)) {
                    return player;
                }
            }
            throw new CommandException(MessageErrors.NOT_ONLINE_PLAYER().toString(sender.getLocale(),arg));
        };
    }
}
