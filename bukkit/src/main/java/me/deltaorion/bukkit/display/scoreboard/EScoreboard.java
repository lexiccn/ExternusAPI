package me.deltaorion.bukkit.display.scoreboard;

import me.deltaorion.bukkit.display.TiedDisplayItem;
import me.deltaorion.common.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A scoreboard is a data structure that represents a panel that is displayed on the side of the users screen. A scoreboard
 * has
 *  - A title that can be modified
 *  - n lines, up to 16
 *  - Each line can be modified at any time. A line can have up to 32 characters (1.8.8-1.12) or 128 (1.13+) characters
 *  - Each line is represented by a positional index, i.e its position relative to the top (top line = 0, bottom = n) and
 *    an optional unique name to identify it.
 *
 *
 * If using an {@link EScoreboard} over a {@link org.bukkit.scoreboard.Scoreboard} the original scoreboard should not be
 * mutated or updated.
 *
 * Implementations
 *   - {@link WrapperScoreboard} wraps around a bukkit scoreboard
 */
public interface EScoreboard extends TiedDisplayItem {

    /**
     * TODO
     *  - Add Scoreboard Builder
     */

    int LINE_LIMIT = 16; //the amount of lines that can be displayed

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player. If the scoreboard
     * is not running, args will be used to set the default args of the message.
     *
     * @param content The new content for the line.
     * @param line The line index to change
     * @param args Any args that the message requires
     * @throws ArrayIndexOutOfBoundsException if the line is above the amount of lines this scoreboard hosts
     */
    void setLine(@NotNull Message content, int line, Object... args);

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player.
     *
     * @param content The new content for the line.
     * @param line The line index to change
     * @throws ArrayIndexOutOfBoundsException if the line is above the amount of lines this scoreboard hosts
     */
    void setLine(@NotNull String content, int line);

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player.
     *
     * @param content The new content for the line.
     * @param line The line index to change
     * @param lineName An optional unique name for the line.
     * @throws ArrayIndexOutOfBoundsException if the line is above the amount of lines this scoreboard hosts
     * @throws IllegalArgumentException If a line with this line name already exists (unless you are editing the line with that name). Use
     *         {@link #setLineByName(String, String)} to edit an existing line by name.
     */
    void setLine(@NotNull String content, int line, @Nullable String lineName);

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player. If the scoreboard
     * is not running, args will be used to set the default args of the message.
     *
     * @param content The new content for the line.
     * @param line The line index to change
     * @param lineName An optional unique name for the line.
     * @param args Any arguments the message should have
     * @throws ArrayIndexOutOfBoundsException if the line is above the amount of lines this scoreboard hosts
     * @throws IllegalArgumentException If a line with this line name already exists (unless you are editing the line with that name). Use
     *         {@link #setLineByName(String, String)} to edit an existing line by name.
     */
    void setLine(@NotNull Message content, int line, @Nullable String lineName, Object... args);

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player. If
     * a line with the given name does not exist nothing happens.
     *
     * @param content The new content for the line.
     * @param lineName THe name of the line that needs to be changed
     */
    void setLineByName(@NotNull String content, @NotNull String lineName);

    /**
     * Mutates the content of a line. If the scoreboard is running, this will update the scoreboard shown to the player. If
     * a line with the given name does not exist nothing happens.  If the scoreboard is not running,
     * args will be used to set the default args of the message.
     *
     * @param content The new content for the line.
     * @param lineName THe name of the line that needs to be changed
     * @param args any arguments for the message.
     */
    void setLineByName(@NotNull Message content, @NotNull String lineName, Object... args);

    /**
     * Mutates the line arguments of a message. If the scoreboard is running this will update the scoreboard shown to the player.
     * This function will set the args of the message with {@link Message#toString(Object...)}
     *
     * @param line the positional index of the scoreboard line to mutate
     * @param args the new arguments for the message
     * @throws ArrayIndexOutOfBoundsException if the line is above the amount of lines this scoreboard hosts
     */
    void setLineArgs(int line, Object... args);

    /**
     * Mutates the line arguments of a message. If the scoreboard is running this will update the scoreboard shown to the player.
     * This function will set the args of the message with {@link Message#toString(Object...)}. If a scoreboard line does no
     *
     * @param lineName the positional index of the scoreboard line to mutate
     * @param args the new arguments for the message
     */
    void setLineArgs(@NotNull String lineName, Object... args);

    /**
     * Mutates the scoreboard title. If the scoreboard is running this will update the scoreboard shown to the player.
     * 
     * @param title The new title for the scoreboard
     * @param args The args for the title
     */
    void setTitle(@NotNull Message title, Object... args);

    /**
     * Mutates the scoreboard title. If the scoreboard is running this will update the scoreboard shown to the player. 
     * 
     * @param title The new title 
     */
    void setTitle(@NotNull String title);


    /**
     *
     * @param index the positional index of the line to retrieve
     * @return the message at that line
     */
    @NotNull
    Message getLineAt(int index);

    /**
     *
     * @param name The name of the line to retrieve from
     * @return null if there is no message with that line or the message with that line name
     */
    @Nullable
    Message getLineFromName(@NotNull String name);

    /**
     *
     * @param index the positional index of the line
     * @return What is seen by the player of the scoreboard at this line
     * @throws ArrayIndexOutOfBoundsException If no line exists with that index
     */
    @NotNull
    String getDisplayedAt(int index);

    /**
     * @param name The name of the scoreboard line
     * @return null if there is no line with that name or what is seen by the player at the line with that name
     */
    @Nullable
    String getDisplayedAt(@NotNull String name);

    /**
     * @return The scoreboard title
     */
    @NotNull
    Message getTitle();

    /**
     * @return the amount of lines the scoreboard has
     */
    int getSize();

    /**
     *
     * @return The name of this scoreboard
     */
    @NotNull
    String getName();
}
