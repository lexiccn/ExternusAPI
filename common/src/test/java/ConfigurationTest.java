import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.memory.MemoryFileConfig;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.config.properties.PropertiesConfigAdapter;
import me.deltaorion.common.test.shared.ConfigTest;
import org.junit.Test;

import java.io.IOException;

public class ConfigurationTest {

    @Test
    public void test() throws IOException {
        ConfigTest test = new ConfigTest();
        FileConfig config = MemoryFileConfig.loadConfiguration(new PropertiesAdapter(),getClass().getClassLoader().getResourceAsStream("config.properties"));
        FileConfig defaults = MemoryFileConfig.loadConfiguration(new PropertiesAdapter(),getClass().getClassLoader().getResourceAsStream("defaults.properties"));

        test.test(config,defaults);
    }
}
