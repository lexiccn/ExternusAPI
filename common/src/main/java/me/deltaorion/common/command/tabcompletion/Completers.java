package me.deltaorion.common.command.tabcompletion;

import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.server.EServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Completers {

    private Completers() {
        throw new UnsupportedOperationException();
    }

    public static CompletionSupplier INTEGERS(final int min, final int max) {
        return command -> {
            List<String> completions = new ArrayList<>();
            for(int i=min;i<=max;i++) {
                completions.add(String.valueOf(i));
            }
            return completions;
        };
    }

    public static CompletionSupplier FLOATS(final double min, final double max, final double delta, final int decimalPlaces) {

        if(delta<=0)
            throw new IllegalArgumentException("Delta must be a non-zero positive.");

        if(decimalPlaces>8 || decimalPlaces<=0)
            throw new IllegalArgumentException("Decimal places must be between 1 and 8");

        final String format = "%."+decimalPlaces+"f";

        return command -> {
            List<String> completions = new ArrayList<>();
            for(double i=min;i<=max;i+=delta) {
                completions.add(String.format(format,i));
            }
            return completions;
        };
    }

    public static CompletionSupplier BOOLEANS() {
        return command -> {
            List<String> completions = new ArrayList<>();
            completions.add("true");
            completions.add("false");
            return completions;
        };
    }

    public static <T extends Enum<T>> CompletionSupplier ENUMS(@NotNull Class<T> type) {
        return command -> {
            List<String> completions = new ArrayList<>();
            for (T c : type.getEnumConstants()) {
                completions.add(c.name());
            }
            return completions;
        };
    }

    public static CompletionSupplier ONLINE_SENDERS(EServer server) {
        return command -> {
            List<String> completion = new ArrayList<>();
            for(Sender sender : server.getOnlineSenders()) {
                completion.add(sender.getName());
            }
            return completion;
        };
    }
}
