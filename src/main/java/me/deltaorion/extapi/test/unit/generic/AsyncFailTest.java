package me.deltaorion.extapi.test.unit.generic;

import junit.framework.Assert;

public class AsyncFailTest implements MinecraftTest {


    public void runTime() {
        throw new RuntimeException();
    }


    public void assertion() {
        Assert.fail();
    }


    public void block() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getName() {
        return "Async Fail Test";
    }
}
