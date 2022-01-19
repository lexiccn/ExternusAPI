package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public abstract class WrapperManager implements EPlugin {

    @Nullable private EPlugin wrapper;
    @NotNull private final EPlugin superPlugin;
    private final String ERR_MSG = "Attempting to access abstraction API methods before the plugin has been enabled. Are you" +
            "overriding onEnable? instead of onPluginEnable";

    public WrapperManager(@NotNull EPlugin superPlugin) {
        super();
        this.superPlugin = superPlugin;
    }

    public void onEnable() {
        wrapper = getWrapper();
        superPlugin.onPluginEnable();
    }

    public void onDisable() {
        superPlugin.onPluginDisable();
    }

    protected abstract EPlugin getWrapper();

    @NotNull
    @Override
    public EServer getEServer() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getEServer();
    }

    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);

        wrapper.saveResource(resourcePath,replace);
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getScheduler();
    }

    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.wrapSender(commandSender);
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getDataDirectory();
    }

    @Override @Nullable
    public InputStream getResourceStream(@NotNull String path) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getResourceStream(path);
    }

    @Override
    public PluginLogger getPluginLogger() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getPluginLogger();
    }

    @Override
    public boolean isPluginEnabled() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.isPluginEnabled();
    }

    @Override
    public void disablePlugin() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        wrapper.disablePlugin();
    }

    @Override
    public PluginTranslator getTranslator() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);

        return wrapper.getTranslator();
    }

    @Override
    public void onPluginDisable() { }

    @Override
    public void onPluginEnable() { }

    @Override
    public void registerDependency(String name, boolean required) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        wrapper.registerDependency(name,required);
    }


    @Override
    public Dependency getDependency(String name) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getDependency(name);
    }

    @Override
    public boolean hasDependency(String name) {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.hasDependency(name);
    }

    @Override
    public Set<String> getDependencies() {
        if(wrapper==null)
            throw new IllegalStateException(ERR_MSG);
        return wrapper.getDependencies();
    }
}
