package me.deltaorion.extapi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import me.deltaorion.extapi.test.animation.LoggingAnimation;
import me.deltaorion.extapi.test.animation.StateAnimationTest;
import me.deltaorion.extapi.test.animation.TestAnimation;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Main {

    public static Main main = new Main();

    public static void main(String[] args) {
        String title = "Hello World";
        for(int i=0;i<title.length();i++) {
            String splitA = title.substring(0,i);
            String letter = title.substring(i,i+1);
            String splitB = title.substring(i+1);
            System.out.println("First: " + splitA);
            System.out.println("Letter: " + letter);
            System.out.println("Finish: "+ splitB);
        }
        /*try {
            main.run();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    public void run() throws URISyntaxException {
        TestServer server = new TestServer();
        TestPlugin plugin = new TestPlugin(server);
        server.addPlugin("Test",plugin);

        for(int i=0;i<1000;i++) {
            System.out.println("Starting "+i);
            simpleAnimationTest(plugin);
            System.out.println("Test Complete");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Disable");
        plugin.onDisable();
    }

    public void simpleAnimationTest(ApiPlugin plugin) {
        try {
            //test all running animation implementations
            testAnimation(TestAnimation.create(plugin, AnimationFactories.SCHEDULE_ASYNC(),false));
            testAnimation(TestAnimation.create(plugin,AnimationFactories.SLEEP_ASYNC(),false));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void testAnimation(TestAnimation animation) throws InterruptedException {
        long time =  System.currentTimeMillis();
        testScreens(animation);
        System.out.println("Screens - "+(System.currentTimeMillis() - time));
        time =  System.currentTimeMillis();
        testFunctionalityAndCancellation(animation);
        System.out.println("Functionality - "+(System.currentTimeMillis() - time));
        time =  System.currentTimeMillis();
        testCorrectness(animation);
        System.out.println("Correctness - "+(System.currentTimeMillis() - time));
    }

    private void testCorrectness(TestAnimation animation) throws InterruptedException {
        List<List<String>> screens = new ArrayList<>();
        final int nScreens = 5;
        final int amount = 1000;
        animation.clearFrames();
        for(int i=0;i<amount;i++) {
            animation.addFrame(new MinecraftFrame<>(String.valueOf(i),0));
        }
        for(int i=0;i<amount;i++) {
            screens.add(new ArrayList<>());
        }
        animation.setFinishLatch(new CountDownLatch(1));
        RunningAnimation<?> sRunningAnimation = animation.get(screens);
        sRunningAnimation.start();
        animation.getFinishLatch().await();

        for(int j=0;j<nScreens;j++) {
            List<String> screen = screens.get(j);
            assertEquals(amount,screen.size());
            for (int i = 0; i < amount; i++) {
                assertEquals(String.valueOf(i), screen.get(i));
            }
        }
    }

    private void testFunctionalityAndCancellation(TestAnimation animation) throws InterruptedException {
        List<String> screenA = new CopyOnWriteArrayList<>();
        List<String> screenB = new CopyOnWriteArrayList<>();
        int nFrames = 50;
        animation.setFrameLatch(new CountDownLatch(1));
        animation.setFinishLatch(new CountDownLatch(1));
        for(int i=0;i<nFrames;i++) {
            animation.addFrame(new MinecraftFrame<>("Gamer",10));
        }
        RunningAnimation<List<String>> runningAnimation = animation.start(screenA);
        animation.getFrameLatch().await();
        //1 frame have passed, the animation must be alive
        assertTrue(runningAnimation.isRunning());
        runningAnimation.addScreen(screenB);
        assertTrue(screenA.size()>0);
        //add some frames in, this shouldn't cause anything to meltdown
        for(int i=0;i<5;i++)
            animation.addFrame(new MinecraftFrame<>("Gamer",0));

        animation.setFrameLatch(new CountDownLatch(2));
        animation.getFrameLatch().await();
        //any number of frames might have passed so we can only make the assertion that both screens should have a frame
        assertTrue(screenB.size()>0);
        assertTrue(screenA.size()>0);
        for(int i=0;i<5;i++) {
            runningAnimation.cancel();
        }
        animation.getFinishLatch().await();
         assertFalse(runningAnimation.isRunning());
        int size = screenA.size();
        int size2 = screenB.size();
        Thread.sleep(20);
        //double check that it has actually been cancelled
        assertEquals(size,screenA.size());
        assertEquals(size2,screenB.size());

        //none of these should do anything
        runningAnimation.start();
        runningAnimation.run();
        Thread.sleep(20);
        //double check that none of them did anything
        assertEquals(size,screenA.size());
        assertEquals(size2,screenB.size());
        assertEquals(0,animation.getCurrentlyRunning().size());
    }

    private void testScreens(TestAnimation animation) {
        animation.setFinishLatch(new CountDownLatch(1));
        List<String> screenA = new CopyOnWriteArrayList<>();
        List<String> screenB = new CopyOnWriteArrayList<>();
        screenB.add("aaa");
        RunningAnimation<List<String>> sTest = animation.start();
        //ensure that the screened animation adds the screens correctly.
        assertEquals(sTest.getScreens().size(),0);
        assertFalse(animation.getFrames().hasNext());
        sTest.addScreen(screenA);
        assertEquals(sTest.getScreens().size(), 1);
        sTest.removeScreen(screenA);
        assertEquals(sTest.getScreens().size(),0);
        sTest.addScreen(screenA);
        sTest.addScreen(screenB);
        assertEquals(2,sTest.getScreens().size());
        try {
            animation.getFinishLatch().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}