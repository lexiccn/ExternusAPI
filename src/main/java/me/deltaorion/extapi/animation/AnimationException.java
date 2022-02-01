package me.deltaorion.extapi.animation;

public class AnimationException extends Exception {

    public AnimationException(String message) {
        super(message);
    }

    public AnimationException(String message, Throwable e) {
        super(message,e);
    }
}
