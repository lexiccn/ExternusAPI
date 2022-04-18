package me.deltaorion.bukkit.display.actionbar.renderer;

import me.deltaorion.bukkit.display.actionbar.ActionBarRenderer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FailRenderer implements ActionBarRenderer {

    @Override
    public void render(@NotNull Player player, @NotNull String render) {
        throw new RuntimeException();
    }
}
