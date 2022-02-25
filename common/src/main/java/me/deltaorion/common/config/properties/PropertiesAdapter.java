package me.deltaorion.common.config.properties;

import me.deltaorion.common.config.adapter.AdapterFactory;
import me.deltaorion.common.config.adapter.ConfigAdapter;

public class PropertiesAdapter implements AdapterFactory {

    @Override
    public ConfigAdapter getBlank() {
        return new PropertiesConfigAdapter();
    }
}
