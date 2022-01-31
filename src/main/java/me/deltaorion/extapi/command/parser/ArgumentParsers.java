package me.deltaorion.extapi.command.parser;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.sent.MessageErrors;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.test.TestEnum;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

/**
 * Simple static class containing some common argument parsers
 */
public class ArgumentParsers {

    private ArgumentParsers() {
        throw new UnsupportedOperationException();
    }

    /*
     * Takes a string, checks if there is a player online with that username (ignoring case).
     */
    public static ArgumentParser<Player> BUKKIT_PLAYER_PARSER(Plugin plugin) {
        return arg -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(arg)) {
                    return player;
                }
            }
            return null;
        };
    }

    /*
     * Takes a string, checks if there is a proxied player online with that user`name (ignoring case).
     */
    public static ArgumentParser<ProxiedPlayer> BUNGEE_PLAYER_PARSER(net.md_5.bungee.api.plugin.Plugin plugin) {
        return arg -> {
            for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                if (player.getName().equalsIgnoreCase(arg)) {
                    return player;
                }
            }
            return null;
        };
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
