package me.deltaorion.common.config.memory;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.adapter.AdapterFactory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

public class MemoryFileConfig extends SimpleMemoryConfig implements FileConfig {
    private MemoryFileConfig(@NotNull AdapterFactory adapter) {
        super(adapter);
    }

    @Override
    public void save(File file) throws IOException {
        Objects.requireNonNull(file);
        adapter.save(file);
    }

    @Override
    public void save(@NotNull Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        adapter.save(writer);
    }

    @Override
    public void load(@NotNull Reader reader) throws IOException {
        Objects.requireNonNull(reader);
        adapter.load(reader);
    }

    @NotNull
    public static MemoryFileConfig loadConfiguration(@NotNull AdapterFactory adapter, @NotNull InputStream stream) throws IOException {
        MemoryFileConfig config = new MemoryFileConfig(adapter);
        config.load(new InputStreamReader(stream));
        return config;
    }

    @NotNull
    public static MemoryFileConfig loadConfiguration(@NotNull AdapterFactory adapter, @NotNull Reader reader) throws IOException {
        MemoryFileConfig config = new MemoryFileConfig(adapter);
        config.load(reader);
        return config;
    }

    @NotNull
    public static MemoryFileConfig loadConfiguration(@NotNull AdapterFactory adapter, @NotNull File file) throws IOException {
        MemoryFileConfig config = new MemoryFileConfig(adapter);
        config.load(new FileReader(file));
        return config;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("adapter",adapter)
                .add("values",getValues(true)).toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof FileConfig))
            return false;

        FileConfig config = (FileConfig) o;
        return config.getValues(true).equals(this.getValues(true));
    }
}