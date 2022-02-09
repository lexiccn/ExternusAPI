package me.deltaorion.extapi.display.actionbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;

/**
 * Sometimes when an action bar is sent there is already one that is being sent. If this happens a 'rejection' happens. The rejection policy
 * defines what should happen when a rejection occurs.
 */
public interface RejectionPolicy {
    /**
     * This method is run if an action bar is being sent, and one is already rendered on the players screen. That is this is run
     * when a rejection occurs. If this returns false then toRender will be discarded. If this returns true then the currently rendered
     * action bar will be cancelled, and overwritten by toRender.
     *
     * @param toRender The action bar that has been sent and is currently being 'rejected'
     * @param currentlyRendered The action bar that is currently one the screen
     * @param queued The current queue of actions bars.
     * @return If false, then the currently rendered action bar will stay and the sent one will be discarded. If true then the currently rendered
     * action bar will cancelled and overwritten with the new one.
     */
    public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued);


    /**
     * Overwrites the currently rendered action bar
     *   - the one that is currently on the screen is replaced with the one that is being sent. This means the one on the screen will
     *   no longer be shown and the one being sent will be shown instead.
     *   - The queue will remain, the next item in the queue will be played after the new overwritten action bar(the one being sent now)
     *   is finished.
     *
     * @return Overwrite rejection policy
     */
    public static RejectionPolicy OVERWRITE() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                return true;
            }
        };
    }

    /**
     * Silently rejects the action bar being sent
     *   - The action bar being sent will not be sent
     *   - The action bar will NOT be added to the queue.
     *   - The one being rendered will continue to be shown until it is finished.
     *
     * @return Silent Rejection policy
     */
    public static RejectionPolicy SILENT_REJECT() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                return false;
            }
        };
    }

    /**
     * If there is an action bar on the screen then this will fail throwing a {@link RejectedActionBarException}
     *
     * @return The fail rejection policy.
     */
    public static RejectionPolicy FAIL() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                throw new RejectedActionBarException("Cannot send action bar as '"+currentlyRendered+"' is already being shown!");
            }
        };
    }

    /**
     * This will reject the currently being sent and instead add it to the queue
     *   - The one being sent will not be shown right now, instead it will be added to the end of the queue.
     *   - The one being rendered will be shown to the end
     *
     * @return
     */
    public static RejectionPolicy QUEUE() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                queued.add(toRender);
                return false;
            }
        };
    }


}
