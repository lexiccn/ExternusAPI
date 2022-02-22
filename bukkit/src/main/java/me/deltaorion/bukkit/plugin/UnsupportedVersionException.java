package me.deltaorion.bukkit.plugin;

import me.deltaorion.bukkit.item.EMaterial;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an given action cannot be done on this bukkit version
 */
public class UnsupportedVersionException extends RuntimeException {
    public UnsupportedVersionException(String message) {
        super(message);
    }

    public UnsupportedVersionException(@NotNull EMaterial material) {
        super("Cannot create ItemStack as the material '" + material + "'; has not be released for this version");
    }
}
