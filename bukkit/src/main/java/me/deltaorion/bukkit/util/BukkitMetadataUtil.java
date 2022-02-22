package me.deltaorion.bukkit.util;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class BukkitMetadataUtil {

    @Nullable
    private static <R extends Metadatable> MetadataValue getValueOrNull(@Nonnull R entity,@Nonnull String tag) {
        List<MetadataValue> metadataValueList = entity.getMetadata(tag);

        if(metadataValueList == null) {
            return null;
        }

        if(metadataValueList.isEmpty()) {
            return null;
        }
        return metadataValueList.get(0);
    }

    @Nullable
    public static <R extends Metadatable> Object getObjectOrDef(@Nonnull R entity,@Nonnull String tag, Object def) {
        MetadataValue value = getValueOrNull(entity,tag);

        if(value==null) {
            return def;
        }

        return value.value();
    }

    public static <R extends Metadatable> int fetchIntOrDef(@Nonnull R entity,@Nonnull String tag, int def) {
        MetadataValue value = getValueOrNull(entity,tag);

        if(value==null) {
            return def;
        }

        return value.asInt();
    }

    public static <R extends Metadatable> double fetchDoubleOrDef(@Nonnull R entity,@Nonnull String tag, double def) {
        MetadataValue value = getValueOrNull(entity,tag);

        if(value==null) {
            return def;
        }

        return value.asDouble();
    }

    public static <R extends Metadatable> boolean fetchBooleanOrDef(@Nonnull R entity,@Nonnull String tag, boolean def) {
        MetadataValue value = getValueOrNull(entity,tag);

        if(value==null) {
            return def;
        }

        return value.asBoolean();
    }

    public static <R extends Metadatable> String fetchStringOrDef(@Nonnull R entity,@Nonnull String tag, String def) {
        MetadataValue value = getValueOrNull(entity,tag);

        if(value==null) {
            return def;
        }

        return value.asString();
    }

    public static <R extends Metadatable> void setMetadata(@NotNull R entity, @Nonnull Plugin plugin, @NotNull String tag, @NotNull Object value) {
        entity.setMetadata(tag,new FixedMetadataValue(plugin,value));
    }

    public static <R extends Metadatable> void setIfAbsent(@NotNull R entity, @Nonnull Plugin plugin, @NotNull String tag, @NotNull Object value) {
        if(entity.hasMetadata(tag))
            return;

        entity.setMetadata(tag,new FixedMetadataValue(plugin,value));
    }
}
