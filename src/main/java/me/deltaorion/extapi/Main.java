package me.deltaorion.extapi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import me.deltaorion.extapi.animation.MinecraftFrame;
import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.animation.factory.AnimationFactories;
import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import me.deltaorion.extapi.test.animation.LoggingAnimation;
import me.deltaorion.extapi.test.animation.TestAnimation;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Main {

    public static Main main = new Main();

    public static void main(String[] args) {
        try {
            main.run();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void run() throws URISyntaxException {
        TestServer server = new TestServer();
        TestPlugin plugin = new TestPlugin(server);
        server.addPlugin("Test",plugin);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Disable");
        plugin.onDisable();
    }


}
