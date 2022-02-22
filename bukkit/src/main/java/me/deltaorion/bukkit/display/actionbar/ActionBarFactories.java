package me.deltaorion.bukkit.display.actionbar;

import me.deltaorion.bukkit.display.actionbar.renderer.PacketActionBarRenderer;
import me.deltaorion.bukkit.display.actionbar.renderer.WrapperActionBarRenderer;
import me.deltaorion.bukkit.display.actionbar.running.ScheduleRunningActionBar;
import me.deltaorion.bukkit.plugin.plugin.BukkitAPIDepends;
import me.deltaorion.common.plugin.exception.MissingDependencyException;
import org.jetbrains.annotations.NotNull;

public class ActionBarFactories {

    private ActionBarFactories() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public static ActionBarFactory SCHEDULE(ActionBarRenderer renderer) {
        return (actionBar, plugin, player, args, manager, bukkitPlayer) -> {
            if(!plugin.getDependency(BukkitAPIDepends.PROTOCOL_LIB).isActive())
                throw new MissingDependencyException("Cannot Render action bar as dependency '"+BukkitAPIDepends.PROTOCOL_LIB +"' is missing!");

            return new ScheduleRunningActionBar(actionBar,plugin,player,args,manager,renderer,bukkitPlayer);
        };
    }

    @NotNull
    public static ActionBarFactory SCHEDULE_FROM_VERSION() {
        return (actionBar, plugin, player, args, manager, bukkitPlayer) -> {
            ActionBarRenderer renderer;
            if(plugin.getEServer().getServerVersion().getMajor()>=12) {
                renderer = new WrapperActionBarRenderer();
            } else {
                renderer = new PacketActionBarRenderer();
            }
            return new ScheduleRunningActionBar(actionBar,plugin,player,args,manager,renderer,bukkitPlayer);
        };
    }

}
