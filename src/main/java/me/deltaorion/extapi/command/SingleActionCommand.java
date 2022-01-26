package me.deltaorion.extapi.command;

import me.deltaorion.extapi.command.sent.ArgumentErrors;
import me.deltaorion.extapi.command.sent.SentCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class SingleActionCommand implements Command {

    @NotNull
    @Override
    public String getPermission() {
        return NO_PERMISSION;
    }

    @Nullable
    @Override
    public String getDescription() {
        return null;
    }

    @NotNull @Override
    public String getUsage() {
        return NO_USAGE;
    }

    @NotNull
    public Map<String,Command> getFunctions() {
        return Collections.emptyMap();
    }

    @NotNull
    public Collection<String> getCommandArgs() {
        return Collections.emptyList();
    }

    @Override
    public void onCommand(SentCommand command) {
        try {
            commandLogic(command);
        } catch (CommandException e) {
            command.getSender().sendMessage(e.getMessage());
        } catch (Throwable e) {
            command.getPlugin().getPluginLogger().severe(ArgumentErrors.INTERNAL_ERROR().toString(),e);
        }
    }

    public abstract void commandLogic(SentCommand command) throws CommandException;

    @NotNull @Override
    public List<String> onTabCompletion(SentCommand command) {
        try {
            List<String> completions = getTabCompletions(command);
            if (completions == null) {
                return Collections.emptyList();
            } else {
                return Collections.unmodifiableList(completions);
            }
        } catch (CommandException e) {
            return Collections.emptyList();
        } catch (Throwable e) {
            command.getPlugin().getPluginLogger().severe(ArgumentErrors.INTERNAL_ERROR_TAB_COMPLETION().toString(),e);
            return Collections.emptyList();
        }
    }

    protected List<String> getTabCompletions(SentCommand command) throws CommandException {
        return Collections.emptyList();
    }
}
