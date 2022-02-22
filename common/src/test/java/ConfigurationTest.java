import me.deltaorion.common.config.properties.PropertiesFileConfig;
import me.deltaorion.common.test.shared.ConfigTest;
import org.junit.Test;

import java.io.IOException;

public class ConfigurationTest {

    @Test
    public void test() throws IOException {
        ConfigTest test = new ConfigTest();
        PropertiesFileConfig config = PropertiesFileConfig.loadConfiguration(getClass().getClassLoader().getResourceAsStream("config.properties"));
        PropertiesFileConfig defaults = PropertiesFileConfig.loadConfiguration(getClass().getClassLoader().getResourceAsStream("defaults.properties"));

        test.test(config,defaults);
    }
}
