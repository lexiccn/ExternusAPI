import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import me.deltaorion.extapi.test.animation.StateAnimationTest;
import me.deltaorion.extapi.test.animation.TestAnimation;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

public class AnimationTest {

    private final TestServer eServer = new TestServer();
    private final TestPlugin plugin = new TestPlugin(eServer);

    public AnimationTest() {
        eServer.addPlugin("Test",plugin);
    }

    @Test
    public void testBukkitUnit() {
        //ensure that the bukkit tick time unit correctly represents 50 milliseconds and all the surrounding
        //temporal unit functions work
        assertEquals(BukkitServer.MILLIS_PER_TICK, Duration.of(1,BukkitServer.TICK_UNIT).toMillis());
        assertEquals(BukkitServer.MILLIS_PER_TICK*20,Duration.of(20,BukkitServer.TICK_UNIT).toMillis());
        assertEquals(1,Duration.of(1000/BukkitServer.MILLIS_PER_TICK*60,BukkitServer.TICK_UNIT).toMinutes());
    }

    @Test
    public void frameTest() {
        MinecraftFrame<String> frame = new MinecraftFrame<>("Gamer",50);
        MinecraftFrame<String> frame2 = new MinecraftFrame<>("World",50,BukkitServer.TICK_UNIT);
        MinecraftFrame<String> frame3 = new MinecraftFrame<>("Delta",50, ChronoUnit.MINUTES);
        MinecraftFrame<String> frame4 = new MinecraftFrame<>("Gamer",50);

        assertEquals("Gamer",frame.getObject());
        assertEquals("World",frame2.getObject());
        assertEquals("Delta",frame3.getObject());

        assertEquals(50,frame.getTime());
        assertEquals(50*BukkitServer.MILLIS_PER_TICK,frame2.getTime());
        assertEquals(50*1000*60,frame3.getTime());
        assertEquals(frame,frame4);
    }

    @Test
    public void simpleAnimationTest() {
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
        testFunctionalityAndCancellation(animation);
        testCorrectness(animation);
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

    @Test
    public void stateAnimationTest() {
        int amount = 1000;
        int nAnimations = 10;

        CountDownLatch finishLatch = new CountDownLatch(nAnimations);
        StateAnimationTest stateAnimationTest = new StateAnimationTest(plugin,finishLatch);
        for(int i=0;i<amount;i++) {
            stateAnimationTest.addFrame(new MinecraftFrame<>(String.valueOf(i),0));
        }
        List<List<String>> screens = new ArrayList<>();
        for(int i=0;i<nAnimations;i++) {
            List<String> screen = new ArrayList<>();
            stateAnimationTest.start(screen);
            screens.add(screen);
        }
        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int j=0;j<nAnimations;j++) {
            List<String> screenA = screens.get(j);
            int i = 0;
            int even = 0;
            int odd = 0;
            final int size = amount * 2 - 1;
            while (i < size) {
                assertEquals(String.valueOf(even), screenA.get(i));
                even++;
                i++;
                if (i > 1) {
                    assertEquals(String.valueOf(odd), screenA.get(i));
                    i++;
                    odd++;
                }
            }
            assertEquals(size, screenA.size()); //make sure we get no extras lol
        }
        assertEquals(0,stateAnimationTest.getCurrentlyRunning().size());
    }

    @Test
    public void repeatAnimationTest() {
        repeatTest(TestAnimation.create(plugin,AnimationFactories.SCHEDULE_ASYNC(),true));
        repeatTest(TestAnimation.create(plugin,AnimationFactories.SLEEP_ASYNC(),true));
    }

    public void repeatTest(TestAnimation animation) {
        final int nAnimations = 10;
        final int nFrames = 10;
        final int nRepeats = 5;
        final int latchSize = nFrames*nAnimations*nRepeats;
        List<List<String>> screens = new ArrayList<>();
        animation.setFrameLatch(new CountDownLatch(latchSize));

        for(int i=0;i<nFrames;i++) {
            animation.addFrame(new MinecraftFrame<>("Gamer",0));
        }

        animation.setFinishLatch(new CountDownLatch(nAnimations*nRepeats));
        for(int i=0;i<nAnimations;i++) {
            List<String> screen = new ArrayList<>();
            screens.add(screen);
            animation.start(screen);
        }

        try {
            animation.getFrameLatch().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        animation.setStopAllLatch(new CountDownLatch(1));
        animation.stopAll();
        try {
            animation.getStopAllLatch().await();
            animation.getFinishLatch().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int count = 0;
        for (List<String> screen : screens) {
            count += screen.size();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int newCount = 0;
        for (List<String> screen : screens) {
            newCount += screen.size();
        }
        assertEquals(newCount,count);
        assertEquals(0,animation.getCurrentlyRunning().size());

    }
}
