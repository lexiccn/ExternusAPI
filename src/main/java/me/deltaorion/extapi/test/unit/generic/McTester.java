package me.deltaorion.extapi.test.unit.generic;

import me.deltaorion.extapi.common.plugin.EPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class McTester {

    private final EPlugin plugin;
    private final List<MinecraftTest> tests;

    public McTester(EPlugin plugin) {
        this.plugin = plugin;
        this.tests = new ArrayList<>();
    }

    public void addTest(MinecraftTest test) {
        this.tests.add(test);
    }

    private void runTest(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(McTest.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

    public void runTests() {
        for(MinecraftTest test : tests) {
            plugin.getPluginLogger().info("Running Test ["+test.getName()+"]");
            try {
                runTest(test);
            } catch (Exception e) {
                e.getCause().printStackTrace();
            }
        }
        plugin.getPluginLogger().info("All Tests - Complete");
    }

    public void clearTests() {
        this.tests.clear();
    }
}
