import com.google.common.collect.ImmutableSet;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import me.deltaorion.extapi.test.animation.TestAnimation;
import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class AnimationTest {

    private final TestServer eServer = new TestServer();
    private final TestPlugin plugin = new TestPlugin(eServer);

    public AnimationTest() {
        eServer.addPlugin("Test",plugin);
    }

    @Test
    public void testBukkitUnit() {
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
            testAnimation(new TestAnimation(plugin, AnimationFactories.SCHEDULE_ASYNC()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void testAnimation(TestAnimation animation) throws InterruptedException {
        List<String> screenA = new CopyOnWriteArrayList<>();
        List<String> screenB = new CopyOnWriteArrayList<>();
        screenB.add("aaa");
        assertEquals(animation.getScreens(), Collections.emptySet());
        assertEquals(animation.getFrames(),Collections.emptyList());
        animation.addScreen(screenA);
        assertEquals(animation.getScreens(), ImmutableSet.of(screenA));
        animation.removeScreen(screenA);
        assertEquals(animation.getScreens(),Collections.emptySet());
        animation.addScreen(screenA);
        animation.addScreen(screenB);
        assertEquals(2,animation.getScreens().size());
        animation.removeScreen(screenB);
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        animation.addFrame(new MinecraftFrame<>("Gamer",10));
        RunningAnimation runningAnimation = animation.start();
        Thread.sleep(5);
        assertTrue(runningAnimation.isAlive());
        animation.addScreen(screenB);
        Thread.sleep(20);
        assertTrue(screenA.size()>0);
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        animation.addFrame(new MinecraftFrame<>("Gamer",0));
        assertTrue(runningAnimation.isAlive());
        Thread.sleep(20);
        assertTrue(screenB.size()>1);
        assertTrue(screenA.size()>0);
        runningAnimation.cancel();
        Thread.sleep(20);
        assertFalse(runningAnimation.isAlive());
        int size = screenA.size();
        int size2 = screenB.size();
        Thread.sleep(20);
        assertEquals(size,screenA.size());
        assertEquals(size2,screenB.size());

        animation.clearFrames();
        for(int i=0;i<1000;i++) {
            animation.addFrame(new MinecraftFrame<>(String.valueOf(i),0));
        }
        animation.start();
        screenA.clear();
        screenB.clear();
        Thread.sleep(5);
        for(int i=0;i<1000;i++) {
            assertEquals(String.valueOf(i),screenA.get(i));
            assertEquals(String.valueOf(i),screenB.get(i));
        }
        assertEquals(1000,screenA.size()); //make sure we get no extras lol
    }
}
