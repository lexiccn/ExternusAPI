package me.deltaorion.extapi.display.actionbar.renderer;

import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FailRenderer implements ActionBarRenderer {

    @Override
    public void render(@NotNull Player player, @NotNull String render) {
        throw new RuntimeException();
    }
}
