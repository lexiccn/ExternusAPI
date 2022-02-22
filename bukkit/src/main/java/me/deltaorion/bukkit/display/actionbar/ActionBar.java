package me.deltaorion.bukkit.display.actionbar;

import com.google.common.base.MoreObjects;
import me.deltaorion.bukkit.display.bukkit.EApiPlayer;
import me.deltaorion.common.locale.message.Message;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

/**
 * An Action Bar is a display item which shows a line of text at the bottom of the players screen just on top of where item names
 * are normally displayed. This class represents the contents of an action bar that can be sent to a user. To actually send one use
 * {@link EApiPlayer#getActionBarManager()}.
 *
 * Action bars have
 *   - a optional name that can identify the action bar
 *   - a duration for how long it is shown to the user
 *   - the message that is displayed to the player
 */
@Immutable
public class ActionBar {

    @Nullable private final String name;
    @NotNull private final Message message;
    @NotNull private final Duration duration;

    /**
     * Creates an action bar. If the duration is less than 3s then the action bar will not fade out.
     *
     * @param message The message to show to the player
     * @param duration How long the action bar should be displayed for.
     * @param name An optional name for this action bar to identify it.
     */
    public ActionBar(@NotNull Message message, @NotNull Duration duration, @Nullable String name) {
        this.message = message;
        this.duration = duration;
        this.name = name;
    }
    /**
     * Creates an action bar. If the duration is less than 3s then the action bar will not fade out. The action
     * bar will have no name
     *
     * @param message The message to show to the player
     * @param duration How long the action bar should be displayed for.
     */
    public ActionBar(@NotNull Message message, @NotNull Duration duration) {
        this(message,duration,null);
    }

    /**
     * Creates an action bar. If the duration is less than 3s then the action bar will not fade out. The action
     * bar will have no name
     *
     * @param message The message to show to the player
     * @param duration How long the action bar should be displayed for.
     */
    public ActionBar(@NotNull String message, @NotNull Duration duration) {
        this(Message.valueOf(message),duration);
    }

    /**
     *
     * @return The message to display to the player
     */
    @NotNull
    public Message getMessage() {
        return message;
    }

    /**
     *
     * @return The duration of the action bar in millis
     */
    public long getTime() {
        return duration.toMillis();
    }

    /**
     *
     * @return The name of the action bar, null if there is no name
     */
    @Nullable
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Message",message)
                .add("Duration (ms)",getTime()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ActionBar))
            return false;

        ActionBar bar = (ActionBar) o;
        return bar.message.equals(this.message) && bar.getTime() == this.getTime();
    }
}
