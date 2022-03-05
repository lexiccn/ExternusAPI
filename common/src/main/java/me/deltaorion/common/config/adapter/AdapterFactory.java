package me.deltaorion.common.config.adapter;

import me.deltaorion.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;

/**
 * An adapter factory is a factory class that is used to create fresh instances of {@link ConfigAdapter}.
 *
 * Sub Classes include
 *   - {@link me.deltaorion.common.config.nested.yaml.YamlAdapter}
 *   - {@link me.deltaorion.common.config.properties.PropertiesAdapter}
 *
 */
public interface AdapterFactory {

    public ConfigAdapter getNew(@NotNull ConfigSection adapterFor);
}
