package me.deltaorion.extapi.display.actionbar.renderer;

import me.deltaorion.extapi.display.actionbar.ActionBarRenderer;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WrapperActionBarRenderer implements ActionBarRenderer {
    @Override
    public void render(@NotNull Player player, @NotNull String render) {
        player.sendActionBar(render);
    }
}
