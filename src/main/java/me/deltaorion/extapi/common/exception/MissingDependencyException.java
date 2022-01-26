package me.deltaorion.extapi.common.exception;

/**
 * Thrown when the relevant dependency is not loaded on the server.
 */
public class MissingDependencyException extends RuntimeException {

    public MissingDependencyException(String message) {
        super(message);
    }

    public MissingDependencyException(String message, Throwable e) {
        super(message,e);
    }


}
