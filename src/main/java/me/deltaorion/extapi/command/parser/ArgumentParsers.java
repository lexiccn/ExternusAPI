package me.deltaorion.extapi.command.parser;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.test.TestEnum;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public class ArgumentParsers {

    private ArgumentParsers() {
        throw new UnsupportedOperationException();
    }

    public static ArgumentParser<Player> BUKKIT_PLAYER_PARSER(Plugin plugin) {
        return new ArgumentParser<Player>() {
            @Override
            public Player parse(String arg) throws CommandException {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (player.getName().equalsIgnoreCase(arg)) {
                        return player;
                    }
                }
                return null;
            }
        };
    }

    public static ArgumentParser<ProxiedPlayer> BUNGEE_PLAYER_PARSER(net.md_5.bungee.api.plugin.Plugin plugin) {
        return new ArgumentParser<ProxiedPlayer>() {
            @Override
            public ProxiedPlayer parse(String arg) throws CommandException {
                for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                    if (player.getName().equalsIgnoreCase(arg)) {
                        return player;
                    }
                }
                return null;
            }
        };
    }

    public static ArgumentParser<TestEnum> TEST_PARSER() {
        return new ArgumentParser<TestEnum>() {
            @Override
            public TestEnum parse(String arg) throws CommandException {
                try {
                    return TestEnum.valueOf(arg.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException e) {
                    throw new CommandException("Cannot resolve test enum");
                }
            }
        };
    }
}
