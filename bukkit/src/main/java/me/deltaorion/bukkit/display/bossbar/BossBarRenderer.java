package me.deltaorion.bukkit.display.bossbar;

import org.jetbrains.annotations.NotNull;

/**
 * Renders a bossbar to the users screen. This class is responsible for performing any actions related to sending a BossBar
 * to the user.
 */
public interface BossBarRenderer {

    /**
     * Sets the message shown on the bossbar. If the bossbar is running this should update the shown bossbar.
     *
     * @param render the new exact string to render to the bossbar.
     */
    void setMessage(@NotNull String render);

    /**
     * Each bossbar has a progress slider. If set to 1 then the bossbar will be fully colored in. If set to 0 then the bossbar
     * will not be colored in at all. If set to 0.5 then it will be 50% colored in
     *
     * @param progress The progress to set the bossbar to
     * @throws IllegalArgumentException If the value is less than 0 or greater than 1
     */
    void setProgress(float progress);

    /**
     * Toggles whether the bossbar should be visible on the users screen. If set to false one should not see the bossbar at all
     * and if set to true they should see it.
     *
     * @param visible Whether the bossbar should be visible
     */
    void setVisible(boolean visible);

    /**
     * Performs any necessary updating to the shown BossBar.
     */
    void update();

    /**
     * Sets the color of the BossBar. This should update the color on the users screen.
     *
     * @param color The new bossbar color
     */
    void setColor(@NotNull BarColor color);

    /**
     * Sets the style of the BossBar. This should update the style on the users screen.
     *
     * @param style The new bossbar style
     */
    void setStyle(@NotNull BarStyle style);

    /**
     * @param createFog whether the bossbar should create fog
     */
    void setCreateFog(boolean createFog);

    /**
     * @param darkenSky whether the bossbar should darken sky
     */
    void setDarkenSky(boolean darkenSky);

    /**
     * @param playMusic whether to play music or not
     */
    void setPlayMusic(boolean playMusic);
}
