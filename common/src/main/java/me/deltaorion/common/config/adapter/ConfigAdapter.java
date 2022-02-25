package me.deltaorion.common.config.adapter;

import me.deltaorion.common.config.ConfigSection;
import me.deltaorion.common.config.MemoryConfig;
import me.deltaorion.common.config.value.ConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

public interface ConfigAdapter {

    @NotNull
    Set<String> getKeys();

    @NotNull
    public ConfigValue getValue(@Nullable ConfigSection parent, @NotNull MemoryConfig root, @NotNull String path);

    void set(@NotNull String path, Object value);

    void remove(@NotNull String path);

    @NotNull
    ConfigSection createSection(MemoryConfig root, ConfigSection parent, String path);

    @NotNull
    ConfigSection createSection(MemoryConfig root, ConfigSection parent, String path, Map<String ,Object> map);

    boolean supportsNesting();

    void save(@NotNull File file) throws IOException;

    void save(@NotNull Writer writer) throws IOException;

    void load(@NotNull Reader reader) throws IOException;

    @NotNull
    default String getAbsPath(@Nullable ConfigSection parent, @NotNull String nextPath, char pathSeparator) {
        String parentPath = parent == null ? "" : parent.getCurrentPath();
        return parentPath.equals("") ? nextPath : parentPath + pathSeparator + nextPath;
    }
}
