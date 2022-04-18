package me.deltaorion.bungee.plugin.plugin;

import me.deltaorion.bungee.plugin.scheduler.BungeeSchedulerAdapter;
import me.deltaorion.bungee.plugin.server.BungeeServer;
import me.deltaorion.common.plugin.logger.JavaPluginLogger;
import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.EPlugin;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.EServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

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
        Objects.requireNonNull(path,"Resource Stream path cannot be null!");
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
