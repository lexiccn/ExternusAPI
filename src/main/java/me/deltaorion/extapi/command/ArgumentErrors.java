package me.deltaorion.extapi.command;

import me.deltaorion.extapi.locale.message.Message;

public class ArgumentErrors {

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
        return Message.valueOfTranslatable("command.notOnlinePlayer");
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
}
