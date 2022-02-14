package me.deltaorion.extapi.common.exception;

import me.deltaorion.extapi.item.EMaterial;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when an action can only be done on a higher version
 */
public class UnsupportedVersionException extends RuntimeException {
    public UnsupportedVersionException(String message) {
        super(message);
    }

    public UnsupportedVersionException(@NotNull EMaterial material) {
        super("Cannot create ItemStack as the material '" + material + "'; has not be released for this version");
    }
}
