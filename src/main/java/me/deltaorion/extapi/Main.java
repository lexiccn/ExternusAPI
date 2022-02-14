package me.deltaorion.extapi;

import me.deltaorion.extapi.test.TestPlugin;
import me.deltaorion.extapi.test.TestServer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.util.UUID;

public class Main {

    public static Main main = new Main();

    public static void main(String[] args) {
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

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        plugin.onDisable();
    }

}