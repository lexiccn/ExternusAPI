package me.deltaorion.extapi.command.parser;

import me.deltaorion.extapi.command.CommandException;

public interface ArgumentParser<T> {

    public T parse(String arg) throws CommandException;
}
