package me.deltaorion.common.test.shared;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.junit.Assert.*;

public class ConfigTest {

    public void test(@NotNull FileConfig config, @NotNull FileConfig defaults) {
        testBasicSection(config);
        testDefaults(config,defaults);
        valuesTest(config,defaults);
        if(config.supportsNesting()) {
            nestedTest(config,defaults);
        }
    }

    private void valuesTest(FileConfig config, FileConfig defaults) {
        config.setDefaults(defaults);
        config.options().copyDefaults(true);
        Map<String,Object> values = config.getValues(true);
        for(Map.Entry<String,Object> value : values.entrySet()) {
            assertEquals(value.getValue(),config.get(value.getKey()));
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
        assertEquals("b",config.getString("a"));
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
        assertTrue(section.contains(path));
        assertTrue(section.isSet(path));
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
        ConfigSection nest = config.getConfigurationSection("nest");
        assertEquals(config.get("nest.f"),nest.get("f"));
        assertTrue(config.isSet("nest.f"));
        assertTrue(nest.isSet("f"));
        assertTrue(config.contains("nest.k"));
        assertTrue(nest.contains("k"));

        assertEquals(config.getConfigurationSection("nest.i"),nest.getConfigurationSection("i"));
        ConfigSection keys = config.getConfigurationSection("nest.i.j");
        assertEquals(4,keys.getKeys(true).size());
        for(String key : keys.getKeys(false)) {
            assertEquals(key,keys.get(key));
        }

        ConfigSection created = config.createSection("nest.n");
        ConfigSection get = nest.getConfigurationSection("n");
        created.set("l",3);
        created.set("m",4);
        created.set("n",5);
        assertEquals(created,get);
        assertEquals(get.get("m"),created.get("m"));
    }
}
