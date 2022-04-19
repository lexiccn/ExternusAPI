package me.deltaorion.common.command.parser;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.plugin.sender.Sender;

/**
 * An argument parser is a functional interface which takes a string, and transforms it into another Object of the targeted
 * class. This allows a string to represent some object for user input.
 *
 * For example an integer parser would take a string in the form "123" and make it into the integer 123
 *
 * @param <T> The type which the parser transforms the string to
 */
public interface ArgumentParser<T> {

    /**
     * Takes a String and turns it into an equivalent object of type T. If the parse fails this will
     * throw an exception
     *
     * @param arg The string which to transform
     * @return An object representation of that string
     * @throws CommandException if the parse failed, usually due to the string being in the wrong format.
     */
    public T parse(Sender sender, String arg) throws CommandException;
}
