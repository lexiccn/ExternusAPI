package me.deltaorion.extapi.test.unit.generic;

import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.thread.ErrorReportingThreadPool;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class McTester {

    private final EPlugin plugin;
    private final List<MinecraftTest> tests;
    private final List<MinecraftTest> asyncTests;

    private ExecutorService asyncTestPool;

    public McTester(EPlugin plugin) {
        this.plugin = plugin;
        this.tests = new ArrayList<>();
        this.asyncTests = new ArrayList<>();
    }

    public void addTest(@NotNull MinecraftTest test) {
        this.tests.add(Objects.requireNonNull(test));
    }

    public void addAsyncTest(@NotNull MinecraftTest test) {
        this.asyncTests.add(Objects.requireNonNull(test));
    }

    private void runTest(@NotNull MinecraftTest test) {
        Method currentMethod = null;
        try {
            Class<?> clazz = test.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                currentMethod = method;
                if (method.isAnnotationPresent(McTest.class)) {
                    method.setAccessible(true);
                    method.invoke(test);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            handleFailure(test,e.getCause(),currentMethod);
        }
    }

    public void runTests() {
        runSyncTests();
        runAsyncTests();
    }

    private void runAsyncTests() {
        if(asyncTests.size()==0)
            return;

        plugin.getPluginLogger().info("Running Async Tests");

        Runnable asyncRunner = new Runnable() {
            @Override
            public void run() {
                asyncTestPool = Executors.newSingleThreadExecutor();
                for(MinecraftTest test : asyncTests) {
                    plugin.getPluginLogger().info("Running Async Test ["+test.getName()+"]");

                    Runnable testRunnable = new Runnable() {
                        @Override
                        public void run() {
                            runTest(test);
                        }
                    };

                    Future<?> submission = asyncTestPool.submit(testRunnable);
                    try {
                        submission.get(5,TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        plugin.getPluginLogger().severe("Cannot complete test as the thread was interrupted!");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        submission.cancel(true);
                        plugin.getPluginLogger().severe("Test "+test.getName()+" failed as it took more than 5 seconds to execute!",e);
                    }
                    submission.cancel(true);
                }
                plugin.getPluginLogger().info("All Async Tests Complete");
                shutdown();
            }
        };
        plugin.getScheduler().runTaskAsynchronously(asyncRunner);
    }

    private void runSyncTests() {
        plugin.getPluginLogger().info("Running Sync Tests");
        for(MinecraftTest test : tests) {
            plugin.getPluginLogger().info("Running Test ["+test.getName()+"]");
            runTest(test);
        }
        plugin.getPluginLogger().info("All Sync Tests Complete");
    }

    public synchronized void shutdown() {
        if(asyncTestPool!=null) {
            asyncTestPool.shutdownNow();
        }
    }

    private void handleFailure(MinecraftTest test, Throwable e, Method method) {
        plugin.getPluginLogger().severe("Test '"+test.getName()+"' failed on method '"+method.getName()+"' because ",e);
    }

    public void clearTests() {
        this.tests.clear();
    }
}
