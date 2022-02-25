package me.deltaorion.common.test.unit;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.adapter.AdapterFactory;
import me.deltaorion.common.config.memory.MemoryFileConfig;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import me.deltaorion.common.test.shared.ConfigTest;

import java.io.IOException;

public class ConfigurationTest implements MinecraftTest {

    private final ConfigTest test;
    private final AdapterFactory factory;

    public ConfigurationTest(AdapterFactory factory) {
        this.factory = factory;
        this.test = new ConfigTest();
    }

    @McTest
    public void test() {
        FileConfig config = null;
        FileConfig defaults = null;
        try {
            config = MemoryFileConfig.loadConfiguration(factory, getClass().getClassLoader().getResourceAsStream("config.yml"));
            defaults = MemoryFileConfig.loadConfiguration(factory, getClass().getClassLoader().getResourceAsStream("defaults.yml"));

            ConfigTest test = new ConfigTest();
            //test.test(config,defaults);
            test.test(config,defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Configuration Test";
    }
}
