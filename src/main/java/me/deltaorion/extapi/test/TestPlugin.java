package me.deltaorion.extapi.test;

import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.command.parser.SimpleParserRegistry;
import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.depend.DependencyManager;
import me.deltaorion.extapi.common.depend.SimpleDependencyManager;
import me.deltaorion.extapi.common.logger.JavaPluginLogger;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.sender.TestSender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

public class TestPlugin implements EPlugin {

    private final DependencyManager manager;
    private final EServer server;
    private final Random random = new Random();
    private final PluginLogger logger = new JavaPluginLogger(Logger.getLogger("Test-Plugin"));
    private final PluginTranslator translator;
    private boolean enabled = true;
    private final ParserRegistry registry;

    public TestPlugin(EServer server) {
        this.server = server;
        this.manager = new SimpleDependencyManager(this);
        this.translator = new PluginTranslator(this,"en.yml");
        this.registry = new SimpleParserRegistry();
    }

    @Override
    public void registerDependency(String name, boolean required) {
        manager.registerDependency(name,required);
    }

    @Nullable
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

    @NotNull
    @Override
    public EServer getEServer() {
        return getEServer();
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sender wrapSender(@NotNull Object commandSender) {
        return new TestSender(String.valueOf(random.nextInt()), UUID.randomUUID(),random.nextBoolean(),random.nextBoolean(), Locale.ENGLISH);
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
        return translator;
    }

    @Override
    public void onPluginDisable() {

    }

    @Override
    public void onPluginEnable() {

    }

    @Override
    public <T> void registerParser(@NotNull Class<T> clazz, @NotNull ArgumentParser<T> parser) {
        registry.registerParser(clazz,parser);
    }

    @NotNull
    @Override
    public <T> Collection<ArgumentParser<T>> getParser(@NotNull Class<T> clazz) {
        return registry.getParser(clazz);
    }

    @Override
    public <T> void clearParsers(@NotNull Class<T> clazz) {
        this.registry.clearParsers(clazz);
    }
}
