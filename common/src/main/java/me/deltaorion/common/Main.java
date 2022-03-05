package me.deltaorion.common;

import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.nested.yaml.YamlAdapter;
import me.deltaorion.common.test.shared.ConfigTest;

import java.io.PrintWriter;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            ConfigTest test = new ConfigTest();
            FileConfig config = FileConfig.loadConfiguration(new YamlAdapter(),Main.class.getClassLoader().getResourceAsStream("config.yml"));
            FileConfig defaults = FileConfig.loadConfiguration(new YamlAdapter(),Main.class.getClassLoader().getResourceAsStream("defaults.yml"));
            /*
            for(Map.Entry<String,Object> value : config.getValues(true).entrySet()) {
                System.out.println(value.getKey() + ": "+value.getValue());
            }
             */
            long time = System.nanoTime();
            //config.createSection("nest");
            long end = System.nanoTime();
            test.test(config,defaults);
            System.out.println("Time Elapsed --- "+(end-time));

            //config.save(new PrintWriter(System.out));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
