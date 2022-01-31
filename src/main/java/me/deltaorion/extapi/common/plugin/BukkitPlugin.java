package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.ArgumentParsers;
import me.deltaorion.extapi.common.command.AsyncBukkitCommand;
import me.deltaorion.extapi.common.command.BukkitCommandMap;
import me.deltaorion.extapi.common.command.SyncBukkitCommand;
import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.sender.BukkitSenderInfo;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.sender.SimpleSender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.common.thread.ErrorReportingThreadPool;
import me.deltaorion.extapi.item.custom.CustomItemManager;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class BukkitPlugin extends JavaPlugin implements ApiPlugin {

    @NotNull private final SharedApiPlugin plugin;
    @NotNull private final BukkitCommandMap commandMap;
    @Nullable private CustomItemManager itemManager;
    @NotNull private final ExecutorService commandService;

    public BukkitPlugin() {
        super();
        this.plugin = new SharedApiPlugin(new BukkitPluginWrapper(this));
        this.commandMap = new BukkitCommandMap(this);
        this.commandService = new ErrorReportingThreadPool(0,Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        registerDefaultParsers();
    }

    private void registerDefaultDepends() {
        plugin.registerDependency("NBTAPI",false);
    }

    private void registerDefaultParsers() {
        plugin.registerParser(Player.class, ArgumentParsers.BUKKIT_PLAYER_PARSER(this));
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        plugin.onPluginEnable();
        onEnableLogic();
        this.onPluginEnable();
    }

    private void onEnableLogic() {
        registerDefaultDepends();
        this.itemManager = new CustomItemManager(this);
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        this.onPluginDisable();
        plugin.onPluginDisable();
        shutdown();
    }

    private void shutdown() {
        commandService.shutdown();
        try {
            getPluginLogger().info("Shutting down command service!");
            if(!commandService.awaitTermination(5, TimeUnit.SECONDS))
                commandService.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser) {
        plugin.registerParser(clazz,parser);
    }

    @NotNull
    @Override
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz) {
        return plugin.getParser(clazz);
    }

    @Override
    public <T> void clearParsers(@NotNull Class<T> clazz) {
        plugin.clearParsers(clazz);
    }

    @Override
    public void registerDependency(@NotNull String name, boolean required) {
        plugin.registerDependency(name,required);
    }

    @Nullable
    @Override
    public Dependency getDependency(@NotNull String name) {
        return plugin.getDependency(name);
    }

    @Override
    public boolean hasDependency(@NotNull String name) {
        return plugin.hasDependency(name);
    }

    @NotNull
    @Override
    public Set<String> getDependencies() {
        return plugin.getDependencies();
    }

    @NotNull
    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Must wrap a bukkit command sender");

        return new SimpleSender(new BukkitSenderInfo(plugin.getEServer(),getServer(), (CommandSender) commandSender));
    }

    /**
     * Registered a command to run asynchronously. When a command is called a new task is called from a cached thread pool
     * The command may be run concurrently or in parallel, if a command is called once it may be called again before the
     * original command has finished executing. Handling multiple instances of this command being run should be handled
     * by the command. The command must also be responsive to interruption.
     *
     * @param command The command to be run
     * @throws IllegalArgumentException No name is given
     */

    @Override
    public void registerCommand(@NotNull Command command, @NotNull String... names) {
        SyncBukkitCommand bukkitCommand = new SyncBukkitCommand(this,Objects.requireNonNull(command));
        registerBukkitCommand(bukkitCommand,names);
    }

    public void registerAsyncCommand(@NotNull Command command, @NotNull String... names) {
        AsyncBukkitCommand bukkitCommand = new AsyncBukkitCommand(this,command);
        registerBukkitCommand(bukkitCommand,names);
    }

    private void registerBukkitCommand(@NotNull CommandExecutor executor, @NotNull String... names) {
        if(Objects.requireNonNull(names).length==0)
            throw new IllegalArgumentException("Must give the command a valid name!");

        this.commandMap.registerCommand(this,executor,names);
    }

    /**
     * Run a command to run asynchronously. When a command is called a new task is called from a cached thread pool
     * The command may be run concurrently or in parallel, if a command is called once it may be called again before the
     * original command has finished executing. Handling multiple instances of this command being run should be handled
     * by the command. The command must also be responsive to interruption.
     *
     * @param command The command to be run
     * @return A future encapsulating the task
     */

    public Future<?> runCommandAsync(@NotNull Runnable command) {
        if(!this.commandService.isShutdown()) {
            return this.commandService.submit(Objects.requireNonNull(command));
        } else {
            return null;
        }
    }


    @Override
    public PluginTranslator getTranslator() {
        return plugin.getTranslator();
    }

    @Override
    public void onPluginDisable() { }

    @Override
    public void onPluginEnable() {

    }

    @NotNull
    @Override
    public EServer getEServer() {
        return plugin.getEServer();
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        return plugin.getScheduler();
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        return plugin.getDataDirectory();
    }

    @Nullable
    @Override
    public InputStream getResourceStream(@NotNull String path) {
        return plugin.getResourceStream(path);
    }

    @NotNull
    @Override
    public PluginLogger getPluginLogger() {
        return plugin.getPluginLogger();
    }

    @Override
    public boolean isPluginEnabled() {
        return plugin.isPluginEnabled();
    }

    @Override
    public void disablePlugin() {
        plugin.disablePlugin();
    }

    /**
     * Returns the custom item manager. This is a thread safe class which can be used to register custom items. Once
     * a custom item is made it should be registered here so that any custom event listeners a properly registered. A custom item
     * should only be registered here and not through any other means such as the plugin manager.
     *
     * @return the custom item manager.
     * @throws IllegalStateException if this is called before the plugin is enabled
     */

    @NotNull
    public CustomItemManager getCustomItemManager() {
        if(this.itemManager==null)
            throw new IllegalStateException("Cannot Access this API method before the plugin has enabled. Are you overriding onEnable instead of onPluginEnable?");

        return this.itemManager;
    }
}
