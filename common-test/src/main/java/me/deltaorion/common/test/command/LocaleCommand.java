package me.deltaorion.common.test.command;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.IChatColor;
import me.deltaorion.common.locale.message.Message;
import me.deltaorion.common.locale.translator.Translator;
import me.deltaorion.common.plugin.sender.Sender;

import java.util.Locale;

public class LocaleCommand extends FunctionalCommand {
    public LocaleCommand() {
        super(APIPermissions.COMMAND);
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        Sender sender = command.getSender();
        sender.sendMessage(Translator.getInstance().translate("hello", Translator.parseLocale("en_PT")));
        sender.sendMessage(Translator.getInstance().translate("world.hello",Translator.parseLocale("en_PT")));

        Message middle = Message.valueOf("Gam{0}rs Unite!");
        sender.sendMessage(middle.toString("e"));

        Message end = Message.valueOf("Gamer{0}");
        sender.sendMessage(end.toString("s"));

        Message start = Message.valueOf("{0} Unite!");
        sender.sendMessage(start.toString("Gamers"));

        Message translatable = Message.valueOfTranslatable("hello");
        sender.sendMessage(translatable);
        sender.sendMessage(translatable.toString(Translator.parseLocale("en_PT")));
        sender.sendMessage(translatable.toString(Locale.CANADA_FRENCH));

        Message everything = Message.valueOfBuilder(builder -> {
            builder.appendTranslatable("hello")
                    .append(" &e {0} {1} {2} ")
                    .style(IChatColor.BLACK)
                    .append(" Gamer");
        });

        sender.sendMessage(everything.toString("gamer {0}",true,6.5));
        sender.sendMessage(everything.toString("abc"));

        Message defArgs = Message.valueOfBuilder( builder -> {
            builder.append("hello ")
                    .append("{0}")
                    .defArg("world");
        });

        sender.sendMessage(defArgs);
        sender.sendMessage(defArgs.toString("gamer"));

        everything.setDefaults("a",7.5f,"b");

        sender.sendMessage(everything.toString("abc"));
        sender.sendMessage(everything.toString("a.b","bc","e"));

        sender.sendMessage("Translated ----- "+Message.valueOfTranslatable("hello").toString(sender.getLocale()));
    }
}
