package me.deltaorion.common.config.properties;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;

public class PropertiesOptions implements ConfigOptions {

    @NotNull private final MemoryConfig root;
    private boolean copyDefaults;

    public PropertiesOptions(@NotNull MemoryConfig root) {
        this.root = root;
        this.copyDefaults = false;
    }

    @Override
    public MemoryConfig configuration() {
        return null;
    }

    @Override
    public char pathSeparator() {
        throw new UnsupportedOperationException(".properties files do NOT supported a nested structure");
    }

    @Override
    public ConfigOptions pathSeparator(char value) {
        throw new UnsupportedOperationException(".properties files do NOT supported a nested structure");
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
                .add("copy-defaults",copyDefaults).toString();
    }
}
