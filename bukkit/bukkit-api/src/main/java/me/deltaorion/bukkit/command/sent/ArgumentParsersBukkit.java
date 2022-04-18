package me.deltaorion.bukkit.command.sent;

import me.deltaorion.common.command.parser.ArgumentParser;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ArgumentParsersBukkit {

    private ArgumentParsersBukkit() {
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
}
