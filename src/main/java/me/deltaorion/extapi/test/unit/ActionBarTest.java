package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.actionbar.*;
import me.deltaorion.extapi.display.actionbar.renderer.FailRenderer;
import me.deltaorion.extapi.display.actionbar.renderer.TestRenderer;
import me.deltaorion.extapi.display.actionbar.running.ScheduleRunningActionBar;
import me.deltaorion.extapi.display.bukkit.APIPlayerSettings;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.test.unit.bukkit.TestPlayer;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class ActionBarTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public ActionBarTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void testFunctionalityAndCancellation() {
        plugin.getScheduler().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                //the whole test needs to be run async otherwise bukkit cant schedule the task. We will see the stack trace anyway
                try {
                    runFunctionalityTest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runFunctionalityTest() {
        ActionBar actionBar = new ActionBar(Message.valueOf("Gamer"), Duration.of(1, ChronoUnit.DAYS));
        List<String> screen = new ArrayList<>();
        CountDownLatch renderLatch = new CountDownLatch(1);
        AtomicReference<ScheduleRunningActionBar> actionBarRunning = new AtomicReference<>();
        Player tPlayer = new TestPlayer("Gamer");
        BukkitApiPlayer player = new BukkitApiPlayer(plugin, tPlayer, new APIPlayerSettings().setFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager) {
                ScheduleRunningActionBar bar = new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new TestRenderer(screen, renderLatch));
                actionBarRunning.set(bar);
                return bar;
            }
        }));
        assertNull(player.getActionBarManager().getCurrentActionBar());
        player.getActionBarManager().send(actionBar);
        try {
            renderLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, screen.size());
        assertEquals("Gamer", screen.get(0));
        assertEquals(actionBar, player.getActionBarManager().getCurrentActionBar());
        assertNotNull(actionBarRunning.get());

        assertEquals(actionBar, actionBarRunning.get().getActionBar());

        player.getActionBarManager().removeActionBar();

        try {
            assertTrue(actionBarRunning.get().getFinishLatch().await(1000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNull(player.getActionBarManager().getCurrentActionBar());
        assertFalse(actionBarRunning.get().isRunning());

        //shouldn't do anything
        player.getActionBarManager().removeActionBar();
        player.getActionBarManager().removeActionBar();
        player.getActionBarManager().removeActionBar();
        plugin.getBukkitPlayerManager().removeCached(tPlayer);
    }

    @McTest
    public void testRejection() {
        plugin.getScheduler().runTaskAsynchronously(new Runnable() {
            @Override
            public void run() {
                try {
                    runRejection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void runRejection() {
        ActionBar actionBar = new ActionBar(Message.valueOf("Gamer"), Duration.of(1, ChronoUnit.DAYS));
        List<String> screen = new ArrayList<>();
        CountDownLatch renderLatch = new CountDownLatch(1);
        AtomicReference<ScheduleRunningActionBar> actionBarRunning = new AtomicReference<>();
        Player tPlayer = new TestPlayer("Gamer");
        BukkitApiPlayer player = new BukkitApiPlayer(plugin, tPlayer, new APIPlayerSettings().setFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager) {
                ScheduleRunningActionBar bar = new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new TestRenderer(screen, renderLatch));
                actionBarRunning.set(bar);
                return bar;
            }
        }));
        assertNull(player.getActionBarManager().getCurrentActionBar());
        player.getActionBarManager().send(actionBar);
        try {
            renderLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ActionBar gamer2 = new ActionBar("Gamer2", Duration.of(1, ChronoUnit.DAYS));
        player.getActionBarManager().send(gamer2, RejectionPolicy.SILENT_REJECT());
        assertEquals(actionBar, player.getActionBarManager().getCurrentActionBar());
        assertNotSame(actionBar, gamer2);
        try {
            player.getActionBarManager().send(gamer2, RejectionPolicy.FAIL());
            fail();
        } catch (RejectedActionBarException ignored) {

        }
        assertEquals(actionBar, player.getActionBarManager().getCurrentActionBar());
        RunningActionBar ab = actionBarRunning.get();
        player.getActionBarManager().send(gamer2, RejectionPolicy.OVERWRITE());
        try {
            assertTrue(ab.getFinishLatch().await(1000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(gamer2, player.getActionBarManager().getCurrentActionBar());
        player.getActionBarManager().removeActionBar();
        try {
            assertTrue(actionBarRunning.get().getFinishLatch().await(1000, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        plugin.getBukkitPlayerManager().removeCached(tPlayer);


    }


    public void failTest() {
        BukkitApiPlayer player = new BukkitApiPlayer(plugin, new TestPlayer("Gamer"), new APIPlayerSettings().setFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager) {
                return new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new FailRenderer());
            }
        }));
        player.getActionBarManager().send(new ActionBar("Gamer",Duration.of(1,ChronoUnit.SECONDS)));
    }

    @Override
    public String getName() {
        return "Action Bar Test";
    }
}
