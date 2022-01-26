package me.deltaorion.extapi.command.sent;

import me.deltaorion.extapi.locale.message.Message;

public class ArgumentErrors {

    private ArgumentErrors() {
        throw new UnsupportedOperationException();
    }

    public static Message NOT_INTEGER() {
        return Message.valueOfTranslatable("command.notInteger");
    }

    public static Message NOT_NUMBER() {
        return Message.valueOfTranslatable("command.notNumber");
    }

    public static Message NOT_BOOLEAN() {
        return Message.valueOfTranslatable("command.notBoolean");
    }

    public static Message NOT_ONLINE_PLAYER() {
        return Message.valueOfTranslatable("command.notPlayer");
    }

    public static Message NOT_ENUM() {
        return Message.valueOfTranslatable("command.notEnum");
    }

    public static Message NOT_DURATION() {
        return Message.valueOfTranslatable("command.notDuration");
    }

    public static Message BAD_ARGUMENT() {
        return Message.valueOfTranslatable("command.invalidArgumentOther");
    }

    public static Message BAD_USAGE() {
        return Message.valueOfTranslatable("command.invalidUsage");
    }

    public static Message INTERNAL_ERROR() { return Message.valueOfTranslatable("command.internalError");}

    public static Message NO_PERMISSION() { return Message.valueOfTranslatable("command.noPermission");}

    public static Message INTERNAL_ERROR_TAB_COMPLETION() {
        return Message.valueOfTranslatable("command.internalErrorTab");
    }
}
