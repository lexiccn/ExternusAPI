package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.actionbar.*;
import me.deltaorion.extapi.display.actionbar.renderer.FailRenderer;
import me.deltaorion.extapi.display.actionbar.renderer.TestRenderer;
import me.deltaorion.extapi.display.actionbar.running.ScheduleRunningActionBar;
import me.deltaorion.extapi.display.bukkit.APIPlayerSettings;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.display.bukkit.EApiPlayer;
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
    private void runFunctionalityTest() {
        ActionBar actionBar = new ActionBar(Message.valueOf("Gamer"), Duration.of(1, ChronoUnit.DAYS));
        List<String> screen = new ArrayList<>();
        CountDownLatch renderLatch = new CountDownLatch(1);
        AtomicReference<ScheduleRunningActionBar> actionBarRunning = new AtomicReference<>();
        TestPlayer p = new TestPlayer("Gamer");
        Player tPlayer = null;
        try {
            tPlayer = p.asPlayer();
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
            return;
        }
        BukkitApiPlayer player = new EApiPlayer(plugin, tPlayer, new APIPlayerSettings().setActionBarFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer) {
                ScheduleRunningActionBar bar = new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new TestRenderer(screen, renderLatch),bukkitPlayer);
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
    private void runRejection() {
        ActionBar actionBar = new ActionBar(Message.valueOf("Gamer"), Duration.of(1, ChronoUnit.DAYS));
        List<String> screen = new ArrayList<>();
        CountDownLatch renderLatch = new CountDownLatch(1);
        AtomicReference<ScheduleRunningActionBar> actionBarRunning = new AtomicReference<>();
        TestPlayer p = new TestPlayer("Gamer");
        Player tPlayer = null;
        try {
            tPlayer = p.asPlayer();
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
            return;
        }
        BukkitApiPlayer player = new EApiPlayer(plugin, tPlayer, new APIPlayerSettings().setActionBarFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer) {
                ScheduleRunningActionBar bar = new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new TestRenderer(screen, renderLatch),bukkitPlayer);
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
        TestPlayer p = new TestPlayer("Gamer");
        Player tPlayer = p.asPlayer();

        BukkitApiPlayer player = new EApiPlayer(plugin, tPlayer, new APIPlayerSettings().setActionBarFactory(new ActionBarFactory() {
            @NotNull
            @Override
            public RunningActionBar get(@NotNull ActionBar actionBar, @NotNull BukkitPlugin plugin, @NotNull BukkitApiPlayer player, Object[] args, @NotNull ActionBarManager manager, @NotNull Player bukkitPlayer) {
                return new ScheduleRunningActionBar(actionBar, plugin, player, args, manager, new FailRenderer(),bukkitPlayer);
            }
        }));
        player.getActionBarManager().send(new ActionBar("Gamer",Duration.of(1,ChronoUnit.SECONDS)));
    }

    @Override
    public String getName() {
        return "Action Bar Test";
    }
}
