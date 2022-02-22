package me.deltaorion.common.command;

import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A command represents a sequence of events that should occur when a command is sent. This abstract interface details the raw
 * functionality of what should happen given a SentCommand. It should also detail how to give tab completions given that these arguments
 * are sent by a user.
 *
 * Any implementing interface must take into account permissions. Each command should have a permission to use it. If for whatever reason
 * it must not have a permission then the permission should be {@link #NO_PERMISSION}.
 *
 * An implementing interface may have functional command arguments. These are not the same as {@link me.deltaorion.common.command.sent.CommandArg}.
 * A subcommand is another command who's logic should entail if a given argument is typed. A functional command argument is the argument
 * that needs to be typed to activate the subcommand. This can be recursively chained to form a command tree.
 *
 * The main implementation of a command is a {@link FunctionalCommand} which abstracts a lot of command logic given a subcommand.
 */

public interface Command {

    String NO_PERMISSION = "";
    String NO_USAGE = "";

    /**
     * What permission is required to run this command. It is important that (assuming the permission isn't no permission) that the
     * user cannot run the command if they don't have the permission, and that they don't receive any tab completions. If there is no
     * permission then this should return {@link #NO_PERMISSION}
     *
     * @return The permission needed to use this command.
     */
    @NotNull
    String getPermission();

    /**
     * @return A short description detailing the commands functionality. If there is no description this must return null.
     */
    @Nullable
    Message getDescription();

    /**
     *
     * @return very brief instruction on how to use the command. If there is no usage then this should return {@link #NO_USAGE}
     */

    @NotNull
    String getUsage();

    /**
     * Describes the functionality on what should happen when the command is run. All error handling should
     * be handled inside of this function. No command exception should be leaked.
     *
     * @param command The command what was sent by the user
     */
    void onCommand(SentCommand command);

    /**
     *
     * @return An immutable map detailing all of the functional command arguments and which commands
     * they map to
     */

    @NotNull
    Map<String,Command> getFunctions();

    /**
     *
     * @return A set of all of the functional command arguments for this command
     */
    @NotNull
    Collection<String> getCommandArgs();

    /**
     * Should retrieve a list of tab completions. This is not a list of possible words and is rather a final
     * and complete list that should be sent. The final argument in the command is the prefix. All suggestions must
     * start with the prefix. The sent command does not represent a command that has been sent, rather a list of
     * arguments that have been typed so far.
     *
     * All error handling should be handled here and
     *
     * @param command A sent command representing what has currently been typed by the sender.
     * @return A final list of tab completions that should be suggested to the user
     */

    @NotNull
    List<String> onTabCompletion(SentCommand command);
}
