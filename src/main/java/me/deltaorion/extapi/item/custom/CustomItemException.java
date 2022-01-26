package me.deltaorion.extapi.item.custom;

/**
 * Thrown when external code in the custom item is ran and fails.
 */
public class CustomItemException extends Exception {

    public CustomItemException(String message, Throwable e) {
        super(message,e);
    }

    public CustomItemException(String message) {
        super(message);
    }
}
