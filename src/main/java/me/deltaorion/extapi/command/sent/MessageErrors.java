package me.deltaorion.extapi.command.sent;

import me.deltaorion.extapi.locale.message.Message;

/**
 * This is a static class that provides quick and easy access to verbose errors that should be sent to a player. As these
 * are being sent to a player all messages must fully support the locale library. That is, they must be translatable messages. These
 * translatable messages should be able to be overridden by the user in a custom 'lan.yml' file
 *
 * If the translatable message has an argument that needs to be parsed this should be documented.
 */

public class MessageErrors {

    private MessageErrors() {
        throw new UnsupportedOperationException();
    }

    /*
     * Should be sent if a sent command cannot be parsed to an integer.
     * Args
     *    - the string value that could not be parsed
     */
    public static Message NOT_INTEGER() {
        return Message.valueOfTranslatable("command.notInteger");
    }

    /*
     * Should be sent if a sent command cannot be parsed to a double or float
     * Args
     *    - the string value that could not be parsed
     */
    public static Message NOT_NUMBER() {
        return Message.valueOfTranslatable("command.notNumber");
    }

    /*
     * Should be sent if a sent command cannot be parsed to an boolean
     * Args
     *    - the string value that could not be parsed
     */
    public static Message NOT_BOOLEAN() {
        return Message.valueOfTranslatable("command.notBoolean");
    }

    /*
     * Should be sent if a sent command cannot be parsed to an online player or sender
     * Args
     *    - the string value that could not be parsed
     */
    public static Message NOT_ONLINE_PLAYER() {
        return Message.valueOfTranslatable("command.notPlayer");
    }

    /*
     * Should be sent if a sent command cannot be parsed to a generic enum type
     * Args
     *    - the string value that could not be parsed
     *    - A name to describe an enum. For example, if you were parsing an org.bukkit.Material you could call it 'material'
     */
    public static Message NOT_ENUM() {
        return Message.valueOfTranslatable("command.notEnum");
    }

    /*
     * Should be sent if a sent command cannot be parsed to a duration
     * Args
     *    - the string value that could not be parsed
     */
    public static Message NOT_DURATION() {
        return Message.valueOfTranslatable("command.notDuration");
    }
    /*
     * Should be sent if a sent command cannot be parsed to a generic type
     * Args
     *    - the string value that could not be parsed
     */
    public static Message BAD_ARGUMENT() {
        return Message.valueOfTranslatable("command.invalidArgumentOther");
    }

    /*
     * Should be sent if the command sender used the command incorrectly.
     * Args
     *    - The correct usage
     */
    public static Message BAD_USAGE() {
        return Message.valueOfTranslatable("command.invalidUsage");
    }

    /*
     * Sent when the command throws a runtime error or other throwable
     * Args
     *    - the command label
     *    - the sent args
     */
    public static Message INTERNAL_ERROR_COMMAND() { return Message.valueOfTranslatable("command.internalError");}

    /*
     * Sent when a custom item throws a runtime error or other throwable
     * Args
     *    - the name of the item
     */
    public static Message INTERNAL_ERROR_ITEM() { return Message.valueOfTranslatable("item.internalError");}

    /*
     * Sent if the user has no permission to run the command
     * Args
     *
     */
    public static Message NO_PERMISSION() { return Message.valueOfTranslatable("command.noPermission");}

    /*
     * Sent when a runtime error or other throwable is thrown when attempting to tab complete a command
     * Args
     *    - the command label
     *    - the current args
     */
    public static Message INTERNAL_ERROR_TAB_COMPLETION() {
        return Message.valueOfTranslatable("command.internalErrorTab");
    }
}
