package me.deltaorion.extapi.command.sent;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Immutable
public class SentCommand {

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

    @NotNull
    public String getLabel() {
        return this.label;
    }

    @NotNull
    public Sender getSender() {
        return this.sender;
    }

    public String getRawArg(int index) throws CommandException {
        if(!argExists(index))
            throw new CommandException(ArgumentErrors.BAD_USAGE().toString(usage));

        return this.args.get(index).asString();
    }

    public CommandArg getArgOrFail(int index) throws CommandException {
        if(!argExists(index))
            throw new CommandException(ArgumentErrors.BAD_USAGE().toString(usage));

        return this.args.get(index);
    }

    public CommandArg getArgOrFail(int index, String message) throws CommandException {
        if(!argExists(index))
            throw new CommandException(message);

        return this.args.get(index);
    }

    @NotNull
    public CommandArg getArgOrDefault(int index, @NotNull String def) {
        Objects.requireNonNull(def);
        if(!argExists(index))
            return new CommandArg(plugin,def,index);

        return args.get(index);
    }

    @NotNull
    public CommandArg getArgOrBlank(int index) {
        return getArgOrDefault(index,"");
    }

    public List<String> getRawArgs() {
        List<String> args = new ArrayList<>();
        for(CommandArg arg : this.args) {
            args.add(arg.asString());
        }
        return Collections.unmodifiableList(args);
    }

    public Collection<CommandArg> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public int argCount() {
        return this.args.size();
    }

    public boolean hasArg(int index) {
        return argExists(index);
    }

    @Nullable
    public CommandArg getArgOrNull(int index) {
        if (!argExists(index))
            return null;

        return args.get(index);
    }

    private boolean argExists(int index) {
        return index < this.args.size() && index >= 0;
    }


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
}
