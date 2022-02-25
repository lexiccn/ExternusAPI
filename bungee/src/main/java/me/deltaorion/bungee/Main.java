package me.deltaorion.bungee;

import me.deltaorion.bungee.configuration.YamlAdapter;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.memory.MemoryFileConfig;
import me.deltaorion.common.test.shared.ConfigTest;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) {
        FileConfig config = null;
        FileConfig defaults = null;
        try {
            config = MemoryFileConfig.loadConfiguration(new YamlAdapter(), Main.class.getClassLoader().getResourceAsStream("config.yml"));
            defaults = MemoryFileConfig.loadConfiguration(new YamlAdapter(), Main.class.getClassLoader().getResourceAsStream("defaults.yml"));

            ConfigTest test = new ConfigTest();
            //test.test(config,defaults);
            test.test(config,defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
