package me.deltaorion.common;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.yaml.YamlAdapter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            String input = "dog: true";
            FileConfig config = FileConfig.loadConfiguration(new YamlAdapter(),new ByteArrayInputStream(input.getBytes()));
            System.out.println(config.getBoolean("dog"));
        } catch (IOException e) {
            System.err.println("Unable to load config");
        } catch (InvalidConfigurationException e) {
            System.err.println("SYNTAX ERROR");
        }
    }
}
