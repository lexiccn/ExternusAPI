package me.deltaorion.extapi.test.unit;

public class McTestTest implements MinecraftTest {

    @McTest
    public void testPass() {
        System.out.println("This test will PASS!");
        assertFalse(false);
        assertNotNull(new Object());
        assertTrue(true);
        assertNull(null);
        assetEquals("hello","hello");
    }

    @McTest
    public void testFail() {
        System.out.println("This test will FAIL!");
        assetEquals("hello","Hello");
    }


}
