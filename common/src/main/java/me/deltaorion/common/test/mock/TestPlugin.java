package me.deltaorion.common.test.mock;

import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.Command;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.command.parser.ArgumentParsers;
import me.deltaorion.common.plugin.depend.Dependency;
import me.deltaorion.common.plugin.logger.JavaPluginLogger;
import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.plugin.ApiPlugin;
import me.deltaorion.common.plugin.plugin.BaseApiPlugin;
import me.deltaorion.common.plugin.plugin.SharedApiPlugin;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.sender.Sender;
import me.deltaorion.common.plugin.server.EServer;
import me.deltaorion.common.locale.translator.PluginTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class TestPlugin implements ApiPlugin {

    private final EServer server;
    private final Random random = new Random();
    private final PluginLogger logger = new JavaPluginLogger(Logger.getLogger("Test-Plugin"));
    private boolean enabled = true;
    private final BaseApiPlugin baseApiPlugin;
    private final TestSchedularAdapter adapter;

    public TestPlugin(EServer server) {
        this.server = server;
        this.adapter = new TestSchedularAdapter();
        //a little dodgy but doesnt really matter
        this.baseApiPlugin = new SharedApiPlugin(this);
        this.baseApiPlugin.registerParser(Sender.class, ArgumentParsers.SENDER_PARSER(server));
        this.baseApiPlugin.registerParser(TestEnum.class,ArgumentParsers.TEST_PARSER());
    }

    @Override
    public void registerDependency(@NotNull String name, boolean required) {
        baseApiPlugin.registerDependency(name,required);
    }

    @Nullable
    @Override
    public Dependency getDependency(@NotNull String name) {
        return baseApiPlugin.getDependency(name);
    }

    @Override
    public boolean hasDependency(@NotNull String name) {
        return baseApiPlugin.hasDependency(name);
    }

    @NotNull
    @Override
    public Set<String> getDependencies() {
        return baseApiPlugin.getDependencies();
    }

    @NotNull
    @Override
    public EServer getEServer() {
        return server;
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        return adapter;
    }

    public void onDisable() {
        adapter.shutdown();
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        return FileSystems.getDefault().getPath("C:\\Users\\User\\Documents\\abc\\translations");
    }

    @Nullable
    @Override
    public InputStream getResourceStream(@NotNull String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResourceStream(resourcePath);
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
        return logger;
    }

    @Override
    public boolean isPluginEnabled() {
        return enabled;
    }

    @Override
    public void disablePlugin() {
        this.enabled = false;
    }

    @Override
    public PluginTranslator getTranslator() {
        return baseApiPlugin.getTranslator();
    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onPluginEnable() {

    }

    @Override
    public void cacheRunning(RunningAnimation<?> animation) {
        baseApiPlugin.cacheRunning(animation);
    }

    @NotNull
    @Override
    public Collection<RunningAnimation<?>> getCachedRunning() {
        return baseApiPlugin.getCachedRunning();
    }

    @Override
    public void removeCachedRunning(RunningAnimation<?> animation) {
        baseApiPlugin.removeCachedRunning(animation);
    }

    @Override
    public void registerCommand(@NotNull Command command, @NotNull String... names) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser) {
        baseApiPlugin.registerParser(clazz,parser);
    }

    @NotNull
    @Override
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz) {
        return baseApiPlugin.getParser(clazz);
    }

    @Override
    public <T> void clearParsers(@NotNull Class<T> clazz) {
        baseApiPlugin.clearParsers(clazz);
    }
}
