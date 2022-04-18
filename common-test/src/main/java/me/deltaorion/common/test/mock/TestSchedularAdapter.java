package me.deltaorion.common.test.mock;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.scheduler.SchedulerTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

public class TestSchedularAdapter implements SchedulerAdapter {

    private final ScheduledThreadPoolExecutor scheduler;
    private final ErrorReportingExecutor schedulerWorkerPool;

    public TestSchedularAdapter() {
        this.scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactoryBuilder()
                .setNameFormat("luckperms-scheduler")
                .build()
        );
        this.scheduler.setRemoveOnCancelPolicy(true);
        this.schedulerWorkerPool = new ErrorReportingExecutor(Executors.newCachedThreadPool(new ThreadFactoryBuilder()
                .setNameFormat("luckperms-scheduler-worker-%d")
                .build()
        ));
    }

    @Override
    public void scheduleSyncDelayedTask(Runnable task) {
        runTask(task);
    }

    @Override
    public SchedulerTask runTask(Runnable task) {
        return runTaskAsynchronously(task);
    }

    @Override
    public SchedulerTask runTaskAsynchronously(Runnable task) {
        return runTaskLater(task,0,TimeUnit.MILLISECONDS);
    }

    @Override
    public SchedulerTask runTaskLater(Runnable task, long delay, TimeUnit unit) {
        return runTaskLaterAsynchronously(task,delay,unit);
    }

    @Override
    public SchedulerTask runTaskLaterAsynchronously(Runnable task, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = this.scheduler.schedule(() -> this.schedulerWorkerPool.execute(task), delay, unit);
        return () -> future.cancel(false);
    }

    @Override
    public SchedulerTask runTaskTimer(Runnable task, long delay, long period, TimeUnit unit) {
        return runTaskTimerAsynchronously(task,delay,period,unit);
    }

    @Override
    public SchedulerTask runTaskTimerAsynchronously(Runnable task, long delay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = this.scheduler.scheduleAtFixedRate(() -> this.schedulerWorkerPool.execute(task), delay, period, unit);
        return () -> future.cancel(false);
    }

    private static final class ErrorReportingExecutor implements Executor {
        private final ExecutorService delegate;

        private ErrorReportingExecutor(ExecutorService delegate) {
            this.delegate = delegate;
        }

        @Override
        public void execute(@NotNull Runnable command) {
            this.delegate.execute(new ErrorReportingRunnable(command));
        }
    }

    public void shutdown() {
        this.schedulerWorkerPool.delegate.shutdown();
        this.scheduler.shutdown();
        try {
            if(!schedulerWorkerPool.delegate.awaitTermination(3,TimeUnit.SECONDS))
                schedulerWorkerPool.delegate.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static final class ErrorReportingRunnable implements Runnable {
        private final Runnable delegate;

        private ErrorReportingRunnable(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override
        public void run() {
            try {
                this.delegate.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
