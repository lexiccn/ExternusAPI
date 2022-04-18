package me.deltaorion.common.config.yaml;

import me.deltaorion.common.config.AdapterFactory;
import me.deltaorion.common.config.ConfigAdapter;
import me.deltaorion.common.config.ConfigSection;
import org.jetbrains.annotations.NotNull;

public class YamlAdapter implements AdapterFactory {
    @Override
    public ConfigAdapter getNew(@NotNull ConfigSection adapterFor) {
        return new YamlConfigAdapter(adapterFor);
    }

    @Override
    public String getFileExtension() {
        return "yml";
    }
}
