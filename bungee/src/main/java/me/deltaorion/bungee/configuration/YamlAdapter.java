package me.deltaorion.bungee.configuration;

import me.deltaorion.common.config.adapter.AdapterFactory;
import me.deltaorion.common.config.adapter.ConfigAdapter;

public class YamlAdapter implements AdapterFactory {
    @Override
    public ConfigAdapter getBlank() {
        return new YamlConfigAdapter();
    }
}
