package me.deltaorion.common.test.command;

import me.deltaorion.common.APIPermissions;
import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.FunctionalCommand;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.command.tabcompletion.CompletionSupplier;

import java.util.List;

public class FailCommand extends FunctionalCommand {

    public FailCommand() {
        super(APIPermissions.COMMAND);
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
