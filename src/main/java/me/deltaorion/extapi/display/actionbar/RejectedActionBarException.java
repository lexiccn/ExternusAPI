package me.deltaorion.extapi.display.actionbar;

/**
 * Simple exception for any {@link RejectionPolicy} that rejects a sent action bar
 */
public class RejectedActionBarException extends RuntimeException {

    public RejectedActionBarException(String message) {
        super(message);
    }
}
