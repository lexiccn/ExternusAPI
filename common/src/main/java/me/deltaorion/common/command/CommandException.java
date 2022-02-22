package me.deltaorion.common.command;

/**
 * This exception should be thrown when the user does something wrong with a sent command. For example if they forget an argument or
 * don't type a number where required.
 *
 * The error message must be sent to the user.
 */
public class CommandException extends Exception {

    /**
     * @param errMessage The final error message that will be sent to the user
     */
    public CommandException(String errMessage) {
        super(errMessage);
    }
}
