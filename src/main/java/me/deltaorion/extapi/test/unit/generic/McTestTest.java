package me.deltaorion.extapi.test.unit.generic;

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


}
