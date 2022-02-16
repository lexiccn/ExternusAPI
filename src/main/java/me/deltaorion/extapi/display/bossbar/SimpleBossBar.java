package me.deltaorion.extapi.display.bossbar;

import com.google.common.base.MoreObjects;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.DisplayLine;
import me.deltaorion.extapi.display.SimpleDisplayLine;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.protocol.WrapperPlayServerBoss;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A BossBar is a display item that is rendered at the top of the players screen. 
 *   - A player can only have one bossbar rendered at any given time
 *   - A bossbar can have a progress between 0 and 1 {@link #setProgress(float)}
 *   - Displays a single translatable message to the player it is tied to {@link #setMessage(String)}
 *   - Can be made visible or invisible at any time with {@link #setVisible(boolean)}
 *
 *   A bossbar is tied to the player it is given to. Once given to a player this instance cannot be given to another
 *   player.
 */
public class SimpleBossBar implements BossBar {

    @NotNull private final BukkitPlugin plugin;

    @NotNull private DisplayLine displayLine;
    @NotNull private final String name;
    private boolean visible;

    @NotNull private BarColor color;
    @NotNull private BarStyle style;
    @NotNull private final Set<BarFlag> flags;

    @NotNull private final BukkitApiPlayer player;
    private float progress;

    private final float DEFAULT_PROGRESS = 1;
    private final boolean DEFAULT_VISIBILITY = true;

    @NotNull private final BossBarRenderer renderer;

    /**
     * @param plugin The plugin the bossbar is hosted on
     * @param player The player who to give this to
     * @param name The name of the bossbar. This is used to identify the bossbar and is not the message rendered to the users screen.
     */
    public SimpleBossBar(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, @NotNull BossBarRendererFactory factory) {
        this(plugin,player,name,Message.valueOf(""),factory);
    }

    /**
     *
     * @param plugin The plugin the bossbar is hosted on
     * @param player The player who to give this to
     * @param name The name of the bossbar. This is used to identify the bossbar and is not the message rendered to the users screen.
     * @param message The initial message to render to the bossbar.
     */
    public SimpleBossBar(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, @NotNull Message message, @NotNull BossBarRendererFactory factory) {
        this.plugin = Objects.requireNonNull(plugin);
        this.player = plugin.getBukkitPlayerManager().getPlayer(player);
        this.name = Objects.requireNonNull(name);
        this.renderer = factory.get(plugin,this.player);

        displayLine = new SimpleDisplayLine(this.player,message);

        this.flags = new HashSet<>();
        this.color = BarColor.PINK;
        this.style = BarStyle.PROGRESS;

        setVisible(DEFAULT_VISIBILITY);
        setProgress(DEFAULT_PROGRESS);
        setMessage(message);
    }

    /**
     * @return the name of the bossbar.
     */
    @Override
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The message that should be rendered to the user's screen.
     */
    @Override
    @NotNull
    public Message getMessage() {
        return displayLine.getMessage();
    }

    @Override
    public void setMessage(@NotNull Message message, Object... args) {
        displayLine = new SimpleDisplayLine(this.player,message,args);
        try {
            renderer.setMessage(displayLine.getAsDisplayed());
        } catch (Throwable e) {
            handle(e);
        }
    }

    private void handle(Throwable e) {
        plugin.getPluginLogger().severe("An error occurred when rendering the BossBar", e);
    }

    /**
     * Sets the message rendered to the users screen. If the bossbar is running this will update what the user see's
     *
     * @param message The new message to be rendered
     */
    @Override
    public void setMessage(@NotNull String message) {
        setMessage(Message.valueOf(message));
    }

    /**
     * Sets the message arguments to be rendered to the users screen. If the bossbar is running this will change what the user see's
     *
     * @param args The new {@link Message} arguments
     */
    @Override
    public void setArgs(Object... args) {
        setMessage(getMessage(),args);
    }

    /**
     * @return What the user see's on the rendered bossbar message.
     */
    @Override
    @NotNull
    public String getAsDisplayed() {
        return displayLine.getAsDisplayed();
    }

    /**
     * @return Whether the bossbar is visible to the user.
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     * Toggles whether the display item should be visible to the user.
     *    - If visible the user will see the display item
     *    - If invisible the user will still have the display item but will simply not be able to see it.
     *
     *  To actually remove the display item use the appropriate remove method.
     *
     * @param visible Whether the display item is visible or not to the user.
     */
    @Override
    public void setVisible(boolean visible) {
        if(this.visible==visible)
            return;
        this.visible = visible;
        try {
            renderer.setVisible(visible);
        } catch (Throwable e) {
            handle(e);
        }
    }

    @Override
    public float getProgress() {
        return progress;
    }

    /**
     * Sets the 'progress' of the BossBar. This determines the percentage that the bossbar is colored in.
     * If set to 0 or 0% the bossbar is not colored in. If set to 0.5 or 50% the bossbar is half colored in.
     * If set to 1 then the bossbar is fully colored in.
     *
     * @param progress The bossbar progress
     * @throws IllegalArgumentException if the progress is not between 0 and 1
     */
    @Override
    public void setProgress(float progress) {
        Validate.isTrue(progress >= 0 && progress<=1);
        if(progress==this.progress)
            return;
        this.progress = progress;
        try {
            renderer.setProgress(progress);
        } catch (Throwable e) {
            handle(e);
        }

    }

    @Override
    public void setColor(@NotNull BarColor color) {
        Objects.requireNonNull(color);
        if(this.color.equals(color))
            return;
        this.color = color;
        this.renderer.setColor(color);
    }

    @Override @NotNull
    public BarColor getColor() {
        return this.color;
    }

    @Override
    public void setStyle(@NotNull BarStyle style) {
        Objects.requireNonNull(style);
        if(this.style.equals(style))
            return;
        this.style = style;
        this.renderer.setStyle(style);
    }

    @NotNull @Override
    public BarStyle getStyle() {
        return this.style;
    }

    @Override
    public void addFlags(@NotNull BarFlag... flags) {
        for(BarFlag flag : flags) {
            if(!this.flags.contains(flag)) {
                this.flags.add(flag);
                setFlag(flag,true);
            }
        }
    }

    private void setFlag(@NotNull BarFlag flag, boolean add) {
        switch (flag) {
            case CREATE_FOG:
                renderer.setCreateFog(add);
                break;
            case DARKEN_SKY:
                renderer.setDarkenSky(add);
                break;
            case PLAY_BOSS_MUSIC:
                renderer.setPlayMusic(add);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Boss Bar Flag");
        }
    }

    @Override
    public void removeFlags(@NotNull BarFlag... flags) {
        for(BarFlag flag : flags) {
            if(this.flags.contains(flag)) {
                this.flags.remove(flag);
                setFlag(flag,false);
            }
        }
    }

    @NotNull @Override
    public Collection<BarFlag> getFlags() {
        return Collections.unmodifiableSet(this.flags);
    }

    @NotNull @Override
    public BukkitApiPlayer getPlayer() {
        return player;
    }

    /**
     * Does any neccesary updates to the renderer if required. Whether this does anything depends on the renderer implementation.
     *
     * This should not be used to push changes to the user. For example if you use {@link #setMessage(Message, Object...)} that should
     * automatically update the message shown on their screen without any further intervention.
     */
    @Override
    public void update() {
        try {
            renderer.update();
        } catch (Throwable e) {
            handle(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof SimpleBossBar))
            return false;

        SimpleBossBar bossBar = (SimpleBossBar) o;
        return bossBar.player.equals(this.player) && bossBar.visible==this.visible && bossBar.progress == this.progress && displayLine.equals(bossBar.displayLine);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Message",displayLine.getMessage())
                .add("Rendered",displayLine.getAsDisplayed())
                .add("Visible",visible)
                .add("Progress",progress)
                .toString();
    }

}
