package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.depend.SimpleDependencyManager;
import me.deltaorion.extapi.common.logger.JavaPluginLogger;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.BukkitSchedulerAdapter;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.sender.BukkitSenderInfo;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.SimpleSender;
import me.deltaorion.extapi.common.server.BukkitServer;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public class BukkitPluginWrapper implements EPlugin {

    @NotNull private final JavaPlugin wrapped;
    @NotNull private final EServer eServer;
    @NotNull private final PluginLogger pluginLogger;
    @NotNull private final SchedulerAdapter schedulerAdapter;
    private SimpleDependencyManager dependencies;
    private PluginTranslator translator;
    @NotNull private final Server server;

    private BukkitPluginWrapper(@NotNull JavaPlugin wrapped) {
        Validate.notNull(wrapped,"Wrapped plugin cannot be null!");
        this.wrapped = wrapped;
        this.eServer = new BukkitServer(wrapped.getServer());
        this.server = wrapped.getServer();
        pluginLogger = new JavaPluginLogger(wrapped.getLogger());
        schedulerAdapter = new BukkitSchedulerAdapter(wrapped);
    }

    private void init() {
        dependencies = new SimpleDependencyManager(this); //Don't allow the this to escape
        translator = new PluginTranslator(this,"en.yml");
    }

    private BukkitPluginWrapper(@NotNull Plugin wrapped) {
        this((JavaPlugin) wrapped);
    }

    public static BukkitPluginWrapper of(@NotNull Plugin wrapped) {
        BukkitPluginWrapper wrapper = new BukkitPluginWrapper(wrapped);
        wrapper.getPluginLogger().info("Constructing Bukkit Plugin!");
        wrapper.init();
        return wrapper;
    }

    @NotNull @Override
    public EServer getEServer() {
        return eServer;
    }

    @NotNull @Override
    public SchedulerAdapter getScheduler() {
        return schedulerAdapter;
    }

    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Must wrap a bukkit command sender");

        return new SimpleSender(new BukkitSenderInfo(eServer,server, (CommandSender) commandSender));
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        return wrapped.getDataFolder().toPath().toAbsolutePath();
    }

    @Nullable @Override
    public InputStream getResourceStream(@NotNull String path) {
        Validate.notNull(path,"Resource Stream Path cannot be null");
        return wrapped.getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        wrapped.saveResource(resourcePath,replace);
    }

    @NotNull
    @Override
    public PluginLogger getPluginLogger() {
        return this.pluginLogger;
    }

    @Override
    public boolean isPluginEnabled() {
        return wrapped.isEnabled();
    }

    @Override
    public void disablePlugin() {
        this.wrapped.getServer().getPluginManager().disablePlugin(wrapped);
    }

    @NotNull
    @Override
    public PluginTranslator getTranslator() {
        if(translator==null)
            throw new IllegalStateException("Illegal Construction of the Bukkit plugin wrapper");
        return translator;
    }

    @Override
    public void onPluginDisable() {
        throw new UnsupportedOperationException("Cannot call plugin disable on the wrapper");
    }

    @Override
    public void onPluginEnable() {
        throw new UnsupportedOperationException("Cannot call plugin enable on the wrapper");
    }

    @Override
    public void registerDependency(String name, boolean required) {
        dependencies.registerDependency(name,required);
    }


    @Override
    public Dependency getDependency(String name) {
        if(dependencies==null)
            throw new IllegalStateException("Illegal Construction of the Bukkit plugin wrapper");

        return dependencies.getDependency(name);
    }

    @Override
    public boolean hasDependency(String name) {
        if(dependencies==null)
            throw new IllegalStateException("Illegal Construction of the Bukkit plugin wrapper");

        return dependencies.hasDependency(name);
    }

    @Override
    public Set<String> getDependencies() {
        if(dependencies==null)
            throw new IllegalStateException("Illegal Construction of the Bukkit plugin wrapper");

        return dependencies.getDependencies();
    }
}
