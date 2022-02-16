package me.deltaorion.extapi.item.inventory;

import me.deltaorion.extapi.common.version.MinecraftVersion;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class InventoryWrappers {

    private InventoryWrappers() {
        throw new UnsupportedOperationException();
    }

    public static InventoryWrapper FROM_VERSION(@NotNull MinecraftVersion version,@NotNull LivingEntity entity) {
        if(entity instanceof HumanEntity) {
            return HUMAN_FROM_VERSION((HumanEntity) entity,version);
        } else {
            return LIVING_FROM_VERSION(entity,version);
        }
    }

    private static InventoryWrapper HUMAN_FROM_VERSION(@NotNull HumanEntity entity, @NotNull MinecraftVersion version) {
        if(version.getMajor()>=10) {
            return new HumanEntityWrapper10(entity);
        } else {
            return new HumanEntityWrapper8_9(entity);
        }
    }

    private static InventoryWrapper LIVING_FROM_VERSION(@NotNull LivingEntity entity, @NotNull MinecraftVersion version) {
        if(version.getMajor()>=10) {
            return new LivingEntityWrapper10(entity);
        } else {
            return new LivingEntityWrapper8_9(entity);
        }
    }
}
