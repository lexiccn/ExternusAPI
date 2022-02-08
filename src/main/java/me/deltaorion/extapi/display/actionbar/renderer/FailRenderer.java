package me.deltaorion.extapi.display.actionbar.renderer;

import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;

public class FailRenderer implements ActionBarRenderer {
    @Override
    public void render(BukkitApiPlayer player, Message message, Object... args) {
        throw new RuntimeException();
    }
}
