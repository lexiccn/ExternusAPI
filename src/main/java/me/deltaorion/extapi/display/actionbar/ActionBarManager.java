package me.deltaorion.extapi.display.actionbar;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import net.jcip.annotations.GuardedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

/**
 * The ActionBarManager is a helper class for a {@link BukkitApiPlayer} used to manage the sending, removal and queueing of action bars.
 * A player can only display one action bar at any given time. This class keeps track of an ensures that this is held true and that the reference
 * to said RunningActionBar is not lost.
 *
 * Users can send an ActionBar with {@link #send(ActionBar, RejectionPolicy, Object...)}. That action bar can be removed at any time with
 * {@link #removeActionBar()}. You can find out what the current bar is with {@link #getCurrentActionBar()}.
 *
 * When sending an action bar there may be an existing action bar. To deal with this there are various {@link RejectionPolicy}. This determines
 * what should happen if there is an existing action bar. For example, overwriting the one that is currently running, Discarding the one to be sent, Or
 * queuing the one to be sent to wait for the current one.
 *
 */
public class ActionBarManager {

    @GuardedBy("this") private final Queue<ActionBar> actionBars;
    @Nullable private RunningActionBar rendered;
    @NotNull private final BukkitPlugin plugin;
    @NotNull private final BukkitApiPlayer player;
    @NotNull private final ActionBarFactory factory;

    private boolean shutdown = false;

    public ActionBarManager(@NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, @NotNull ActionBarFactory factory) {
        this.plugin = Objects.requireNonNull(plugin);
        this.player = Objects.requireNonNull(player);
        this.factory = Objects.requireNonNull(factory);
        this.actionBars = new ArrayDeque<>();
    }

    /**
     * Sends an action bar to the user. If an action bar is currently playing then the new one will play anyway overwriting the old one.
     *
     * @param actionBar The action bar to send
     * @param args Any message args
     */
    public void send(@NotNull ActionBar actionBar, Object... args) {
        send(actionBar,RejectionPolicy.OVERWRITE(),args);
    }

    /**
     * Sends and action bar to the user with a custom rejection policy. The rejection policy determines what will happen
     * if an action bar is currently running. Several implementations can be found in {@link RejectionPolicy} such as
     * {@link RejectionPolicy#OVERWRITE()} or {@link RejectionPolicy#QUEUE()}
     *
     * This will do nothing if the manager has been shutdown (caused by player disconnect)
     *
     * @param actionBar The action bar to send
     * @param policy The Rejection Policy to use. This will affect what occurs if an action bar is currently running.
     * @param args Any message args
     */
    public void send(@NotNull ActionBar actionBar, @NotNull RejectionPolicy policy, Object... args) {
        Objects.requireNonNull(actionBar);
        Objects.requireNonNull(policy);
        synchronized (this) {
            if(shutdown)
                return;

            if (rendered == null) {
                //at this point rendered is now an ongoing calculation
                rendered = render(actionBar, args);
            } else {
                boolean overwrite = policy.handle(actionBar, rendered.getActionBar(), actionBars);
                if (overwrite) {
                    //ensure that it is cancelled before putting the new one(don't lose reference to existing action bar)
                    removeActionBar(true);
                    rendered = render(actionBar, args);
                }
            }
        }
    }

    //Create and starts a running action bar
    private RunningActionBar render(@NotNull ActionBar actionBar, Object... args) {
        RunningActionBar rendered = factory.get(actionBar,plugin,player,args,this);
        rendered.start();
        return rendered;
    }


    /**
     * Removes any action bars in the queue and cancels the one that is currently running.
     */
    public void clear() {
        actionBars.clear();
        removeActionBar();
    }

    /**
     * Removes the action bar that is currently running. If this method is used then the action
     * bar will not fade out and will be removed now. If there is another action bar in the queue
     * then it will be played. To stop this from happening use {@link #clear()}
     */
    public void removeActionBar() {
        removeActionBar(false);
    }

    private boolean cancelling = false;

    /**
     * Removes a currently running action bar. The existing action bar should be cancelled.
     * If overwriting
     *   - The action bar should not show a blank screen to remove it (to avoid flashing)
     *   - The queue should be ignored
     *
     * Otherwise
     *   - The next action bar in the queue needs to be removed now by showing a blank screen
     *
     * @param overwrite Whether the removal is because of an overwrite.
     */
    private void removeActionBar(boolean overwrite) {
        synchronized (this) {
            //ensure there is something to cancel
            if (rendered == null)
                return;
            //this will stop the running action bar from double cancelling if it calls this method itself. (its the only thing with access
            // to the lock)
            if(cancelling)
                return;

            cancelling = true;
            //cancel the currently rendered action bar
            this.rendered.cancel(overwrite);
            rendered = null;

            cancelling = false;

            if(overwrite)
                return;

            //grab next action bar from queue and play it
            if(actionBars.isEmpty())
                return;

            send(actionBars.poll(),RejectionPolicy.SILENT_REJECT());
        }
    }

    /**
     * Changes the {@link me.deltaorion.extapi.locale.message.Message} arguments in the currently running action bar
     *
     * @param args The message args to change
     */
    public void setArgs(Object... args) {
        synchronized (this) {
            if (rendered == null)
                return;
            rendered.setArgs(args);
        }
    }

    /**
     *
     * @return The currently running action bar. Null if there is none.
     */
    @Nullable
    public ActionBar getCurrentActionBar() {
        RunningActionBar actionBar = rendered;
        if(actionBar==null)
            return null;

        return actionBar.getActionBar();
    }

    public void shutdown() {
        synchronized (this) {
            shutdown = true;
        }
        clear();
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("Player",player)
                .add("Rendered",rendered)
                .toString();
    }

}
