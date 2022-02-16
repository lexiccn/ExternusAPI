package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.exception.MissingDependencyException;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.display.actionbar.renderer.PacketActionBarRenderer;
import me.deltaorion.extapi.display.actionbar.renderer.WrapperActionBarRenderer;
import me.deltaorion.extapi.display.actionbar.running.ScheduleRunningActionBar;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.item.EMaterial;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarFactories {

    private ActionBarFactories() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static ActionBarFactory SCHEDULE(ActionBarRenderer renderer) {
        return new ActionBarFactory() {
            @NotNull @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer) {
                if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB.getName()).isActive())
                    throw new MissingDependencyException("Cannot Render action bar as dependency '"+BukkitAPIDepends.PROTOCOL_LIB.getName() +"' is missing!");

                return new ScheduleRunningActionBar(actionBar,plugin,player,args,manager,renderer,bukkitPlayer);
            }
        };
    }

    @NotNull
    public static ActionBarFactory SCHEDULE_FROM_VERSION() {
        return new ActionBarFactory() {
            @NotNull @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer) {
                ActionBarRenderer renderer;
                if(plugin.getEServer().getServerVersion().getMajor()>=12) {
                    renderer = new WrapperActionBarRenderer();
                } else {
                    renderer = new PacketActionBarRenderer();
                }
                return new ScheduleRunningActionBar(actionBar,plugin,player,args,manager,renderer,bukkitPlayer);
            }
        };
    }

}
