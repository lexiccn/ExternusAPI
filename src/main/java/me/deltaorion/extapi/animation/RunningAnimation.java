package me.deltaorion.extapi.animation;

public interface RunningAnimation extends Runnable {

    public void cancel();

    public boolean isAlive();

    public void start();
}
