package me.deltaorion.extapi.test.unit;

import java.util.Objects;

public interface MinecraftTest {

     default void fail() {
        throw new AssertionError("Test Failed");
    }

    default void assertTrue(boolean x) {
        if(!x)
            throw new AssertionError("Could not verify that the statement was true");
    }

    default void assertFalse(boolean x) {
        if(x)
            throw new AssertionError("Could not verify that the statement was false");
    }

    default void assertNotNull(Object o) {
        if(o == null)
            throw new AssertionError("The given object was null");
    }

    default void assertNull(Object o) {
        if(o != null)
            throw new AssertionError("The given '"+o+"' is not null");
    }

    default void assetEquals(Object o1, Object o2) {
        if(!Objects.equals(o1,o2))
            throw new AssertionError("Could not verify the objects were equal. Received '"+o1+"' and '"+o2+"'");
    }

}
