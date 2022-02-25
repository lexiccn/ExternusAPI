package me.deltaorion.common.config.memory;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.AdapterFactory;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class SimpleMemoryConfig extends MemoryConfigSection implements MemoryConfig {

    @Nullable private MemoryConfig def;
    @NotNull private final MemoryConfigOptions options;
    @NotNull private final AdapterFactory factory;

    public SimpleMemoryConfig(@NotNull AdapterFactory factory) {
        super(factory.getBlank());
        this.options = new MemoryConfigOptions(this);
        this.factory = factory;
    }

    @Override
    public void addDefault(String path, Object value) {
        if(def==null) {
            def=new SimpleMemoryConfig(factory);
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
                .add("base",adapter)
                .add("def",def).toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MemoryConfig))
            return false;

        MemoryConfig config = (MemoryConfig) o;
        if(!config.options().equals(this.options()))
            return false;

        return config.getValues(true).equals(this.getValues(true));
    }

}
