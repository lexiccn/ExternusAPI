package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.actionbar.running.ScheduleRunningActionBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

public class ActionBarFactories {

    public static ActionBarFactory SCHEDULE(ActionBarRenderer renderer) {
        return new ActionBarFactory() {
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, ActionBarManager manager) {
                return new ScheduleRunningActionBar(actionBar,plugin,player,args,manager,renderer);
            }
        };
    }
}
