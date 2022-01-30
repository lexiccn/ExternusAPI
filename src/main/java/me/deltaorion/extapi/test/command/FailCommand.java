package me.deltaorion.extapi.test.command;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.command.tabcompletion.CompletionSupplier;

import java.util.List;

public class FailCommand extends FunctionalCommand {
    public FailCommand() {
        super("ExtApi.Test.Command");
        registerCompleter(1, new CompletionSupplier() {
            @Override
            public List<String> getCompletions(SentCommand command) throws CommandException {
                throw new RuntimeException();
            }
        });
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        throw new RuntimeException();
    }


}
