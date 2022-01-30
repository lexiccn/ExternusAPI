package me.deltaorion.extapi.command.parser;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ParserRegistry {

    /**
     *
     *
     * @param clazz
     * @param parser
     * @param <T>
     */
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser);

    @NotNull
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz);

    public <T> void clearParsers(@NotNull Class<T> clazz);

}
