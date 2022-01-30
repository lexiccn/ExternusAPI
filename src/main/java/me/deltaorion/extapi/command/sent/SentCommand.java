package me.deltaorion.extapi.command.sent;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * A sent command is a data structure that represents a command that was sent by a user. This class contains many methods
 * to retrieve information about the sent command such as who sent it, and methods to access arguments that may or may not be present.
 * This does not represent some abstract command structure or what should happen when a command is sent, rather just what was sent
 * by the user
 *   - A command is sent by either the console or a sender, this is wrapped in the generic {@link Sender} class. To get
 *     a player class you will have to use the plugins methods with the senders UUID.
 *   - The sent command has n arguments that were sent. The arguments can be iterated across or retrieved on a per index basis. If
 *     the arg doesn't exist then appropriate action can be taken
 *   - The command that sent this command has a usage, this is used if the user did not include a necessary argument.
 *   - A command is usually in the form /label arg1 arg2 arg3. The label would be 'label' and the args ['arg1','arg2',arg3']. Each argument
 *   has an index based on the positional order in which it was typed
 */

@Immutable
public class SentCommand implements Iterable<CommandArg> {

    @NotNull private final Sender sender;
    @NotNull private final List<CommandArg> args;
    @NotNull private final String label;
    @NotNull private final ApiPlugin plugin;
    @NotNull private final String usage;

    private SentCommand(@NotNull ApiPlugin plugin, @NotNull Sender sender, @NotNull List<CommandArg> args, @NotNull String label, @NotNull String usage) {
        this.label = Objects.requireNonNull(label);
        this.sender = Objects.requireNonNull(sender);
        this.args = Objects.requireNonNull(args);
        this.plugin = Objects.requireNonNull(plugin);
        this.usage = Objects.requireNonNull(usage);
    }

    public SentCommand(@NotNull ApiPlugin plugin, @NotNull Sender sender, @NotNull String[] args, @NotNull String label, @NotNull String usage) {
        this.usage = Objects.requireNonNull(usage);
        List<CommandArg> argList = new ArrayList<>();
        for(int i=0;i< args.length;i++) {
            String arg = args[i];
            argList.add(new CommandArg(plugin,arg,i));
        }
        this.args = Collections.unmodifiableList(argList);
        this.label = Objects.requireNonNull(label);
        this.sender = Objects.requireNonNull(sender);
        this.plugin = Objects.requireNonNull(plugin);
    }

    /**
     * The label is what the user typed to activate this command. For example
     * if the user typed /testcommand a b c the label would be 'testcommand'
     *
     * @return the sent command label
     */
    @NotNull
    public String getLabel() {
        return this.label;
    }

    /**
     * The sender is the person who activated this command.
     *
     * @return The command's sender
     */
    @NotNull
    public Sender getSender() {
        return this.sender;
    }

    /**
     * Returns the raw unwrapped string form of a command argument as it was sent by the user.
     *
     * @param index the positional index of the argument
     * @return the raw unwrapped string of the argument
     * @throws CommandException if the argument was not sent by the user
     */
    public String getRawArg(int index) throws CommandException {
        if(!argExists(index))
            throw new CommandException(MessageErrors.BAD_USAGE().toString(usage));

        return this.args.get(index).asString();
    }

    /**
     * Returns a command argument representation of the argument sent by the user. If the argument
     * doesn't exist then the bad usage message will be sent to the user detailing the correct
     * usage.
     *
     * @param index the positional index of the argument
     * @return a command argument representation of the argument sent by the user
     * @throws CommandException if the argument was not sent by the user
     */
    public CommandArg getArgOrFail(int index) throws CommandException {
        if(!argExists(index))
            throw new CommandException(MessageErrors.BAD_USAGE().toString(usage));

        return this.args.get(index);
    }

    /**
     * Returns a command argument representation of the argument sent by the user. If this fails
     * then the message provided will be sent to the user. If the argument
     * doesn't exist then the bad usage message will be sent to the user detailing the correct
     * usage.
     *
     * @param index the positional index of the argument
     * @param message a verbose message to be sent to the user if this argument doesn't exist
     * @return a command argument representation of the argument sent by the user
     * @throws CommandException if the argument was not sent by the user
     */
    public CommandArg getArgOrFail(int index, String message) throws CommandException {
        if(!argExists(index))
            throw new CommandException(message);

        return this.args.get(index);
    }

    /**
     * Returns a command argument representation of the argument sent by the user. If the argument does not
     * exist then this method will simply return a wrapped command arg with the default string.
     *
     * @param index the positional index of the argument
     * @return a command argument representation of the argument sent by the user or the default
     */
    @NotNull
    public CommandArg getArgOrDefault(int index, @NotNull String def) {
        Objects.requireNonNull(def);
        if(!argExists(index))
            return new CommandArg(plugin,def,index);

        return args.get(index);
    }
    /**
     * Returns a command argument representation of the argument sent by the user. If the argument does not
     * exist then this method will return a command arg with no contents. That is a command arg with the string
     * ""
     *
     * @param index the positional index of the argument
     * @return a command argument representation of the argument sent by the user or the default
     */

    @NotNull
    public CommandArg getArgOrBlank(int index) {
        return getArgOrDefault(index,"");
    }

    /**
     * Returns a command argument representation of the argument sent by the user. If the argument does not
     * exist then this method will return null.
     *
     * @param index the positional index of the argument
     * @return a command argument representation of the argument sent by the user or null
     */
    @Nullable
    public CommandArg getArgOrNull(int index) {
        if (!argExists(index))
            return null;

        return args.get(index);
    }

    /**
     * Retrieves an immutable list containing the raw string form of all of the arguments sent. This is essentially a list
     * of all the arguments typed by the user.
     *
     * @return a raw string list of all of the arguments sent by the user.
     */
    public List<String> getRawArgs() {
        List<String> args = new ArrayList<>();
        for(CommandArg arg : this.args) {
            args.add(arg.asString());
        }
        return Collections.unmodifiableList(args);
    }

    /**
     * Returns an immutable list containing all of the command arguments sent by the user.
     *
     * @return A list containing all of the args sent by the user
     */
    public List<CommandArg> getArgs() {
        return Collections.unmodifiableList(args);
    }

    /**
     *
     * @return the amount of arguments sent by the user.
     */
    public int argCount() {
        return this.args.size();
    }

    /**
     * Checks whether the argument in the position index was sent by the user.
     *
     * @param index the positional index to check
     * @return whether the user sent an argument in that index
     */
    public boolean hasArg(int index) {
        return argExists(index);
    }

    private boolean argExists(int index) {
        return index < this.args.size() && index >= 0;
    }

    /**
     * Takes 'this' sent command. Then returns a new cloned SentCommand with the first argument in the list removed. If this command
     * does not have a first argument, then this returns a SentCommand with no arguments. As a result all the following arguments
     * would have their positional index decreased by 1
     *
     * For example 'label a b c' would become 'label b c'
     *
     * @param usage The new usage of how this command should be correctly sent
     * @return A cloned sentcommand with the first argument removed.
     *
     */
    public SentCommand reduce(String usage) {
        if(hasArg(0)) {
            List<CommandArg> newList = new ArrayList<>(args);
            newList.remove(0);
            return new SentCommand(plugin,sender,Collections.unmodifiableList(newList),label,usage);
        } else {
            return new SentCommand(plugin,sender,Collections.emptyList(),label,usage);
        }
    }

    @NotNull
    public EPlugin getPlugin() {
        return plugin;
    }

    @NotNull
    @Override
    public Iterator<CommandArg> iterator() {
        return this.args.iterator();
    }

    @Override
    public void forEach(Consumer<? super CommandArg> action) {
        Iterable.super.forEach(action);
    }

}
