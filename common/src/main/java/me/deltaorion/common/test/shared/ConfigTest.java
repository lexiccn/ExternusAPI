package me.deltaorion.common.test.shared;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.value.ConfigValue;

import static org.junit.Assert.*;

public class ConfigTest {

    public void test(FileConfig config, FileConfig defaults) {
        testBasicSection(config);
        testDefaults(config,defaults);
        if(config.supportsNesting()) {
            nestedTest(config,defaults);
        }
    }

    private void testBasicSection(FileConfig config) {
        checkSet(config);
        testTyping(config);
    }

    private void testDefaults(FileConfig config, FileConfig defaults) {
        config.setDefaults(defaults);
        basicDefaultsTest(config,false);
        basicDefaultsTest(config,true);
        config.mergeDefaults();
        basicDefaultsTest(config,true,true);
    }

    private void basicDefaultsTest(MemoryConfig config, boolean copyDefaults) {
        basicDefaultsTest(config,copyDefaults,false);
    }

    private void basicDefaultsTest(MemoryConfig config, boolean copyDefaults, boolean merge) {
        if(!merge) {
            config.options().copyDefaults(copyDefaults);
        }
        assertTrue(config.isSet("a"));
        assertTrue(config.isSet("i"));
        assertEquals(config.isSet("j"),copyDefaults);
        assertTrue(config.contains("k"));
        assertEquals(config.isSet("k"),copyDefaults);

        assertEquals("epic",config.getString("k"));
        config.addDefault("epic","k");
        assertEquals("k",config.getString("epic"));
        assertEquals(copyDefaults,config.isSet("epic"));

        char end = copyDefaults ? 'k' : 'i';

        for(char x = 'a'; x<=end;x++) {
            checkPath(config,String.valueOf(x));
        }
    }

    private void checkSet(FileConfig config) {
        for(char x = 'a'; x<='i';x++) {
            checkPath(config,String.valueOf(x));
        }
    }

    private void testTyping(FileConfig config) {
        ConfigSection section = config.getRoot();

        assertNull(section.getParent());

        assertEquals(section.getString("a"),"b");
        assertEquals(section.getInt("a"), ConfigValue.DEFAULT_NUMBER);
        assertEquals(section.getDouble("a"),ConfigValue.DEFAULT_NUMBER,0.001);
        assertFalse(section.getBoolean("a"));
        assertEquals(section.get("a"),"b");

        assertEquals(section.getInt("a",3),3);
        assertEquals(section.getDouble("a",3.14),3.14,0.001);
        assertTrue(section.getBoolean("a", true));
        assertEquals(section.getString("a","a"),"b");
        assertEquals(section.get("a","a"),"b");

        config.addDefault("a", 3);

        assertEquals(config.getInt("a"),3);
        config.addDefault("a",3.14);
        assertEquals(config.getInt("a"),3);
        assertEquals(config.getInt("a",4),4);
        assertTrue(config.getBoolean("a", true));
        assertEquals(config.get("a",null),"b");

        assertNull(config.get("abcdefgh"));
        assertTrue(config.getBoolean("abcdefgh",true));
        assertNull(config.getString("abcdefgh"));

        assertNull(config.get("abcdefj.roepgk.woep"));
        assertTrue(config.getBoolean("eopwk.ewoifpk.ewop",true));
    }

    private void checkPath(ConfigSection section, String path) {
        assertTrue(section.contains("a"));
        assertTrue(section.isSet("a"));
        assertTrue(section.getKeys(true).contains(path));
        assertTrue(section.getValues(true).containsKey(path));
    }

    public void nestedTest(FileConfig config, FileConfig defaults) {
        config.setDefaults(defaults);
        config.options().copyDefaults(true);
        config.options().pathSeparator('.');
        assertTrue(config.isConfigurationSection("nest"));

        basicNestTest(config);
    }

    private void basicNestTest(FileConfig config) {
        ConfigSection section = config.getConfigurationSection("nest");
        assertEquals(config.get("nest.f"),section.get("f"));
        assertTrue(config.isSet("nest.f"));
        assertTrue(section.isSet("f"));
        assertTrue(config.contains("nest.k"));
        assertTrue(section.contains("k"));
        assertFalse(section.isSet("k"));

        assertEquals(config.getConfigurationSection("nest.i"),section.getConfigurationSection("i"));
        ConfigSection keys = config.getConfigurationSection("nest.i.j");
        assertEquals(4,keys.getKeys(true).size());
        for(String key : keys.getKeys(false)) {
            assertEquals(key,keys.get(key));
        }
    }
}
