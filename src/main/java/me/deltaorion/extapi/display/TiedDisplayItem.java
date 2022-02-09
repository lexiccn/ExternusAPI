package me.deltaorion.extapi.display;

import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Interface to represent a display item that is
 *   - tied to the player
 *   - visibility can be toggled
 */
public interface TiedDisplayItem {

    /**
     * @return The player this display item is tied to
     */
    @NotNull
    public BukkitApiPlayer getPlayer();

    /**
     * Toggles whether the display item should be visible to the user.
     *    - If visible the user will see the display item
     *    - If invisible the user will still have the display item but will simply not be able to see it.
     *
     *  To actually remove the display item use the appropriate remove method.
     *
     * @param visible Whether the display item is visible or not to the user.
     */
    public void setVisible(boolean visible);

    /**
     *    - If visible the user will see the display item
     *    - If invisible the user will still have the display item but will simply not be able to see it.
     *
     *  To actually remove the display item use the appropriate remove method.
     *
     * @return Whether the display item is visible or not to the user.
     */
    public boolean isVisible();
}
