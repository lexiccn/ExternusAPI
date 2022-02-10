package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.TiedDisplayItem;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
public class BossBar implements TiedDisplayItem {

    @NotNull private final BukkitPlugin plugin;

    @NotNull private final String name;
    @NotNull private Message message;
    @NotNull private String asDisplayed;
    private boolean visible;

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
    public BossBar(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name) {
        this(plugin,player,name,Message.valueOf(""));
    }

    /**
     *
     * @param plugin The plugin the bossbar is hosted on
     * @param player The player who to give this to
     * @param name The name of the bossbar. This is used to identify the bossbar and is not the message rendered to the users screen.
     * @param message The initial message to render to the bossbar.
     */
    public BossBar(@NotNull BukkitPlugin plugin, @NotNull Player player, @NotNull String name, @NotNull Message message) {
        this.plugin = Objects.requireNonNull(plugin);
        this.player = plugin.getBukkitPlayerManager().getPlayer(player);
        this.name = Objects.requireNonNull(name);
        this.message = Objects.requireNonNull(message);
        BossBarRendererFactory factory = Objects.requireNonNull(BossBarRendererFactory.fromVersion(plugin.getEServer().getServerVersion()));
        this.asDisplayed = message.toString(this.player.getLocale());
        this.renderer = factory.get(plugin,this.player);
        this.player.setBossBar(this);

        setVisible(DEFAULT_VISIBILITY);
        setProgress(DEFAULT_PROGRESS);
        setMessage(message);
    }

    /**
     * @return the name of the bossbar.
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return The message that should be rendered to the user's screen.
     */
    @NotNull
    public Message getMessage() {
        return this.message;
    }

    public void setMessage(@NotNull Message message, Object... args) {
        this.message = message;
        this.asDisplayed = message.toString(player.getLocale(),args);
        try {
            renderer.setMessage(asDisplayed);
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
    public void setMessage(@NotNull String message) {
        setMessage(Message.valueOf(message));
    }

    /**
     * Sets the message arguments to be rendered to the users screen. If the bossbar is running this will change what the user see's
     *
     * @param args The new {@link Message} arguments
     */
    public void setArgs(Object... args) {
        setMessage(getMessage(),args);
    }

    /**
     * @return What the user see's on the rendered bossbar message.
     */
    @NotNull
    public String getDisplayed() {
        return asDisplayed;
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
    public void update() {
        try {
            renderer.update();
        } catch (Throwable e) {
            handle(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BossBar))
            return false;

        BossBar bossBar = (BossBar) o;
        return bossBar.player.equals(this.player) && bossBar.visible==this.visible && bossBar.progress == this.progress && bossBar.message.equals(this.message);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Message",message)
                .add("Rendered",asDisplayed)
                .add("Visible",visible)
                .add("Progress",progress)
                .toString();
    }

}
