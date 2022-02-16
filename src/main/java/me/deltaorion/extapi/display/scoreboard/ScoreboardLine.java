package me.deltaorion.extapi.display.scoreboard;

import com.google.common.base.MoreObjects;
import me.deltaorion.extapi.display.DisplayLine;
import me.deltaorion.extapi.display.SimpleDisplayLine;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a line on a scoreboard. A line has
 *   - an index, indicating the position on the scoreboard, top being 0 and bottom being n where this line sits on the scoreboard
 *   - an optional unique name that can be used to identify a line.
 *   - a message that should be displayed.
 */
public class ScoreboardLine implements DisplayLine {
    @NotNull private final DisplayLine display;
    @Nullable private final String name;
    private final int line;

    /**
     *
     * @param message The message to display on scoreboards line
     * @param name An optional name for the scoreboard line
     * @param line The positional index of this line on the scoreboard, 0 being on top and n being at the bottom
     * @param player The player who this line will be shown to
     * @param args Any message arguments to be displayed.
     */
    public ScoreboardLine(@NotNull Message message, @Nullable String name, int line, @NotNull BukkitApiPlayer player, Object... args) {
        this.display = new SimpleDisplayLine(player,message,args);
        this.name = name;
        this.line = line;
    }

    /**
     * @return
     */
    @NotNull
    public Message getMessage() {
        return display.getMessage();
    }

    @Override
    public void setArgs(Object... args) {
        display.setArgs(args);
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
        return display.getAsDisplayed();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("message",getMessage())
                .add("index",line)
                .add("name",name).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ScoreboardLine))
            return false;

        ScoreboardLine line = (ScoreboardLine) o;

        return Objects.equals(this.name,line.name) && this.line == line.line && display.equals(line.display);
    }
}
