package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public class BungeePlugin extends Plugin implements EPlugin {

   private final WrapperManager manager = new WrapperManager(this) {
       @Override
       protected EPlugin getWrapper() {
           return BungeePluginWrapper.of(BungeePlugin.this);
       }
   };

    public BungeePlugin() {
        super();
    }

    @NotNull @Override
    public EServer getEServer() {
        return manager.getEServer();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        manager.onEnable();
    }

    public void onDisable() {
        super.onDisable();
        manager.onDisable();
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
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        manager.saveResource(resourcePath,replace);
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

}
