package me.deltaorion.common.command.sent;

import me.deltaorion.common.command.CommandException;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.util.DurationParser;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * A command argument is a data structure used to represent a string argument in a command. When a command
 * is sent it contains the label, that is what they typed to activate the command and several following arguments.
 *
 * For example, the command '/testcommand hello    world' would produce the args 'hello' and 'world'.
 *
 * This class provides several methods for manipulating and working with these args. This primarily involves parsing the string arg
 * into another data type such as an int.
 *
 * An argument consists of an index, or the position in which it sits in the list of arguments and a string value
 * which is the raw value of the typed arg.
 *
 */

@Immutable
public class CommandArg {

    @NotNull private final ApiPlugin plugin;
    @NotNull private final String arg;
    private final int index;

    public CommandArg(@NotNull ApiPlugin plugin, @NotNull String arg, int index) {

        Objects.requireNonNull(arg);
        Objects.requireNonNull(plugin);
        if(index<0)
            throw new ArrayIndexOutOfBoundsException("Command Arg index must be greater than 0");

        this.plugin = plugin;
        this.arg = arg;
        this.index = index;
    }

    public int asInt() throws CommandException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new CommandException(MessageErrors.NOT_INTEGER().toString(arg));
        }
    }

    public int asIntOrElse(@NotNull Function<String,Integer> orElse) {
        Objects.requireNonNull(orElse);
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return orElse.apply(arg);
        }
    }

    public int asIntOrDefault(int def) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public float asFloat() throws CommandException {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            throw new CommandException(MessageErrors.NOT_NUMBER().toString(arg));
        }
    }

    public float asFloatOrElse(@NotNull Function<String,Float> orElse) {
        Objects.requireNonNull(orElse);
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            return orElse.apply(arg);
        }
    }

    public float asFloatOrDefault(float def) {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public double asDouble() throws CommandException {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw new CommandException(MessageErrors.NOT_NUMBER().toString(arg));
        }
    }

    public double asDoubleOrElse(@NotNull Function<String,Double> orElse) {
        Objects.requireNonNull(orElse);
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            return orElse.apply(arg);
        }
    }

    public double asDoubleOrDefault(double def) {
        try {
            return Float.parseFloat(arg);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public boolean asBoolean() throws CommandException {
        try {
            return parseBoolean(arg);
        } catch (IllegalArgumentException e) {
            throw new CommandException(MessageErrors.NOT_BOOLEAN().toString(arg));
        }
    }

    public boolean asBooleanOrElse(@NotNull Function<String,Boolean> orElse) {
        Objects.requireNonNull(orElse);
        try {
            return parseBoolean(arg);
        } catch (IllegalArgumentException e) {
            return orElse.apply(arg);
        }
    }

    public boolean asBooleanOrDefault(boolean def) {
        try {
            return parseBoolean(arg);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public <T extends Enum<T>> T asEnum(@NotNull Class<T> type, @NotNull String name) throws CommandException {
        Objects.requireNonNull(name);
        Objects.requireNonNull(type);
        try {
            return Enum.valueOf(type, arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommandException(MessageErrors.NOT_ENUM().toString(arg,name));
        }
    }

    public <T extends Enum<T>> T asEnumOrDefault(@NotNull Class<T> type, T def) {
        Objects.requireNonNull(type);
        try {
            return Enum.valueOf(type, arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public Duration asDuration() throws CommandException {
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            throw new CommandException(MessageErrors.NOT_DURATION().toString(arg));
        }
    }

    public Duration asDurationOrElse(@NotNull Function<String,Duration> orElse){
        Objects.requireNonNull(orElse);
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return orElse.apply(arg);
        }
    }

    public Duration asDurationOrDefault(Duration def) {
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    /**
     * Parses a command argument to a generic class. The {@link ArgumentParser} must be defined and registered in the
     * {@link me.deltaorion.common.command.parser.ParserRegistry} for this to work. If there is an error while
     * parsing the argument then this will throw a command exception
     *
     * @param to The class that should be parsed too
     * @param <T> The class type that will be returned
     * @return The parsed value
     * @throws CommandException if there was an error performing the parse
     */
    public <T> T parse(@NotNull Class<T> to) throws CommandException {
        return parseArg(Objects.requireNonNull(to));
    }

    /**
     * Parses a command argument to a generic class. The {@link ArgumentParser} must be defined and registered in the
     * {@link me.deltaorion.common.command.parser.ParserRegistry} for this to work. If there is an error while parsing
     * then this will return the default value instead
     *
     * @param to The class that should be parsed too
     * @param <T> The class type that will be returned
     * @param def The default value to be returned
     * @return The parsed value
     *
     */
    public <T> T parseOrDefault(@NotNull Class<T> to, T def) {
        try {
            return parseArg(Objects.requireNonNull(to));
        } catch (CommandException e) {
            return def;
        }
    }

    /**
     * Parses a command argument to a generic class. The {@link ArgumentParser} must be defined and registered in the
     * {@link me.deltaorion.common.command.parser.ParserRegistry} for this to work. If there is an error while parsing
     * then this will perform the default functionality defined in orElse instead.
     *
     * @param to The class that should be parsed too
     * @param <T> The class type that will be returned
     * @param orElse a function describing what to do if the parsing fails.
     * @return The parsed value
     *
     */
    public <T> T parseOrElse(@NotNull Class<T> to, @NotNull Function<String,T> orElse) {
        Objects.requireNonNull(orElse);
        try {
            return parseArg(Objects.requireNonNull(to));
        } catch (CommandException e) {
            return orElse.apply(arg);
        }
    }

    private <T> T parseArg(@NotNull Class<T> to) throws CommandException {
        CommandException recent = null;
        Collection<ArgumentParser<T>> parsers = plugin.getParser(to);
        for(ArgumentParser<T> parser : parsers) {
            try {
                T obj = parser.parse(arg);
                if(obj!=null)
                    return obj;

            } catch (CommandException e) {
                recent = e;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if(recent!=null)
            throw recent;

        throw new CommandException(MessageErrors.BAD_ARGUMENT().toString(arg));
    }

    public boolean matches(String string) {
        return this.arg.equalsIgnoreCase(string);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CommandArg))
            return false;

        CommandArg arg = (CommandArg) o;
        return (this.index == arg.index && this.arg.equals(arg.arg));
    }

    public String asString() {
        return this.arg;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return this.arg;
    }

    private boolean parseBoolean(@NotNull String arg) {
        if(arg.equalsIgnoreCase("yes") || arg.equalsIgnoreCase("true")) {
            return true;
        } else if(arg.equalsIgnoreCase("no") || arg.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException();
        }

    }
}
