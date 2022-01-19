package me.deltaorion.extapi.command;

import jdk.nashorn.internal.ir.annotations.Immutable;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.util.DurationParser;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Function;

@Immutable
public class CommandArg {

    @NotNull private final EPlugin plugin;
    @NotNull private final String arg;
    private final int index;

    public CommandArg(@NotNull EPlugin plugin, @NotNull String arg, int index) {

        Validate.notNull(arg);
        Validate.notNull(plugin);

        this.plugin = plugin;
        this.arg = arg;
        this.index = index;
    }

    public int asInt() throws CommandException {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException();
        }
    }

    public int asIntOrElse(Function<String,Integer> orElse) {
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
            throw new UnsupportedOperationException();
        }
    }

    public float asFloatOrElse(Function<String,Float> orElse) {
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
            throw new UnsupportedOperationException();
        }
    }

    public double asDoubleOrElse(Function<String,Double> orElse) {
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
            throw new UnsupportedOperationException();
        }
    }

    public boolean asBooleanOrElse(Function<String,Boolean> orElse) {
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

    public <T extends Enum<T>> T asEnum(Class<T> type, String name) throws CommandException {
        try {
            return Enum.valueOf(type, arg.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException();
        }
    }

    public <T extends Enum<T>> T asEnumOrDefault(Class<T> type, T def) throws CommandException {
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
            throw new UnsupportedOperationException();
        }
    }

    public Duration asDurationOrElse(Function<String,Duration> orElse) throws CommandException {
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return orElse.apply(arg);
        }
    }

    public Duration asDuration(Duration def) throws CommandException {
        try {
            return DurationParser.parseDuration(arg);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public <T> T parse(Class<T> to) throws CommandException {
        throw new UnsupportedOperationException();
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

    private boolean parseBoolean(String arg) {
        if(arg.equalsIgnoreCase("yes") || arg.equalsIgnoreCase("true")) {
            return true;
        } else if(arg.equalsIgnoreCase("no") || arg.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
