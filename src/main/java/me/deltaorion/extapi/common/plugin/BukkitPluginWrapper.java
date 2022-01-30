package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.ArgumentParsers;
import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.command.parser.SimpleParserRegistry;
import me.deltaorion.extapi.common.command.BukkitCommandMap;
import me.deltaorion.extapi.common.command.SyncBukkitCommand;
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
import me.deltaorion.extapi.common.thread.ErrorReportingThreadPool;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import me.deltaorion.extapi.test.TestEnum;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class BukkitPluginWrapper implements EPlugin {

    @NotNull private final JavaPlugin wrapped;
    @NotNull private final EServer eServer;
    @NotNull private final PluginLogger pluginLogger;
    @NotNull private final SchedulerAdapter schedulerAdapter;
    @NotNull private final Server server;



    private BukkitPluginWrapper(@NotNull JavaPlugin wrapped) {
        Validate.notNull(wrapped,"Wrapped plugin cannot be null!");
        this.wrapped = wrapped;
        this.eServer = new BukkitServer(wrapped.getServer());
        this.server = wrapped.getServer();
        pluginLogger = new JavaPluginLogger(wrapped.getLogger());
        schedulerAdapter = new BukkitSchedulerAdapter(wrapped);
    }

    public BukkitPluginWrapper(@NotNull Plugin wrapped) {
        this((JavaPlugin) wrapped);
    }

    @NotNull @Override
    public EServer getEServer() {
        return eServer;
    }

    @NotNull @Override
    public SchedulerAdapter getScheduler() {
        return schedulerAdapter;
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
}
