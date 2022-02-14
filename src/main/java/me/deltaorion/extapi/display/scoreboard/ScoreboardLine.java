package me.deltaorion.extapi.display.scoreboard;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a line on a scoreboard. A line has
 *   - an index, indicating the position on the scoreboard, top being 0 and bottom being n where this line sits on the scoreboard
 *   - an optional unique name that can be used to identify a line.
 *   - a message that should be displayed.
 */
public class ScoreboardLine {
    @NotNull private final Message message;
    @Nullable private final String name;
    private final int line;
    private volatile String asDisplayed;

    /**
     *
     * @param message The message to display on scorebords line
     * @param name An optional name for the scoreboard line
     * @param line The line numba 
     */
    public ScoreboardLine(@NotNull Message message, @Nullable String name, int line) {
        this.message = message;
        this.name = name;
        this.line = line;
        this.asDisplayed = "";
    }

    /**
     * @return
     */
    @NotNull
    public Message getMessage() {
        return message;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public String getTeamName() {
        return getTeamName(line);
    }

    public static String getTeamName(int line) {
        return line + "";
    }

    @NotNull
    public String getAsDisplayed() {
        return asDisplayed;
    }

    public void setAsDisplayed(@NotNull String asDisplayed) {
        this.asDisplayed = java.util.Objects.requireNonNull(asDisplayed);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message",message)
                .add("index",line)
                .add("name",name).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ScoreboardLine))
            return false;

        ScoreboardLine line = (ScoreboardLine) o;

        return this.message.equals(line.message) && this.line == line.line && java.util.Objects.equals(line.name,this.name);
    }
}
