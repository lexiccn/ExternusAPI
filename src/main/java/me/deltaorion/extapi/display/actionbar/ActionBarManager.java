package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Queue;

public class ActionBarManager {

    @GuardedBy("this") private final Queue<ActionBar> actionBars;
    @Nullable private RunningActionBar rendered;
    private final BukkitPlugin plugin;
    private final BukkitApiPlayer player;
    private final ActionBarFactory factory;

    public ActionBarManager(BukkitPlugin plugin, BukkitApiPlayer player, ActionBarFactory factory) {
        this.plugin = plugin;
        this.player = player;
        this.factory = factory;
        this.actionBars = new ArrayDeque<>();
    }

    public void send(@NotNull ActionBar actionBar, Object... args) {
        send(actionBar,RejectionPolicy.OVERWRITE(),args);
    }

    public void send(@NotNull ActionBar actionBar, @NotNull RejectionPolicy policy, Object... args) {
        synchronized (this) {
            if (rendered == null) {
                //at this point rendered is now an ongoing calculation
                rendered = render(actionBar, args);
            } else {
                boolean overwrite = policy.handle(actionBar, rendered.getActionBar(), actionBars);
                if (overwrite) {
                    removeActionBar(true);
                    rendered = render(actionBar, args);
                }
            }
        }
    }


    private RunningActionBar render(@NotNull ActionBar actionBar, Object... args) {
        RunningActionBar rendered = factory.get(actionBar,plugin,player,args,this);
        rendered.start();
        return rendered;
    }


    public void clear() {
        actionBars.clear();
        removeActionBar();
    }

    public void removeActionBar() {
        removeActionBar(false);
    }


    private boolean cancelling = false;

    private void removeActionBar(boolean overwrite) {
        synchronized (this) {
            if (rendered == null)
                return;

            if(cancelling)
                return;

            cancelling = true;

            this.rendered.cancel(overwrite);
            rendered = null;

            cancelling = false;

            if(overwrite)
                return;

            if(actionBars.isEmpty())
                return;

            send(actionBars.poll(),RejectionPolicy.SILENT_REJECT());
        }
    }


    public void setArgs(Object... args) {
        synchronized (this) {
            if (rendered == null)
                return;
            rendered.setArgs(args);
        }
    }

    @Nullable
    public ActionBar getCurrentActionBar() {
        RunningActionBar actionBar = rendered;
        if(actionBar==null)
            return null;

        return actionBar.getActionBar();
    }

}
