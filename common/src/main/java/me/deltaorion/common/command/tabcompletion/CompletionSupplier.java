package me.deltaorion.common.command.tabcompletion;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.sent.SentCommand;

import java.util.List;

public interface CompletionSupplier {

    /**
     * Returns a list of possible tab completions given a sent command representation of the currently typed arguments.
     * The sent command in this case is not representing a command that has been sent by rather a list of currently typed arguments.
     * This method is typically called when given a certain positional index is being typed then get a
     * list of appropriate tab completions.
     *
     * Some generic CompletionSuppliers can be found in the static class {@link Completers}
     *
     * @param command A sent command representation of all of the args that have currently been typed
     * @return a list of possible completion options
     * @throws CommandException If this is thrown during tab completion for whatever reason then the user will be provided
     * with no additional tab completions.
     */
    List<String> getCompletions(SentCommand command) throws CommandException;
}
