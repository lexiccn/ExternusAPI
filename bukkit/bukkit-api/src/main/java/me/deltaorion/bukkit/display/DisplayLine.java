package me.deltaorion.bukkit.display;

import me.deltaorion.common.locale.message.Message;
import org.jetbrains.annotations.NotNull;

/**
 * A display line represents any line to be displayed to a user in a display item. A display line has the following properties
 *   - the underlying message is immutable, to get a new message one should replace the display line
 *   - The args can be changed at any time. Should the args change then the change should be reflected by changing {@link #getAsDisplayed()}
 *   - The locale of the sender can be changed. This change should be reflected in by changing {@link #getAsDisplayed()}
 */
public interface DisplayLine {

    /**
     * @return What the user currently see's on this display line on the display item.
     */
    @NotNull
    public String getAsDisplayed();

    /**
     * @return The message that is currently being rendered on the display item
     */
    @NotNull
    public Message getMessage();

    /**
     * Change the message arguments that are being displayed
     *   - should not alter the message
     *   - should push the change immediately so that it can be viewed by the user
     *
     * @param args The new message arguments to be displayed
     */
    public void setArgs(Object... args);
}
