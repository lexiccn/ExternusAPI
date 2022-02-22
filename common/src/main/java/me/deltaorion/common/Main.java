package me.deltaorion.common;

import me.deltaorion.common.test.mock.TestPlugin;
import me.deltaorion.common.test.mock.TestServer;

public class Main {

    public static void main(String[] args) {
        TestServer eServer = new TestServer();
       TestPlugin plugin = new TestPlugin(eServer);
    }

}
