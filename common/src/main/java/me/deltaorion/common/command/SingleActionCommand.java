package me.deltaorion.common.command;

import me.deltaorion.common.command.sent.MessageErrors;
import me.deltaorion.common.command.sent.SentCommand;
import me.deltaorion.common.locale.message.Message;
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
    public Message getDescription() {
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
            command.getSender().sendMessage(MessageErrors.INTERNAL_ERROR_COMMAND().toString(command.getLabel(),command.getRawArgs()));
            command.getPlugin().getPluginLogger().severe(MessageErrors.INTERNAL_ERROR_COMMAND().toString(command.getLabel(),command.getRawArgs()),e);
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
            command.getPlugin().getPluginLogger().severe(MessageErrors.INTERNAL_ERROR_TAB_COMPLETION().toString(command),e);
            command.getSender().sendMessage(MessageErrors.INTERNAL_ERROR_TAB_COMPLETION().toString(command,e));
            return Collections.emptyList();
        }
    }

    protected List<String> getTabCompletions(SentCommand command) throws CommandException {
        return Collections.emptyList();
    }
}
