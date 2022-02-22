package me.deltaorion.bukkit.configuration;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.config.ConfigOptions;
import me.deltaorion.common.config.FileConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Objects;

public class FileConfigurationWrapper extends MemoryConfigWrapper implements FileConfig {

    @NotNull private final FileConfiguration configuration;

    public FileConfigurationWrapper(@NotNull FileConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    public ConfigOptions options() {
        return new ConfigOptionsWrapper(configuration.options(),this);
    }

    @Override
    public void save(File file) throws IOException {
        configuration.save(file);
    }

    @Override
    public void save(Writer writer) throws IOException {
        throw new UnsupportedOperationException("Cannot save to a generic writer. This is Bukkit's fault!");
    }

    @Override
    public void load(Reader reader) throws IOException {
        try {
            configuration.load(reader);
        } catch (InvalidConfigurationException e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        FileConfiguration otherConfig = null;
        if(o instanceof FileConfigurationWrapper) {
            otherConfig = ((FileConfigurationWrapper) o).configuration;
        } else if(o instanceof FileConfiguration) {
            otherConfig = (FileConfiguration) o;
        } else {
            return false;
        }
        return Objects.equals(otherConfig,this.configuration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("wrapped",this.configuration)
                .toString();
    }

}
