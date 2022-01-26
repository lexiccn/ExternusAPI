package me.deltaorion.extapi.test.unit.generic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class McTester {

    private final List<MinecraftTest> tests;

    public McTester() {
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
            try {
                runTest(test);
            } catch (Exception e) {
                e.getCause().printStackTrace();
            }
        }
    }

    public void clearTests() {
        this.tests.clear();
    }
}
