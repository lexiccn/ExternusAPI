package me.deltaorion.bukkit.display.actionbar;

import me.deltaorion.bukkit.display.DisplayLine;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

/**
 * Normally when sending an action bar it can only be sent for 3 seconds. To overcome this problem and have n duration for action bars a
 * runnable should be used. This runnable will continue sending the same action bar over and over to the user giving the illusion that the action
 * bar is permanent.
 *
 * This does come with some downsides however. If the duration of the action bar is less than 3 seconds then a runnable with a blank message
 * is sent instead to terminate it quickly. This trick can also be used to cancel the action bar.
 *
 * When cancelled the Running Action Bar is terminated permanently. It cannot be run again and all references to it should be removed to avoid
 * memory leaks.
 *
 * For the most part a running action bar is immutable except for the message arguments which can be changed at anytime with {@link #setArgs(Object...)}
 *
 * Implementations
 *   - {@link me.deltaorion.bukkit.display.actionbar.running.ScheduleRunningActionBar}
 */
public interface RunningActionBar extends DisplayLine {

    /**
     * Starts a running action bar. This should do nothing if this has been cancelled or if the action bar is already running.
     */
    void start();

    /**
     * Cancels the running action bar. As there is no special state dependent cleanup this can safely, not be called assuming that the
     * server is shutting down. When this is called
     *  - the current render should finish
     *  - A blank line is rendered
     *
     * The overwrite clause determines whether the new action bar should put a blank screen at the end to suddenly cancel or not. If overwrite
     * is set to true then it will assume a message is coming after this one and thus it should not send a blank action bar as the new
     * one will replace this one.
     *
     * @param overwrite whether the cancel is the result of an overwrite or not
     */
    void cancel(boolean overwrite);

    /**
     * @return whether the action bar is running or not
     */
    boolean isRunning();

    /**
     * @return The action bar that this is running. This is immutable and should not change.
     */
    @NotNull
    ActionBar getActionBar();

    /**
     * Counts down when the action bar has finished. This is mostly a testing utility.
     *
     * @return A finish latch which counts down when the action bar has finished running.
     */
    CountDownLatch getFinishLatch();
}
