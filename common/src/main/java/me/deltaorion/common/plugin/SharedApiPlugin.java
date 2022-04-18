package me.deltaorion.common.plugin;

import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.parser.ArgumentParser;
import me.deltaorion.common.command.parser.ArgumentParsers;
import me.deltaorion.common.command.parser.ParserRegistry;
import me.deltaorion.common.command.parser.SimpleParserRegistry;
import me.deltaorion.common.config.properties.PropertiesAdapter;
import me.deltaorion.common.locale.translator.PluginTranslator;
import me.deltaorion.common.locale.translator.TranslationManager;
import me.deltaorion.common.plugin.depend.Dependency;
import me.deltaorion.common.plugin.depend.SimpleDependencyManager;
import me.deltaorion.common.plugin.logger.PluginLogger;
import me.deltaorion.common.plugin.scheduler.SchedulerAdapter;
import me.deltaorion.common.plugin.sender.Sender;
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
    @NotNull private final SimpleDependencyManager dependencies;
    @Nullable private TranslationManager translator;
    @NotNull private final ConcurrentMap<RunningAnimation<?>,Object> animationCache;

    public SharedApiPlugin(@NotNull EPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.registry = new SimpleParserRegistry();
        this.dependencies = new SimpleDependencyManager(plugin);
        this.animationCache = new ConcurrentHashMap<>();
        registerDefaults();
    }

    private void registerDefaults() {
        registry.registerParser(Sender.class,ArgumentParsers.SENDER_PARSER(plugin.getEServer()));
    }

    /**
     * Plugin Disable Logic. This should be overridden by the plugin and any disable logic should be defined. This will be called on reloads
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
     * Plugin Enable Logic. This should be overridden by the plugin extending this. This will be called on
     * reloads and on server startups
     */

    @Override
    public void onPluginEnable() {
        if(this.translator==null)
            this.translator = new PluginTranslator(plugin,"en.properties","translations",new PropertiesAdapter(),EServer.DEFAULT_LOCALE);

        this.translator.reload();
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
    public TranslationManager getTranslator() {
        if(this.translator==null)
            throw new IllegalStateException();

        return this.translator;
    }

    @Override
    public void setTranslator(TranslationManager manager) {
        this.translator = manager;
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

    @NotNull @Override
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
