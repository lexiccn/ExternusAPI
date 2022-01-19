package me.deltaorion.extapi.common.plugin;

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
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.Set;

public class BungeePluginWrapper implements EPlugin {

    @NotNull private final Plugin wrapped;
    @NotNull private final EServer eServer;
    @NotNull private final ProxyServer server;
    @NotNull private final PluginLogger logger;
    @NotNull private final BungeeSchedulerAdapter schedulerAdapter;
    @Nullable private SimpleDependencyManager dependencies;
    @Nullable private PluginTranslator translator;
    private boolean isEnabled;


    private BungeePluginWrapper(@NotNull Plugin wrapped) {
        this.wrapped = wrapped;
        this.server = wrapped.getProxy();
        this.isEnabled = true;
        this.eServer = new BungeeServer(wrapped.getProxy());
        this.logger = new JavaPluginLogger(wrapped.getLogger());
        this.schedulerAdapter = new BungeeSchedulerAdapter(wrapped);

    }

    private void init() {
        this.dependencies = new SimpleDependencyManager(this);
        this.translator = new PluginTranslator(this,"en.yml");
    }

    public static BungeePluginWrapper of(@NotNull Plugin wrapped) {
        BungeePluginWrapper wrapper = new BungeePluginWrapper(wrapped);
        wrapper.getPluginLogger().info("Constructing Bungee Plugin!");
        wrapper.init();
        return wrapper;
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

    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        if(!(commandSender instanceof CommandSender))
            throw new IllegalArgumentException("Command Sender must be a net.md5 command sender");

        return new SimpleSender(new BungeeSenderInfo((CommandSender) commandSender,server,getEServer()));
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

    @NotNull
    @Override
    public PluginTranslator getTranslator() {
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

    public Plugin getPlugin() {
        return this.wrapped;
    }
}
