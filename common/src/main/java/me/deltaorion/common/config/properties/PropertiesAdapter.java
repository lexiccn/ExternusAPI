package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.ConfigAdapter;
import org.jetbrains.annotations.NotNull;

public class PropertiesAdapter implements AdapterFactory {

    @Override
    public ConfigAdapter getNew(@NotNull ConfigSection adapterFor) {
        return new PropertiesConfigAdapter(adapterFor);
    }

    @Override
    public String getFileExtension() {
        return "properties";
    }
}
