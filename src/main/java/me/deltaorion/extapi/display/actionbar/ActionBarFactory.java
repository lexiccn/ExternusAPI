package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.jetbrains.annotations.NotNull;

public interface ActionBarFactory {

    public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, ActionBarManager manager);
}
