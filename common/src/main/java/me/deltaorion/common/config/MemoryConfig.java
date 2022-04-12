package me.deltaorion.common.config;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.options.FileConfigOptions;
import me.deltaorion.common.config.options.MemoryConfigOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class MemoryConfig extends ConfigSection {

    @Nullable private MemoryConfig def;
    @NotNull protected final FileConfigOptions options;
    @NotNull private final AdapterFactory factory;

    public MemoryConfig(@NotNull AdapterFactory factory) {
        super(factory);
        this.options = new FileConfigOptions(this);
        this.factory = factory;
    }

    /**
     * Sets the default value of the given path as provided.
     * <p>
     * If no source {@link FileConfig} was provided as a default
     * collection, then a new {@link ConfigSection} will be created to
     * hold the new default value.
     * <p>
     * If value is null, the value will be removed from the default
     * Configuration source.
     *
     * @param path Path of the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is null.
     */
    
    public void addDefault(@NotNull String path, Object value) {
        if(def==null) {
            def=new MemoryConfig(factory);
        }
        def.set(path,value);
    }

    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link FileConfig} was provided as a default
     * collection, then a new {@link ConfigSection} will be created to
     * hold the new default values.
     *
     * @param defaults A map of Path{@literal ->}Values to add to defaults.
     * @throws IllegalArgumentException Thrown if defaults is null.
     */
    
    public void addDefaults(@NotNull Map<String, Object> defaults) {
        Objects.requireNonNull(defaults);

        for(Map.Entry<String,Object> entry : defaults.entrySet()) {
            addDefault(entry.getKey(),entry.getValue());
        }
    }

    /**
     * Sets the default values of the given paths as provided.
     * <p>
     * If no source {@link ConfigLoader} was provided as a default
     * collection, then a new {@link FileConfig} will be created to
     * hold the new default value.
     * <p>
     * This method will not hold a reference to the specified Configuration,
     * nor will it automatically update if that Configuration ever changes. If
     * you require this, you should set the default source {@link #setDefaults(MemoryConfig)}
     *
     * @param defaults A configuration holding a list of defaults to copy.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
    
    public void addDefaults(@NotNull MemoryConfig defaults) {
        Objects.requireNonNull(defaults);
        addDefaults(defaults.getValues(true));
    }

    /**
     * Sets the source of all default values for this {@link ConfigLoader}.
     * <p>
     * If a previous source was set, or previous default values were defined,
     * then they will not be copied to the new source.
     *
     * @param defaults New source of default values for this configuration.
     * @throws IllegalArgumentException Thrown if defaults is null or this.
     */
    
    public void setDefaults(MemoryConfig defaults) {
        this.def = defaults;
    }

    /**
     * Gets the source {@link FileConfig} for this configuration.
     * <p>
     * If no configuration source was set, but default values were added, then
     * a {@link FileConfig} will be returned. If no source was set
     * and no defaults were set, then this method will return null.
     *
     * @return Configuration source for default values, or null if none exist.
     */
    
    public MemoryConfig getDefaults() {
        return def;
    }

    @NotNull
    
    public MemoryConfigOptions options() {
        return options;
    }

    /**
     * Takes any default values that are not set and directly copies them into the
     * regular values. If {@link #supportsCommentPreservation()} is true then all default comments will be merged.  
     */
    
    public void mergeDefaults() {
        MemoryConfig defaults = def;
        if(defaults==null)
            return;

        boolean copyDef = this.options.copyDefaults();
        this.options.copyDefaults(false);
        for(String key : def.getKeys(true)) {
            if(!isSet(key)) {
                this.set(key,defaults.get(key));
                if(this.supportsCommentPreservation() && defaults.supportsCommentPreservation()) {
                    this.setComments(key,defaults.getComments(key));
                    this.setInlineComments(key,defaults.getInlineComments(key));
                }
            }
        }
        this.options.copyDefaults(copyDef);
    }

    
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("base",adapter)
                .add("def",def).toString();
    }

    
    public boolean equals(Object o) {
        if(!(o instanceof MemoryConfig))
            return false;

        MemoryConfig config = (MemoryConfig) o;
        if(!config.options().equals(this.options()))
            return false;

        return config.getValues(true).equals(this.getValues(true));
    }

}
