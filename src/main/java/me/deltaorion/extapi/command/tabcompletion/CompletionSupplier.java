package me.deltaorion.extapi.command.tabcompletion;

import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.sent.SentCommand;

import java.util.List;

public interface CompletionSupplier {

    List<String> getCompletions(SentCommand command) throws CommandException;
}
