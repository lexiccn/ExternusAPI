package me.deltaorion.common.test.unit;

import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;

import static org.junit.Assert.*;

public class McTestTest implements MinecraftTest {

    @McTest
    public void testPass() {
        assertFalse(false);
        assertNotNull(new Object());
        assertTrue(true);
        assertNull(null);
        assertEquals("hello","hello");
    }

    @McTest
    public void testFail() {
        try {
            assertEquals("hello", "Hello");
        } catch (AssertionError e) {
            return;
        }

        fail();
    }

    public void dontRun() {
        fail("This should not be run at all!");
    }


    @Override
    public String getName() {
        return "Minecraft Test Initial";
    }
}
