package me.deltaorion.common.test.unit;

import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.properties.PropertiesConfigLoader;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import me.deltaorion.common.test.shared.LocalizationTest;

public class LocaleTest implements MinecraftTest {

    private final ApiPlugin plugin;
    private final LocalizationTest test;

    public LocaleTest(ApiPlugin plugin) {
        this.plugin = plugin;
        this.test = new LocalizationTest();
        pullTestLocales();
    }

    private void pullTestLocales() {
        ConfigLoader configFr = new PropertiesConfigLoader(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("fr.properties"),"translations/fr.properties");
        ConfigLoader configPT = new PropertiesConfigLoader(getClass().getClassLoader(),plugin.getDataDirectory().resolve("translations").resolve("en_PT.properties"),"translations/en_PT.properties");
        plugin.getTranslator().reload();
    }

    @McTest
    public void testTranslationManager() {
        test.testTranslationManager();
    }

    @McTest
    public void testMessage() {
        test.testMessage();
    }

    @Override
    public String getName() {
        return "Locale Test";
    }
}
