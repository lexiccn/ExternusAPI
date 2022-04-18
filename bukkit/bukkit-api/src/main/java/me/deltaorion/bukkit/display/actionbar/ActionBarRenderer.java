package me.deltaorion.bukkit.display.actionbar;

import me.deltaorion.bukkit.display.actionbar.renderer.PacketActionBarRenderer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation for how an action bar should be rendered. This class should define how to render an action bar string
 * to the player. Whether that be through sending packets or using some kind of bukkit method.
 *
 * This class is not a manager for an action bar but simply defines how to send the action bar to the player.
 *
 * Implementations
 *   - {@link PacketActionBarRenderer}
 */
public interface ActionBarRenderer {

    /**
     * Sends an action bar with the given text to the player
     *
     * @param player The player to send to
     * @param render What to render to the screen
     */
    public void render(@NotNull Player player, @NotNull String render);
}
