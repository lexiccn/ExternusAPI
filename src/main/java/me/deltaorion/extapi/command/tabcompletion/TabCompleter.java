package me.deltaorion.extapi.command.tabcompletion;

import me.deltaorion.extapi.command.sent.CommandArg;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TabCompleter {

    @NotNull
    public List<String> search(@NotNull List<String> words, @NotNull List<String> args);
}
