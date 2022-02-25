package me.deltaorion.bukkit.configuration;

import com.google.common.base.Splitter;
import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.adapter.ConfigAdapter;
import me.deltaorion.common.config.memory.MemoryConfigSection;
import me.deltaorion.common.config.value.ConfigObjectValue;
import me.deltaorion.common.config.value.ConfigValue;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class YamlConfigAdapter implements ConfigAdapter {

    @NotNull private ConfigurationSection configuration;

    public YamlConfigAdapter() {
        this.configuration = new YamlConfiguration();
    }

    public YamlConfigAdapter(@NotNull ConfigurationSection section) {
        this.configuration = section;
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return configuration.getKeys(false);
    }

    @NotNull
    @Override
    public ConfigValue getValue(ConfigSection parent, @NotNull MemoryConfig root, @NotNull String path) {
        Object value = configuration.get(path);
        if(value instanceof ConfigurationSection) {
            String absPath = getAbsPath(parent,path,root.options().pathSeparator());
            return new ConfigObjectValue(path,new MemoryConfigSection(new YamlConfigAdapter((ConfigurationSection) value),absPath,parent,root));
        } else {
            return new ConfigObjectValue(path,configuration.get(path));
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
    public ConfigSection createSection(MemoryConfig root, ConfigSection parent, String path, Map<String, Object> map) {
        return new MemoryConfigSection(new YamlConfigAdapter(configuration.createSection(path,map)),path,parent,root);
    }

    @Override
    public boolean supportsNesting() {
        return true;
    }

    @Override
    public void save(@NotNull File file) throws IOException {
        if(!(configuration instanceof FileConfiguration))
            throw new UnsupportedOperationException("Cannot save an individual configuration section!");

        ((FileConfiguration) configuration).save(file);
    }

    @Override
    public void save(@NotNull Writer writer) throws IOException {
        if(!(configuration instanceof FileConfiguration))
            throw new UnsupportedOperationException("Cannot save an individual configuration section!");

        FileConfiguration fileConfig = (FileConfiguration) configuration;
        String str = fileConfig.saveToString();
        for(String split : Splitter.fixedLength(100).split(str)) {
            writer.write(split);
            writer.flush();
        }
        writer.close();
    }

    @Override
    public void load(@NotNull Reader reader) throws IOException {
        this.configuration = YamlConfiguration.loadConfiguration(reader);
    }
}
