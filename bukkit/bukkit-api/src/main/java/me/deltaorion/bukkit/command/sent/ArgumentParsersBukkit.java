package me.deltaorion.bukkit.command.sent;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.command.sent.CommandArg;
import me.deltaorion.common.command.sent.MessageErrors;
import org.bukkit.Material;
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
        return (sender, arg) -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(arg)) {
                    return player;
                }
            }
            throw new CommandException(MessageErrors.NOT_ONLINE_PLAYER().toString(sender.getLocale(),arg));
        };
    }

    public static ArgumentParser<Material> MATERIAL_PARSER() {
        return (sender, arg) -> {
            Material material = Material.matchMaterial(arg);
            if(material==null)
                throw new CommandException(MessageErrors.NOT_MATERIAL().toString(sender.getLocale(),arg));

            return material;
        };
    }

    public static ArgumentParser<EMaterial> EMATERIAL_PARSER() {
        return (sender, arg) -> {
            EMaterial material = EMaterial.matchMaterial(arg);
            if(material==null)
                throw new CommandException(MessageErrors.NOT_MATERIAL().toString(sender.getLocale(),arg));

            return material;
        };
    }
}
