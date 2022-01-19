package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class BukkitPlugin extends JavaPlugin implements EPlugin {


    public BukkitPlugin() {
        super();
    }

    private final WrapperManager manager = new WrapperManager(this) {
        @Override
        protected EPlugin getWrapper() {
            return BukkitPluginWrapper.of(BukkitPlugin.this);
        }
    };

    @Override
    public void onEnable() {
        super.onEnable();
        manager.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        manager.onDisable();
    }


    @NotNull
    @Override
    public EServer getEServer() {
        return manager.getEServer();
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        return manager.getScheduler();
    }

    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        return manager.wrapSender(commandSender);
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        return manager.getDataDirectory();
    }

    @Override @Nullable
    public InputStream getResourceStream(@NotNull String path) {
        return manager.getResourceStream(path);
    }

    @Override
    public PluginLogger getPluginLogger() {
        return manager.getPluginLogger();
    }

    @Override
    public boolean isPluginEnabled() {
        return manager.isPluginEnabled();
    }

    @Override
    public void disablePlugin() {
        manager.disablePlugin();
    }

    @Override
    public PluginTranslator getTranslator() {
        return manager.getTranslator();
    }

    @Override
    public void onPluginDisable() { }

    @Override
    public void onPluginEnable() { }

    @Override
    public void registerDependency(String name, boolean required) {
        manager.registerDependency(name,required);
    }


    @Override
    public Dependency getDependency(String name) {
        return manager.getDependency(name);
    }

    @Override
    public boolean hasDependency(String name) {
        return manager.hasDependency(name);
    }

    @Override
    public Set<String> getDependencies() {
        return manager.getDependencies();
    }

    @Override
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser) {
        manager.registerParser(clazz,parser);
    }

    @NotNull
    @Override
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz) {
        return manager.getParser(clazz);
    }

    @Override
    public <T> void clearParsers(@NotNull Class<T> clazz) {
        manager.clearParsers(clazz);
    }
}
