package me.deltaorion.extapi.animation;

/**
 * Called when external animation rendering code fails.
 */
public class AnimationException extends Exception {

    public AnimationException(String message) {
        super(message);
    }

    public AnimationException(String message, Throwable e) {
        super(message,e);
    }
}
