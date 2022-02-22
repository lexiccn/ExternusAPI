package me.deltaorion.common.command.parser;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ThreadSafe
public class SimpleParserRegistry implements ParserRegistry {
    @NotNull private final ConcurrentMap<TypeToken<?>, List<ArgumentParser<?>>> registry;

    public SimpleParserRegistry() {
        this.registry = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(parser);

        TypeToken<T> token = TypeToken.of(clazz);
        List<ArgumentParser<?>> parsers = registry.computeIfAbsent(token, t -> new CopyOnWriteArrayList<>());
        synchronized (parsers) {
            if (!parsers.contains(parser)) {
                parsers.add(parser);
            }
        }
    }

    @NotNull
    @Override
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz) {
        List<ArgumentParser<?>> parsers = registry.get(TypeToken.of(clazz));
        if (parsers == null)
            return Collections.emptyList();

        return (Collection) Collections.unmodifiableList(parsers);
    }

    @Override
    public <T> void clearParsers(@NotNull Class<T> clazz) {
        this.registry.remove(TypeToken.of(clazz));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Registry Map",registry).toString();
    }
}
