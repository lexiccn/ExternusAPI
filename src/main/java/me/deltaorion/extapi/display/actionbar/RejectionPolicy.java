package me.deltaorion.extapi.display.actionbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;

public interface RejectionPolicy {
    //will be run if an action bar is currently
    public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued);

    public static RejectionPolicy OVERWRITE() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                return true;
            }
        };
    }

    public static RejectionPolicy SILENT_REJECT() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                return false;
            }
        };
    }

    public static RejectionPolicy FAIL() {
        return new RejectionPolicy() {
            @Override
            public boolean handle(@NotNull ActionBar toRender, @NotNull ActionBar currentlyRendered, @NotNull Queue<ActionBar> queued) {
                throw new RejectedActionBarException("Cannot send action bar as '"+currentlyRendered+"' is already being shown!");
            }
        };
    }

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
