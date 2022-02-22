package me.deltaorion.common.command;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.command.tabcompletion.CompletionSupplier;
import me.deltaorion.common.command.tabcompletion.StringTabCompleter;
import me.deltaorion.common.command.tabcompletion.TabCompleter;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.locale.message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * A functional command represents a command tree structure with functional command arguments and subcommands. A base command
 * is the root command used to run the whole thing. The base command then may or may not have functional command arguments. A
 * functional command argument links a string to a subcommand. These are registered using {@link #registerArgument(String, Command)}.
 * A functional command argument is checked on the first index only. That is index position 0.
 * If what the user typed matches(ignoring case) the functional command argument then that subcommands logic will be run.
 * That subcommand may have its own functional command arguments. If there are no matching functional commands then base
 * {@link CommandLogic} is run.
 *
 * During tab completion, the functional command arguments are automatically supplied. However additional tab completion might need to be supplied
 * in conjunction with the {@link CommandLogic}. This additional tab completion should be defined using {@link #registerCompleter(int, CompletionSupplier)}
 *
 * This class is mostly thread safe. All functions apart for {@link CommandLogic} and {@link CompletionSupplier} are thread safe.
 */
public abstract class FunctionalCommand implements  Command {
    //list of functional command arguments
    @NotNull private final ConcurrentMap<String,Command> commandArgs;
    @NotNull private final ConcurrentMap<Integer, CompletionSupplier> completers;
    @NotNull private final String permission;
    @NotNull private final String usage;
    @Nullable private final Message description;
    @NotNull private final TabCompleter completer;

    protected FunctionalCommand(String permission, String usage, @Nullable Message description) {
        this.commandArgs = new ConcurrentHashMap<>();
        this.completers = new ConcurrentHashMap<>();
        this.permission = Objects.requireNonNull(permission);
        this.usage = Objects.requireNonNull(usage);
        this.description = description;
        this.completer = new StringTabCompleter();
    }

    private FunctionalCommand(Builder builder) {
        this.permission = Objects.requireNonNull(builder.permission,Objects.requireNonNull(builder.permission,"Enter a permission to build the command or use 'assertNoPermission' to force no permission"));
        this.description = builder.description;
        this.usage = builder.usage;
        this.commandArgs = new ConcurrentHashMap<>();
        this.completers = new ConcurrentHashMap<>();
        this.completer = new StringTabCompleter();

        for(Map.Entry<String,Command> arg : builder.commandArgs.entrySet()) {
            registerArgument(arg.getKey(),arg.getValue());
        }

        for(Map.Entry<Integer, CompletionSupplier> entry : builder.completers.entrySet()) {
            registerCompleter(entry.getKey(),entry.getValue());
        }
    }

    protected FunctionalCommand(String permission, String usage) {
        this(permission,usage,null);
    }

    protected FunctionalCommand(String permission) {
        this(permission,NO_USAGE);
    }

    public abstract void commandLogic(SentCommand command) throws CommandException;

    /**
     * Registers a functional command argument. If the index 0 typed argument matches ignore case this 'arg' then the
     * defined 'command' will be run. The SentCommand is {@link SentCommand#reduce(String)} removing its first argument.
     * The reduced SentCommand is passed into the command.
     *
     * If a functional command argument has already been registered and you use this again, then this will overwrite the
     * previously defined arg.
     *
     * You can define more functional command arguments in the 'command'
     *
     * @param arg The arg to check against
     * @param command the command to run if that arg is typed.
     * @throws IllegalArgumentException if arg has a space.
     */
    protected void registerArgument(@NotNull String arg, @NotNull Command command) {
        Objects.requireNonNull(arg);
        if(arg.contains(" "))
            throw new IllegalArgumentException("Command Argument cannot contain spaces!");

        this.commandArgs.put(arg.toLowerCase(Locale.ROOT),Objects.requireNonNull(command));
    }

    protected void registerArgument(@NotNull String arg, @NotNull CommandLogic logic) {
        Objects.requireNonNull(arg);
        Objects.requireNonNull(logic);
        Command command = new FunctionalCommand(NO_PERMISSION) {
            @Override
            public void commandLogic(SentCommand command) throws CommandException {
                logic.onCommand(command);
            }
        };
        registerArgument(arg,command);
    }

    /**
     * Registers tab completion logic that should be run when this index is being typed. The CompletionSupplier will be called if
     * the user is not typing a subcommand(the base {@link #commandLogic(SentCommand)} would have been run given what they type)
     * and that the prefix, that is what they are currently typing is of the index defined.
     *
     * @param index The positional index of what they are typing that should be used to run this completer.
     * @param completer the tab completer to be run
     */
    protected void registerCompleter(int index, @NotNull CompletionSupplier completer) {
        if(index<=0)
            throw new IllegalArgumentException("Completion index must be positive and greater than 0");

        this.completers.put(index,completer);
    }

    @Override
    public final void onCommand(SentCommand command) {
        try {
            //check user has permission
            if(!hasPermission(this, command.getSender()))
                throw new CommandException(MessageErrors.NO_PERMISSION().toString(getPermission()));

            Command found = null;
            if(command.hasArg(0)) {
                //loop thorugh all the functional command arguments and see if the index 0 entry matches any of them
                for(Map.Entry<String,Command> commandEntry : commandArgs.entrySet()) {
                    if(commandEntry.getKey().equalsIgnoreCase(command.getRawArg(0))) {
                        if(hasPermission(commandEntry.getValue(),command.getSender())) {
                            //if a suitable command has been found break the loop. Otherwise the iterator is may lock
                            //the map reducing concurrency
                            found = commandEntry.getValue();
                            break;
                        } else {
                            throw new CommandException(MessageErrors.NO_PERMISSION().toString(commandEntry.getValue().getPermission()));
                        }
                    }
                }
            }
            //if they haven't typed a functional command run the base command logic, otherwise run the reduced command
            if(found==null) {
                commandLogic(command);
            } else {
                found.onCommand(command.reduce(found.getUsage()));
            }

        } catch (CommandException e) {
            command.getSender().sendMessage(e.getMessage());
        } catch (Throwable e) {
            command.getSender().sendMessage(MessageErrors.INTERNAL_ERROR_COMMAND().toString(command.getLabel(),command.getRawArgs()));
            command.getPlugin().getPluginLogger().severe("An error occurred when running the command '"+command.getLabel()+"' with args '"+command.getRawArgs()+"'",e);
        }
    }

    @NotNull
    public Map<String,Command> getFunctions() {
        return Collections.unmodifiableMap(commandArgs);
    }

    @NotNull
    public Collection<String> getCommandArgs() {
        return Collections.unmodifiableSet(commandArgs.keySet());
    }

    @NotNull @Override
    public final List<String> onTabCompletion(SentCommand command) {

        //no permission then return no completions, they cant use this command anyway
        if(!hasPermission(this,command.getSender()))
            return Collections.emptyList();

        try {
            //if they are typing a subcommand use the logic for that instead
            List<String> completions = new ArrayList<>();
            if (command.hasArg(1)) {
                Command found = null;
                for (Map.Entry<String, Command> commandEntry : commandArgs.entrySet()) {
                    if (commandEntry.getKey().equalsIgnoreCase(command.getRawArg(0))) {
                        if(hasPermission(commandEntry.getValue(),command.getSender())) {
                            found = commandEntry.getValue();
                            break;
                        }
                    }
                }
                //if they are typing a subcommand hand logic for that instead
                if(found!=null) {
                    completions.addAll(found.onTabCompletion(command.reduce(found.getUsage())));
                } else {
                    completions.addAll(getAdditionalCompletion(command));
                }
            } else {
                //if they have typed nothing then hand them everything
                for(Map.Entry<String,Command> commandEntry : commandArgs.entrySet()) {
                    if(hasPermission(commandEntry.getValue(),command.getSender()))
                        completions.add(commandEntry.getKey());
                }
                completions.addAll(getAdditionalCompletion(command));
            }

            return Collections.unmodifiableList(completer.search(completions,command.getRawArgs()));
        } catch (CommandException e) {
            return Collections.emptyList();
        } catch (Throwable e) {
            command.getSender().sendMessage(MessageErrors.INTERNAL_ERROR_TAB_COMPLETION().toString(command.getLabel(),command.getRawArgs()));
            command.getPlugin().getPluginLogger().severe("An error occurred when attempting to tab complete the command '"+command.getLabel()+"' with args '"+command.getRawArgs()+"'",e);
            return Collections.emptyList();
        }
    }

    private Collection<String> getAdditionalCompletion(SentCommand command) throws CommandException {
        CompletionSupplier completer = this.completers.get(command.argCount());
        if(completer==null)
            return Collections.emptyList();
        List<String> completions = completer.getCompletions(command);
        if(completions == null) {
            return Collections.emptyList();
        } else {
            return completions;
        }
    }

    private boolean hasPermission(Command command, Sender sender) {
        if(command.getPermission().equals(NO_PERMISSION))
            return true;

        return sender.hasPermission(command.getPermission());
    }

    @Nullable @Override
    public Message getDescription() {
        return description;
    }

    @NotNull @Override
    public String getPermission() {
        return permission;
    }

    @NotNull @Override
    public String getUsage() {
        return this.usage;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("permission",permission)
                .add("usage",usage)
                .add("description",description)
                .add("func_command_args",getCommandArgs())
                .add("tab_suppliers",completers.keySet())
                .add("completer",completer)
                .toString();
    }

    public static class Builder {
        @NotNull private final Map<String,Command> commandArgs;
        @NotNull private final Map<Integer, CompletionSupplier> completers;
        @Nullable private CommandLogic logic;
        @Nullable private String permission;
        @Nullable private Message description = null;
        @NotNull private String usage = NO_USAGE;

        public Builder() {
            this.commandArgs = new HashMap<>();
            this.completers = new HashMap<>();
        }

        public Builder onCommand(@NotNull CommandLogic logic) {
            this.logic = Objects.requireNonNull(logic);
            return this;
        }

        public Builder addArgument(@NotNull String arg, @NotNull Consumer<Builder> builderConsumer) {
            Objects.requireNonNull(builderConsumer);
            Objects.requireNonNull(arg);
            Builder builder = new Builder();
            builderConsumer.accept(builder);
            this.commandArgs.put(arg.toLowerCase(Locale.ROOT),builder.build());
            return this;
        }

        public Builder addTabCompleter(int index, @NotNull CompletionSupplier completer) {
            this.completers.put(index,Objects.requireNonNull(completer));
            return this;
        }

        public Builder setPermission(@NotNull String permission) {
            this.permission = Objects.requireNonNull(permission);
            return this;
        }

        public Builder assertNoPermission() {
            this.permission = NO_PERMISSION;
            return this;
        }

        public Builder setUsage(@NotNull String usage) {
            this.usage = usage;
            return this;
        }

        public Builder setDescription(@Nullable Message description) {
            this.description = description;
            return this;
        }

        public FunctionalCommand build() {
            return new FunctionalCommand(this) {

                @Override
                public void commandLogic(SentCommand command) throws CommandException {
                    if(logic!=null) {
                        logic.onCommand(command);
                    }
                }
            };
        }
    }

    public interface CommandLogic {
        void onCommand(SentCommand command) throws CommandException;
    }

}

