package me.deltaorion.bukkit.plugin.sender;

import me.deltaorion.common.plugin.EServer;
import me.deltaorion.common.locale.translator.Translator;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class BukkitSenderInfo_8_11 extends BukkitSenderInfo {
    public BukkitSenderInfo_8_11(@NotNull EServer eServer, @NotNull Server server, @NotNull CommandSender sender) {
        super(eServer, server, sender);
    }

    @NotNull @Override @SuppressWarnings("deprecation")
    public Locale getLocale()  {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Locale locale = Translator.parseLocale(player.spigot().getLocale());
            if(locale==null) {
                return EServer.DEFAULT_LOCALE;
            } else {
                return locale;
            }
        } else {
            return EServer.DEFAULT_LOCALE;
        }
    }
}
