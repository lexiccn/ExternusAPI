package me.deltaorion.extapi.common.sender.bukkit;

import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.Translator;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class BukkitSenderInfo_12 extends BukkitSenderInfo {
    public BukkitSenderInfo_12(@NotNull EServer eServer, @NotNull Server server, @NotNull CommandSender sender) {
        super(eServer, server, sender);
    }

    @NotNull
    @Override
    public Locale getLocale() {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Locale locale = Translator.parseLocale(player.getLocale());
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
