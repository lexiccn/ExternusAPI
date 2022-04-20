package me.deltaorion.common.test.unit;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
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
    public void test() throws IOException, InvalidConfigurationException {
        FileConfig config = FileConfig.loadConfiguration(factory, getClass().getClassLoader().getResourceAsStream("me.deltaorion.extapi/config.yml"));
        FileConfig defaults = FileConfig.loadConfiguration(factory, getClass().getClassLoader().getResourceAsStream("me.deltaorion.extapi/defaults.yml"));

        ConfigTest test = new ConfigTest();
        //test.test(config,defaults);
        test.test(config,defaults);
    }

    @Override
    public String getName() {
        return "Configuration Test";
    }
}
