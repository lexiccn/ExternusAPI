package me.deltaorion.bungee.plugin.plugin;

import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.Command;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.bungee.command.BungeeCommand;
import me.deltaorion.common.plugin.depend.Dependency;
import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.plugin.plugin.SharedApiPlugin;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.server.EServer;
import me.deltaorion.common.locale.translator.PluginTranslator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class BungeePlugin extends Plugin implements ApiPlugin {

    private final SharedApiPlugin plugin;

    public BungeePlugin() {
        super();
        this.plugin = new SharedApiPlugin(new BungeePluginWrapper(this));
        initialiseChatColors();
    }

    @NotNull @Override
    public EServer getEServer() {
        return plugin.getEServer();
    }

    @Override
    public final void onEnable() {
        super.onEnable();
        plugin.onPluginEnable();
        this.onPluginEnable();
    }

    public final void onDisable() {
        super.onDisable();
        this.onPluginDisable();
        plugin.onPluginDisable();
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

    @Override @Nullable
    public InputStream getResourceStream(@NotNull String path) {
        return plugin.getResourceStream(path);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        plugin.saveResource(resourcePath,replace);
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

    @Override
    public PluginTranslator getTranslator() {
        return plugin.getTranslator();
    }

    @Override
    public void onPluginDisable() { }

    @Override
    public void onPluginEnable() { }

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

    @Override
    public void registerCommand(@NotNull Command command, @NotNull String... names) {
        if(names.length==0)
            throw new IllegalArgumentException("Command must have a valid name!");

        BungeeCommand bungeeCommand = new BungeeCommand(this,command,names);
        getProxy().getPluginManager().registerCommand(this,bungeeCommand);
    }

    @Override
    public void registerDependency(@NotNull String name, boolean required) {
        plugin.registerDependency(name,required);
    }

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
        this.plugin.clearParsers(clazz);
    }

    private void initialiseChatColors() {
        //we arent given a way to initialise this properly
        me.deltaorion.common.locale.ChatColor.initialise(
                ChatColor::translateAlternateColorCodes,
                ChatColor::stripColor
        );
    }

}
