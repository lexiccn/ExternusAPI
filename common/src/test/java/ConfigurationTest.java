import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.InvalidConfigurationException;
import me.deltaorion.common.config.yaml.YamlAdapter;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.test.shared.ConfigTest;
import org.junit.Test;

import java.io.IOException;

public class ConfigurationTest {

    @Test
    public void test() throws IOException, InvalidConfigurationException {
        ConfigTest test = new ConfigTest();
        FileConfig config = FileConfig.loadConfiguration(new PropertiesAdapter(),getClass().getClassLoader().getResourceAsStream("config.properties"));
        FileConfig defaults = FileConfig.loadConfiguration(new PropertiesAdapter(),getClass().getClassLoader().getResourceAsStream("defaults.properties"));

        test.test(config,defaults);

        config = FileConfig.loadConfiguration(new YamlAdapter(),getClass().getClassLoader().getResourceAsStream("config.yml"));
        defaults = FileConfig.loadConfiguration(new YamlAdapter(),getClass().getClassLoader().getResourceAsStream("defaults.yml"));

        test.test(config,defaults);
    }
}
