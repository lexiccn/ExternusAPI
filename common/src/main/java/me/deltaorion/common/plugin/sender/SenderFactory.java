package me.deltaorion.common.plugin.sender;

import me.deltaorion.common.plugin.version.MinecraftVersion;
import org.jetbrains.annotations.NotNull;

/**
 * A sender factory is an abstract interface which is used to create a new sender instance from a command sender. This class
 * must be Thread Safe. However Thread Safety should be achieved by ensuring that all methods are stateless.
 */
public interface SenderFactory {

    /**
     * Stateless creates a new sender instance.
     *
     * @param sender The original sender object
     * @param version The current version of the server
     * @return A new sender instance
     */
    @NotNull
    Sender get(@NotNull Object sender, @NotNull MinecraftVersion version);
}
