package me.deltaorion.common;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.memory.MemoryFileConfig;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.config.properties.PropertiesConfigAdapter;
import me.deltaorion.common.test.mock.TestPlugin;
import me.deltaorion.common.test.mock.TestServer;
import me.deltaorion.common.test.shared.ConfigTest;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            ConfigTest test = new ConfigTest();
            FileConfig config = MemoryFileConfig.loadConfiguration(new PropertiesAdapter(), Main.class.getClassLoader().getResourceAsStream("config.properties"));
            FileConfig defaults = MemoryFileConfig.loadConfiguration(new PropertiesAdapter(), Main.class.getClassLoader().getResourceAsStream("defaults.properties"));

            System.out.println(config.getKeys(true));
            System.out.println(config.getKeys(true));
        } catch (IOException e) {
            return;
        }
    }

}
