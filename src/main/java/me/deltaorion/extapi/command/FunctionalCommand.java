package me.deltaorion.extapi.command;

import me.deltaorion.extapi.command.sent.ArgumentErrors;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.command.tabcompletion.CompletionSupplier;
import me.deltaorion.extapi.command.tabcompletion.StringTabCompleter;
import me.deltaorion.extapi.command.tabcompletion.TabCompleter;
import me.deltaorion.extapi.common.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public abstract class FunctionalCommand implements  Command {

    @NotNull private final ConcurrentMap<String,Command> commandArgs;
    @NotNull private final ConcurrentMap<Integer, CompletionSupplier> completers;
    @NotNull private final String permission;
    @NotNull private final String usage;
    @Nullable private final String description;
    @NotNull private final TabCompleter completer;

    protected FunctionalCommand(String permission, String usage, @Nullable String description) {
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

    protected void registerArgument(@NotNull String arg, @NotNull Command command) {
        Objects.requireNonNull(arg);
        if(arg.contains(" "))
            throw new IllegalArgumentException("Command Argument cannot contain spaces!");

        this.commandArgs.put(arg.toLowerCase(Locale.ROOT),Objects.requireNonNull(command));
    }

    protected void registerCompleter(int index, @NotNull CompletionSupplier completer) {
        if(index<=0)
            throw new IllegalArgumentException("Completion index must be positive and greater than 0");

        this.completers.put(index,completer);
    }

    @Override
    public void onCommand(SentCommand command) {
        try {
            if(!hasPermission(this, command.getSender()))
                throw new CommandException(ArgumentErrors.NO_PERMISSION().toString());

            Command found = null;
            if(command.hasArg(0)) {
                for(Map.Entry<String,Command> commandEntry : commandArgs.entrySet()) {
                    if(commandEntry.getKey().equalsIgnoreCase(command.getRawArg(0))) {
                        if(hasPermission(commandEntry.getValue(),command.getSender())) {
                            found = commandEntry.getValue();
                            break;
                        } else {
                            throw new CommandException(ArgumentErrors.NO_PERMISSION().toString(commandEntry.getValue().getPermission()));
                        }
                    }
                }
            }

            if(found==null) {
                commandLogic(command);
            } else {
                found.onCommand(command.reduce(found.getUsage()));
            }

        } catch (CommandException e) {
            command.getSender().sendMessage(e.getMessage());
        } catch (Throwable e) {
            command.getSender().sendMessage(ArgumentErrors.INTERNAL_ERROR().toString());
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
    public List<String> onTabCompletion(SentCommand command) {

        //no permission
        if(!hasPermission(this,command.getSender()))
            return Collections.emptyList();

        try {
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

                if(found!=null) {
                    completions.addAll(found.onTabCompletion(command.reduce(found.getUsage())));
                } else {
                    completions.addAll(getAdditionalCompletion(command));
                }
            } else {
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
            command.getPlugin().getPluginLogger().severe("An error occured when attempting to tab complete the command '"+command.getLabel()+"' with args '"+command.getRawArgs()+"'",e);
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
    public String getDescription() {
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

    public static class Builder {
        @NotNull private final Map<String,Command> commandArgs;
        @NotNull private final Map<Integer, CompletionSupplier> completers;
        @Nullable private CommandLogic logic;
        @Nullable private String permission;
        @Nullable private String description = null;
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

        public Builder setDescription(@Nullable String description) {
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

    public static interface CommandLogic {
        public void onCommand(SentCommand command) throws CommandException;
    }

}

