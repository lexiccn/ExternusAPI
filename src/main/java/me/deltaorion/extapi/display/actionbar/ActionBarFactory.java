package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for constructing new RunningActionBar's
 */
public interface ActionBarFactory {

    /**
     * Should return a new {@link RunningActionBar} implementation
     *
     * @param actionBar The action bar to use
     * @param plugin The plugin which the action bar is hosted on
     * @param player The player to show the action bar too
     * @param args Message arguments
     * @param manager The action bar manager displaying the running action bar
     * @return A new RunningActionBar Implementation
     * @throws me.deltaorion.extapi.common.exception.MissingDependencyException if an implementation requires some kind of dependency
     */
    @NotNull
    public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer);
}
