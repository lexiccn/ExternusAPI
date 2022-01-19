import me.deltaorion.extapi.config.Configuration;
import me.deltaorion.extapi.config.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.junit.Assert.*;

public class ConfigurationTests {

    @Test
    public void testStorageConfiguration() {
        try {
            URI uri = getClass().getClassLoader().getResource("config.yml").toURI();
            Configuration configuration = new FileConfiguration(new File(uri).toPath());
            testConfigBasic(configuration);
        } catch (Exception e) {
            fail();
        }
    }

    public void testConfigBasic(Configuration configuration) {
        try {
            assertEquals(configuration.getConfig().getString("a"),"b");
            assertTrue(configuration.getConfig().getBoolean("b"));
            ConfigurationSection section = configuration.getConfig().getConfigurationSection("c");
            section.getKeys(false).forEach(key -> {
                assertEquals(key,section.getString(key));
            });
        } catch (Exception e) {
            fail();
        }
    }
}
