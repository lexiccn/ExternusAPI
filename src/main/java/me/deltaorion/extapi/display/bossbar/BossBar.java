package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.display.DisplayLine;
import me.deltaorion.extapi.display.TiedDisplayItem;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.protocol.WrapperPlayServerBoss;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A BossBar is a display item that is rendered at the top of the players screen.
 *   - A player can only have one BossBar rendered at any given time. Although it is technically possible to have
 *     more this is largely undesirable.
 *   - A BossBar can have a progress between 0 and 1 {@link #setProgress(float)}
 *   - Displays a single translatable message to the player it is tied to {@link #setMessage(String)}
 *   - Can be made visible or invisible at any time with {@link #setVisible(boolean)}
 *
 *   A BossBar is tied to the player it is given to. Once given to a player this instance cannot be given to another
 *   player.
 */
public interface BossBar extends TiedDisplayItem, DisplayLine {

    /**
     * @return the name of the bossbar.
     */
    @NotNull
    public String getName();

    /**
     * Sets the message rendered to the users screen. If the bossbar is running this will update what the user see's
     *
     * @param message The new message to be rendered
     * @param args Any message args to be displayed
     */
    public void setMessage(@NotNull Message message, Object... args) ;

    /**
     * Sets the message rendered to the users screen. If the bossbar is running this will update what the user see's
     *
     * @param message The new message to be rendered
     */
    public void setMessage(@NotNull String message);

    /**
     * @return The progress of the BossBar ranging from 0 to 1.
     */
    public float getProgress();

    /**
     * Sets the 'progress' of the BossBar. This determines the percentage that the bossbar is colored in.
     * If set to 0 or 0% the bossbar is not colored in. If set to 0.5 or 50% the bossbar is half colored in.
     * If set to 1 then the bossbar is fully colored in.
     *
     * @param progress The bossbar progress
     * @throws IllegalArgumentException if the progress is not between 0 and 1
     */
    public void setProgress(float progress);

    /**
     * Sets the color of the BossBar. This will default as {@link BarColor#PINK}
     *
     * If the version is 1.8 or less this method may throw an exception
     *
     * @param color the new color for the BossBar
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException if the version is 1.8 or less
     */
    public void setColor(@NotNull BarColor color);

    /**
     * @return The current color of the BossBar.
     */
    @NotNull
    public BarColor getColor();

    /**
     * Sets the style of the BossBar. This will default as {@link me.deltaorion.extapi.protocol.WrapperPlayServerBoss.BarStyle#PROGRESS}
     *
     * If the version is 1.8 or less this method may throw an exception
     *
     * @param style the new style for the BossBar
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException if the version is 1.8 or less
     */
    public void setStyle(@NotNull BarStyle style);

    /**
     * @return The current bar style
     */
    @NotNull
    public BarStyle getStyle();

    /**
     * Sets the BossBar flags. These are special properties for the BossBar. If the BossBar already has that flag this
     * will do nothing.
     *
     * @param flags The flags to set
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException if the version is 1.8 or less
     */
    public void addFlags(@NotNull BarFlag... flags);

    /**
     * Removes the BossBar flags. These are special properties for the BossBar. If the BossBar does not already have the flag
     * then this will do nothing.
     *
     * @param flags The flags to set
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException if the version is 1.8 or less
     */
    public void removeFlags(@NotNull BarFlag... flags);

    /**
     *
     * @return the flags that the BossBar currently has
     */
    @NotNull
    public Collection<BarFlag> getFlags();

    /**
     * Does any neccesary updates to the renderer if required. Whether this does anything depends on the renderer implementation.
     *
     * This should not be used to push changes to the user. For example if you use {@link #setMessage(Message, Object...)} that should
     * automatically update the message shown on their screen without any further intervention.
     *
     * This will not be called in any version greater than 1.8 as any post 1.9 BossBar's should be packet or wrapper based.
     */
    public void update();

}
