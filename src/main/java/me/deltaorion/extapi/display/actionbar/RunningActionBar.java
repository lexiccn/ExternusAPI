package me.deltaorion.extapi.display.actionbar;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

public interface RunningActionBar {

    public void start();

    public void cancel(boolean overwrite);

    public boolean isRunning();

    @NotNull
    public ActionBar getActionBar();

    public void setArgs(Object... args);

    public CountDownLatch getFinishLatch();
}
