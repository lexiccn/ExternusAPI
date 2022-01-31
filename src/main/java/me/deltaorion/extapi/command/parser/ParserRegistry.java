package me.deltaorion.extapi.command.parser;

import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@ThreadSafe
public interface ParserRegistry {

    /**
     * Registers a {@link ArgumentParser} to the registry. The registry is contains a list of parsers for the class. Multiple
     * different parsers can be registered for the same class.
     *
     * @param clazz The class or type which the parser should parse to
     * @param parser The argument parser object
     * @param <T> The type which the parser should parse to
     */
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser);

    /**
     * Returns all the parsers that are registered for a given class
     *
     * @param clazz The class or type to retrieve the parsers from
     * @param <T> The type for which to retrieve the parsers
     * @return A collection of all of the argument parsers for this type
     */
    @NotNull
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz);

    /**
     * Removes all of the parsers registered for a given class
     */
    public <T> void clearParsers(@NotNull Class<T> clazz);

}
