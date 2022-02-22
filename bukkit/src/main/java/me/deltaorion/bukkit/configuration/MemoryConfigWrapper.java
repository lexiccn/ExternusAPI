package me.deltaorion.bukkit.configuration;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.MemoryConfig;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MemoryConfigWrapper extends ConfigSectionWrapper implements MemoryConfig {

    @NotNull private final Configuration configuration;

    public MemoryConfigWrapper(@NotNull Configuration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    public void addDefault(String path, Object value) {
        this.configuration.addDefault(path,value);
    }

    @Override
    public void addDefaults(Map<String, Object> defaults) {
        configuration.addDefaults(defaults);
    }

    @Override
    public void addDefaults(MemoryConfig defaults) {
        configuration.addDefaults(defaults.getValues(true));
    }

    @Override
    public void setDefaults(MemoryConfig defaults) {
        if(defaults instanceof MemoryConfigWrapper) {
            configuration.setDefaults(((MemoryConfigWrapper) defaults).configuration);
        } else {
            addDefaults(defaults);
        }
    }

    @Override
    public MemoryConfig getDefaults() {
        return new MemoryConfigWrapper(configuration.getDefaults());
    }

    @Override
    public ConfigOptions options() {
        return new ConfigOptionsWrapper(configuration.options(),this);
    }

    @Override
    public boolean equals(Object o) {
        Configuration otherConfig = null;
        if(o instanceof MemoryConfigWrapper) {
            otherConfig = ((MemoryConfigWrapper) o).configuration;
        } else if(o instanceof FileConfigurationOptions) {
            otherConfig = (Configuration) o;
        } else {
            return false;
        }
        return Objects.equals(otherConfig,this.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wrapped",this.configuration)
                .toString();
    }

    @Override
    public void mergeDefaults() {
        MemoryConfig defaults = getDefaults();
        if(defaults==null)
            return;

        boolean copyDef = options().copyDefaults();
        options().copyDefaults(false);
        for(String key : defaults.getKeys(true)) {
            if(!isSet(key)) {
                this.set(key,defaults.get(key));
            }
        }
        options().copyDefaults(copyDef);
    }
}
