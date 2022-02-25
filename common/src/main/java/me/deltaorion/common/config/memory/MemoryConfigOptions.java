package me.deltaorion.common.config.memory;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

public class MemoryConfigOptions implements ConfigOptions {

    @NotNull private final MemoryConfig root;
    private boolean copyDefaults;
    private char pathSeparator;

    public MemoryConfigOptions(@NotNull MemoryConfig root) {
        this.root = root;
        this.copyDefaults = false;
        this.pathSeparator = '.';
    }

    @Override
    public MemoryConfig configuration() {
        return root;
    }

    @Override
    public char pathSeparator() {
        return pathSeparator;
    }

    @Override
    public ConfigOptions pathSeparator(char value) {
        this.pathSeparator = value;
        return this;
    }

    @Override
    public boolean copyDefaults() {
        return copyDefaults;
    }

    @Override
    public ConfigOptions copyDefaults(boolean value) {
        this.copyDefaults = value;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("copy-defaults",copyDefaults)
                .add("path-separator",pathSeparator)
                .toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof ConfigOptions))
            return false;

        ConfigOptions options = (ConfigOptions) o;
        return options.copyDefaults() == this.copyDefaults() && options.pathSeparator() == this.pathSeparator();
    }
}
