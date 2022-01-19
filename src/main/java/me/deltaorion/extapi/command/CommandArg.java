package me.deltaorion.extapi.command;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.util.DurationParser;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

@Immutable
public class CommandArg {

    @NotNull private final EPlugin plugin;
    @NotNull private final String arg;
    private final int index;

    public CommandArg(@NotNull EPlugin plugin, @NotNull String arg, int index) {

        Validate.notNull(arg);
        Validate.notNull(plugin);
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
            throw new CommandException(ArgumentErrors.NOT_INTEGER().toString(arg));
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
            throw new CommandException(ArgumentErrors.NOT_NUMBER().toString(arg));
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
            throw new CommandException(ArgumentErrors.NOT_NUMBER().toString(arg));
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
            throw new CommandException(ArgumentErrors.NOT_BOOLEAN().toString(arg));
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
            throw new CommandException(ArgumentErrors.NOT_ENUM().toString(arg,name));
        }
    }

    public <T extends Enum<T>> T asEnumOrDefault(@NotNull Class<T> type, T def) throws CommandException {
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
            throw new CommandException(ArgumentErrors.NOT_DURATION().toString(arg));
        }
    }

    public Duration asDurationOrElse(@NotNull Function<String,Duration> orElse) throws CommandException {
        Objects.requireNonNull(orElse);
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return orElse.apply(arg);
        }
    }

    public Duration asDurationOrDefault(Duration def) throws CommandException {
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public <T> T parse(@NotNull Class<T> to) throws CommandException {
        return parseArg(Objects.requireNonNull(to));
    }

    public <T> T parseOrDefault(@NotNull Class<T> to, T def) {
        try {
            return parseArg(Objects.requireNonNull(to));
        } catch (CommandException e) {
            return def;
        }
    }

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

        throw new CommandException(ArgumentErrors.BAD_ARGUMENT().toString(arg));
    }

    public boolean matches(String string) {
        return this.arg.equalsIgnoreCase(string);
    }
    public String asString() {
        return this.arg.toLowerCase();
    }

    public int getIndex() {
        return this.index;
    }

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
