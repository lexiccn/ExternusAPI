package me.deltaorion.common.config.properties;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.MemoryConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class PropertiesMemoryConfig extends PropertiesSection implements MemoryConfig {

    @Nullable private MemoryConfig def;
    @NotNull private final PropertiesOptions options;

    public PropertiesMemoryConfig(@NotNull Properties properties) {
        super(properties);
        this.def = null;
        this.options = new PropertiesOptions(this);
    }

    @Override
    public void addDefault(String path, Object value) {
        if(def==null) {
            def=new PropertiesMemoryConfig(new Properties());
        }
        def.set(path,value);
    }

    @Override
    public void addDefaults(Map<String, Object> defaults) {
        Objects.requireNonNull(defaults);

        for(Map.Entry<String,Object> entry : defaults.entrySet()) {
            addDefault(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void addDefaults(MemoryConfig defaults) {
        Objects.requireNonNull(defaults);
        addDefaults(defaults.getValues(true));
    }

    @Override
    public void setDefaults(MemoryConfig defaults) {
        this.def = defaults;
    }

    @Override
    public MemoryConfig getDefaults() {
        return def;
    }

    @Override
    public ConfigOptions options() {
        return options;
    }

    @Override
    public void mergeDefaults() {
        MemoryConfig defaults = def;
        if(defaults==null)
            return;

        boolean copyDef = this.options.copyDefaults();
        this.options.copyDefaults(false);
        for(String key : def.getKeys(true)) {
            if(!isSet(key)) {
                this.set(key,defaults.get(key));
            }
        }
        this.options.copyDefaults(copyDef);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("base",properties)
                .add("def",def).toString();
    }

    public boolean equals(Object o) {
        if(!(o instanceof PropertiesMemoryConfig))
            return false;

        return ((PropertiesMemoryConfig) o).properties.equals(this.properties) && Objects.equals(((PropertiesMemoryConfig) o).def, this.def);
    }
}
