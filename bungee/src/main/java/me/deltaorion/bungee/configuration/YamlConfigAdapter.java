package me.deltaorion.bungee.configuration;

import com.google.common.base.Splitter;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.memory.MemoryConfigSection;
import me.deltaorion.common.config.value.ConfigObjectValue;
import me.deltaorion.common.config.value.ConfigValue;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class YamlConfigAdapter implements ConfigAdapter {

    @NotNull private Configuration configuration;

    public YamlConfigAdapter() {
        this.configuration = new Configuration();
    }

    public YamlConfigAdapter(@NotNull Configuration configuration) {
        this.configuration = configuration;
    }


    @NotNull
    @Override
    public Set<String> getKeys() {
        return Collections.unmodifiableSet(new HashSet<>(configuration.getKeys()));
    }

    @NotNull
    @Override
    public ConfigValue getValue(ConfigSection parent, @NotNull MemoryConfig root, @NotNull String path) {
        Object value = configuration.get(path);
        if(value instanceof Configuration) {
            String absPath = getAbsPath(parent,path,root.options().pathSeparator());
            return new ConfigObjectValue(path,new MemoryConfigSection(new YamlConfigAdapter((Configuration) value),absPath,parent,root));
        } else {
            return new ConfigObjectValue(path,value);
        }
    }

    @Override
    public void set(@NotNull String path, Object value) {
        configuration.set(path,value);
    }

    @Override
    public void remove(@NotNull String path) {
        configuration.set(path,null);
    }

    @NotNull
    @Override
    public ConfigSection createSection(MemoryConfig root, ConfigSection parent, String path) {
        return createSection(root,parent,path,Collections.emptyMap());
    }

    @NotNull
    @Override
    public ConfigSection createSection(MemoryConfig root, ConfigSection parent , String path, Map<String, Object> map) {
        if(path.equals(""))
            throw new IllegalArgumentException("Cannot set this section");

        List<String> split = Splitter.on(root.options().pathSeparator()).splitToList(path);
        ConfigSection section = parent;
        for (int i=0;i<split.size();i++) {
            String pathSect = split.get(i);
            if(i==split.size()-1) {
                Configuration next = new Configuration();
                for(Map.Entry<String,Object> entry : map.entrySet()) {
                    next.set(entry.getKey(),entry.getValue());
                }
                section.set(pathSect,next);
                section = Objects.requireNonNull(section.getConfigurationSection(pathSect));
            } else {
                if (!section.isConfigurationSection(pathSect)) {
                    Configuration next = new Configuration();
                    section.set(pathSect, next);
                }
                section = Objects.requireNonNull(section.getConfigurationSection(pathSect));
            }
        }
        return section;
    }

    @Override
    public boolean supportsNesting() {
        return true;
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration,file);
    }

    @Override
    public void save(@NotNull Writer writer) throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration,writer);
    }

    @Override
    public void load(@NotNull Reader reader) throws IOException {
        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(reader);
    }
}
