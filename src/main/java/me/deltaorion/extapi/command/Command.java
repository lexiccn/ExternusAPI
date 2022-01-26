package me.deltaorion.extapi.command;

import me.deltaorion.extapi.command.sent.SentCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Command {

    final static String NO_PERMISSION = "";
    final static String NO_USAGE = "";

    @NotNull
    public String getPermission();

    @Nullable
    public String getDescription();

    @NotNull
    public String getUsage();

    public void onCommand(SentCommand command);

    @NotNull
    public Map<String,Command> getFunctions();

    @NotNull
    public Collection<String> getCommandArgs();

    @NotNull
    public List<String> onTabCompletion(SentCommand command);
}
