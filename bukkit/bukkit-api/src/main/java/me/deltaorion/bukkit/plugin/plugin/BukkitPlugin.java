package me.deltaorion.bukkit.plugin.plugin;

import me.deltaorion.bukkit.command.AsyncBukkitCommand;
import me.deltaorion.bukkit.command.BukkitCommandMap;
import me.deltaorion.bukkit.command.SyncBukkitCommand;
import me.deltaorion.bukkit.command.sent.ArgumentParsersBukkit;
import me.deltaorion.bukkit.display.bukkit.BukkitPlayerManager;
import me.deltaorion.bukkit.display.bukkit.SimpleBukkitPlayerManager;
import me.deltaorion.bukkit.display.bukkit.WrappedPlayerManager;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.custom.CustomItemManager;
import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.Command;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.locale.IChatColor;
import me.deltaorion.common.locale.translator.TranslationManager;
import me.deltaorion.common.plugin.ApiPlugin;
import me.deltaorion.common.plugin.EServer;
import me.deltaorion.common.plugin.SharedApiPlugin;
import me.deltaorion.common.plugin.depend.Dependency;
import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.scheduler.ErrorReportingThreadPool;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
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

public abstract class BukkitPlugin extends JavaPlugin implements ApiPlugin {

    @NotNull private final SharedApiPlugin plugin;
    @NotNull private final BukkitCommandMap commandMap;
    @Nullable private CustomItemManager itemManager;
    @NotNull private final ExecutorService commandService;
    @Nullable private SimpleBukkitPlayerManager bukkitPlayerManager;

    private final String MATERIAL_LOCATION = "me.deltaorion.extapi/material.properties";

    public BukkitPlugin() {
        this.plugin = new SharedApiPlugin(new BukkitPluginWrapper(this));
        this.commandMap = new BukkitCommandMap(this);
        this.commandService = new ErrorReportingThreadPool(0,Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        registerDefaultParsers();
        allowDuplicates();
    }


    private void initialiseEMaterial() {
        if(EMaterial.isInitialised())
            return;

        try {
            EMaterial.initialise(getEServer().getServerVersion(),
                    Objects.requireNonNull(
                            getClassLoader().getResourceAsStream(MATERIAL_LOCATION),
                    "Could not find material data stream at '" + MATERIAL_LOCATION + "'")
            );
        } catch (Throwable e) {
            getPluginLogger().severe("An error occurred while initialising EMaterial. Are you reloading the server?",e);
            throw e;
        }
    }

    private void registerDefaultDepends() {
        plugin.registerDependency(BukkitAPIDepends.NBTAPI, false);
        plugin.registerDependency(BukkitAPIDepends.PROTOCOL_LIB, false);
    }

    private void registerDefaultParsers() {
        plugin.registerParser(Player.class, ArgumentParsersBukkit.BUKKIT_PLAYER_PARSER(this));
        plugin.registerParser(Material.class, ArgumentParsersBukkit.MATERIAL_PARSER());
        plugin.registerParser(EMaterial.class,ArgumentParsersBukkit.EMATERIAL_PARSER());
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
        this.bukkitPlayerManager = new SimpleBukkitPlayerManager(this);
        initialiseEMaterial();
        initialiseChatColors();
        initialiseEMaterialNBT();
    }

    private void initialiseEMaterialNBT() {
        try {
            if (plugin.getDependency(BukkitAPIDepends.NBTAPI).isActive()) {
                plugin.getPluginLogger().info("Initialising EMaterial NBT support");
                EMaterial.initialiseNBT();
            } else {
                EMaterial.assertNoNBTAPI();
            }
        } catch (Throwable e) {
            plugin.getPluginLogger().severe("An error occurred when initialising EMaterial NBT check",e);
        }
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
        Objects.requireNonNull(bukkitPlayerManager,"Plugin is enabled and thus the player manager should not be null").shutdown();
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
    public TranslationManager getTranslator() {
        return plugin.getTranslator();
    }

    @Override
    public void setTranslator(TranslationManager manager) {
        plugin.setTranslator(manager);
    }

    @Override
    public void onPluginDisable() { }

    @Override
    public void onPluginEnable() {

    }

    @Override
    public void cacheRunning(RunningAnimation<?> animation) {
        this.plugin.cacheRunning(animation);
    }

    @NotNull
    @Override
    public Collection<RunningAnimation<?>> getCachedRunning() {
        return plugin.getCachedRunning();
    }

    @Override
    public void removeCachedRunning(RunningAnimation<?> animation) {
        plugin.removeCachedRunning(animation);
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

    @NotNull
    public BukkitPlayerManager getBukkitPlayerManager() {
        if(this.bukkitPlayerManager==null)
            throw new IllegalStateException("Cannot Access this API method before the plugin has enabled. Are you overriding onEnable instead of onPluginEnable?");
        return new WrappedPlayerManager(bukkitPlayerManager);
    }

    private void initialiseChatColors() {
        if(IChatColor.isInitialised())
            return;

        for(ChatColor color : ChatColor.values())  {
            IChatColor c = IChatColor.valueOf(color.name());
            c.initialise(color.getChar(),color.isFormat(),color.toString());
        }

        IChatColor.initialise(
                ChatColor::translateAlternateColorCodes,
                ChatColor::stripColor
        );
    }

    private void allowDuplicates() {
    }

}
