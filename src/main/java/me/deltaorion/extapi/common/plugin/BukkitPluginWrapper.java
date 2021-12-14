package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.depend.SimpleDependencyManager;
import me.deltaorion.extapi.common.entity.sender.BukkitSenderInfo;
import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.entity.sender.SimpleSender;
import me.deltaorion.extapi.common.logger.JavaPluginLogger;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.BukkitSchedulerAdapter;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;

public class BukkitPluginWrapper implements EPlugin {

    private final JavaPlugin wrapped;
    private final EServer eServer;
    private final PluginLogger pluginLogger;
    private final SchedulerAdapter schedulerAdapter;
    private final SimpleDependencyManager dependencies;
    private final PluginTranslator translator;

    public BukkitPluginWrapper(JavaPlugin wrapped) {
        this.wrapped = wrapped;
        if(this.wrapped!=null) {
            this.eServer = new BukkitServer(wrapped.getServer());
            pluginLogger = new JavaPluginLogger(wrapped.getLogger());
            schedulerAdapter = new BukkitSchedulerAdapter(wrapped);
            dependencies = new SimpleDependencyManager(this);
            translator = new PluginTranslator(this,"en.yml");
        } else {
            this.eServer = null;
            this.pluginLogger = null;
            schedulerAdapter = null;
            this.dependencies = null;
            translator = null;
        }
    }

    public BukkitPluginWrapper(Plugin plugin) {
        this(((JavaPlugin) plugin));
    }

    @Override
    public EServer getEServer() {
        return eServer;
    }

    @Override
    public SchedulerAdapter getScheduler() {
        return schedulerAdapter;
    }

    @Override
    public Sender wrapSender(Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Must wrap a bukkit command sender");

        return new SimpleSender(new BukkitSenderInfo(getEServer(), (CommandSender) commandSender));
    }

    @Override
    public Path getDataDirectory() {
        return wrapped.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public InputStream getResourceStream(String path) {
        return wrapped.getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public URL getResourceURL(String path) {
        return wrapped.getClass().getClassLoader().getResource(path);
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        wrapped.saveResource(resourcePath,replace);
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.pluginLogger;
    }

    @Override
    public boolean isPluginEnabled() {
        if(wrapped!=null) {
            return wrapped.isEnabled();
        } else {
            return false;
        }
    }

    @Override
    public Object getPluginObject() {
        return wrapped;
    }

    @Override
    public void disablePlugin() {
        this.wrapped.getServer().getPluginManager().disablePlugin(wrapped);
    }

    @Override
    public PluginTranslator getTranslator() {
        return translator;
    }

    @Override
    public void registerDependency(String name, boolean required) {
        dependencies.registerDependency(name,required);
    }


    @Override
    public Dependency getDependency(String name) {
        return dependencies.getDependency(name);
    }

    @Override
    public boolean hasDependency(String name) {
        return dependencies.hasDependency(name);
    }

    @Override
    public Set<String> getDependencies() {
        return dependencies.getDependencies();
    }

    public JavaPlugin getPlugin() {
        return wrapped;
    }
}
