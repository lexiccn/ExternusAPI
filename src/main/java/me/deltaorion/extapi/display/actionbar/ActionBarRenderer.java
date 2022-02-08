package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;

public interface ActionBarRenderer {

    public void render(BukkitApiPlayer player, Message message, Object... args);
}
