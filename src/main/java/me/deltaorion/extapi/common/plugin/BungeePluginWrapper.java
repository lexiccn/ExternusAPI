package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.command.Command;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.ArgumentParsers;
import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.command.parser.SimpleParserRegistry;
import me.deltaorion.extapi.common.command.BungeeCommand;
import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.depend.SimpleDependencyManager;
import me.deltaorion.extapi.common.sender.BungeeSenderInfo;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.SimpleSender;
import me.deltaorion.extapi.common.logger.JavaPluginLogger;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.BungeeSchedulerAdapter;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.server.BungeeServer;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import me.deltaorion.extapi.test.TestEnum;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class BungeePluginWrapper implements EPlugin {

    @NotNull private final Plugin wrapped;
    @NotNull private final EServer eServer;
    @NotNull private final ProxyServer server;
    @NotNull private final PluginLogger logger;
    @NotNull private final BungeeSchedulerAdapter schedulerAdapter;
    private boolean isEnabled;


    public BungeePluginWrapper(@NotNull Plugin wrapped) {
        this.wrapped = wrapped;
        this.server = wrapped.getProxy();
        this.isEnabled = true;
        this.eServer = new BungeeServer(wrapped.getProxy());
        this.logger = new JavaPluginLogger(wrapped.getLogger());
        this.schedulerAdapter = new BungeeSchedulerAdapter(wrapped);
    }

    @NotNull
    @Override
    public EServer getEServer() {
        return eServer;
    }

    @NotNull
    @Override
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
        Validate.notNull(path,"Resource Stream path cannot be null!");
        return wrapped.getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = wrapped.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + resourcePath);
        }

        File outFile = new File(getDataDirectory().toFile(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataDirectory().toFile(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.warn( "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.severe( "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    @NotNull
    @Override
    public PluginLogger getPluginLogger() {
        return this.logger;
    }

    @Override
    public boolean isPluginEnabled() {
        return this.isEnabled;
    }


    @Override
    public void disablePlugin() {
        wrapped.getProxy().getPluginManager().unregisterCommands(wrapped);
        wrapped.getProxy().getPluginManager().unregisterListeners(wrapped);
        this.isEnabled = false;
        this.logger.warn("Disabling Bungee Plugins is not fully supported. All commands and listeners have been de-registered.");
    }
}
