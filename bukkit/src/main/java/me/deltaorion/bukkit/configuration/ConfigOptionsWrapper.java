package me.deltaorion.bukkit.configuration;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.FileConfig;
import me.deltaorion.common.config.MemoryConfig;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConfigOptionsWrapper implements ConfigOptions {

    @NotNull private final ConfigurationOptions options;
    @NotNull private final MemoryConfig configuration;

    public ConfigOptionsWrapper(@NotNull ConfigurationOptions options, @NotNull MemoryConfig configuration) {
        this.options = options;
        this.configuration = configuration;
    }

    @Override
    public MemoryConfig configuration() {
        return configuration;
    }

    @Override
    public char pathSeparator() {
        return options.pathSeparator();
    }

    @Override
    public ConfigOptions pathSeparator(char value) {
        options.pathSeparator(value);
        return this;
    }

    @Override
    public boolean copyDefaults() {
        return options.copyDefaults();
    }

    @Override
    public ConfigOptions copyDefaults(boolean value) {
        options.copyDefaults(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        ConfigurationOptions otherOptions = null;
        if(o instanceof ConfigOptionsWrapper) {
            otherOptions = ((ConfigOptionsWrapper) o).options;
        } else if(o instanceof MemoryConfigurationOptions) {
            otherOptions = (ConfigurationOptions) o;
        } else {
            return false;
        }
        return Objects.equals(otherOptions,this.options);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wrapped",this.options)
                .toString();
    }

}
