package me.deltaorion.extapi.command.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ParserRegistry {

    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser);

    @NotNull
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz);

    public <T> void clearParsers(@NotNull Class<T> clazz);

}
