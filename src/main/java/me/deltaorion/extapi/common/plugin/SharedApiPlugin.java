package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.command.parser.ArgumentParser;
import me.deltaorion.extapi.command.parser.ArgumentParsers;
import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.command.parser.SimpleParserRegistry;
import me.deltaorion.extapi.common.depend.Dependency;
import me.deltaorion.extapi.common.depend.SimpleDependencyManager;
import me.deltaorion.extapi.common.logger.PluginLogger;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import me.deltaorion.extapi.test.TestEnum;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SharedApiPlugin implements BaseApiPlugin {

    @NotNull
    private final EPlugin plugin;
    @NotNull private final ParserRegistry registry;
    @NotNull private SimpleDependencyManager dependencies;
    @Nullable private PluginTranslator translator;
    @NotNull private final ConcurrentMap<RunningAnimation<?>,Object> animationCache;

    private final String ERR_MSG = "Attempting to access abstraction API methods before the plugin has been enabled. Are you" +
            "overriding onEnable? instead of onPluginEnable";

    public SharedApiPlugin(@NotNull EPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.registry = new SimpleParserRegistry();
        this.dependencies = new SimpleDependencyManager(plugin);
        this.animationCache = new ConcurrentHashMap<>();
        registerDefaults();
    }

    private void registerDefaults() {
        registry.registerParser(TestEnum.class, ArgumentParsers.TEST_PARSER());
        registry.registerParser(Sender.class,ArgumentParsers.SENDER_PARSER(plugin.getEServer()));

    }

    /**
     * Plugin Disable Logic. This should be overiden by the plugin and any disable logic should be defined. This will be called on reloads
     * and on server shutdowns.
     */
    @Override
    public void onPluginDisable() {
        cleanupAnimation();
    }

    private void cleanupAnimation() {
        Iterator<RunningAnimation<?>> iterator = animationCache.keySet().iterator();
        while(iterator.hasNext()) {
            RunningAnimation<?> animation = iterator.next();
            animation.cancel();
            iterator.remove();
        }
    }

    /**
     * Plugin Enable Logic. This should be overriden by the plugin extending this. This will be called on
     * reloads and on server startups
     */

    @Override
    public void onPluginEnable() {
        this.translator = new PluginTranslator(plugin,"en.yml");
    }

    @Override
    public void cacheRunning(RunningAnimation<?> animation) {
        animationCache.put(animation,new Object());
    }

    @NotNull
    @Override
    public Collection<RunningAnimation<?>> getCachedRunning() {
        return Collections.unmodifiableSet(animationCache.keySet());
    }

    @Override
    public void removeCachedRunning(RunningAnimation<?> animation) {
        animationCache.remove(animation);
    }


    @NotNull
    public PluginTranslator getTranslator() {
        if(this.translator==null)
            throw new IllegalStateException(ERR_MSG);

        return this.translator;
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
        registry.clearParsers(clazz);
    }

    @Override
    public void registerDependency(@NotNull String name, boolean required) {
        dependencies.registerDependency(name,required);
    }

    @Nullable
    @Override
    public Dependency getDependency(@NotNull String name) {
        return dependencies.getDependency(name);
    }

    @Override
    public boolean hasDependency(@NotNull String name) {
        return dependencies.hasDependency(name);
    }

    @Override
    public Set<String> getDependencies() {
        return dependencies.getDependencies();
    }

    @NotNull
    @Override
    public EServer getEServer() {
        return plugin.getEServer();
    }

    @NotNull
    @Override
    public SchedulerAdapter getScheduler() {
        return plugin.getScheduler();
    }

    @NotNull
    @Override
    public Path getDataDirectory() {
        return plugin.getDataDirectory();
    }

    @Nullable
    @Override
    public InputStream getResourceStream(@NotNull String path) {
        return plugin.getResourceStream(path);
    }

    @Override
    public void saveResource(@NotNull String resourcePath, boolean replace) {
        plugin.saveResource(resourcePath,replace);
    }

    @NotNull
    @Override
    public PluginLogger getPluginLogger() {
        return plugin.getPluginLogger();
    }

    @Override
    public boolean isPluginEnabled() {
        return plugin.isPluginEnabled();
    }

    @Override
    public void disablePlugin() {
        plugin.disablePlugin();
    }
}
