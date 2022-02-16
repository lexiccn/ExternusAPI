package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.common.sender.Sender;
import org.jetbrains.annotations.NotNull;

public interface ApiPlugin extends BaseApiPlugin {

    /**
     * Registers a command for the plugin with the given names. The name is the label of the command. For example if
     * the name is help, then /help will execute the command
     * Commands do not need to be included in the plugin description file.
     * The first name will be considered the name of the command and other entries will be considered aliases. The registered
     * command will run synchronously.
     *
     * @param command The generic command to be registered
     * @param names The name, followed by aliases for the command
     * @throws IllegalArgumentException if no name is specified.
     */

    public abstract void registerCommand(@NotNull Command command, @NotNull String... names);
}
