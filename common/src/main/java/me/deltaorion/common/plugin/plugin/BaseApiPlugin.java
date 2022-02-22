package me.deltaorion.common.plugin.plugin;

import me.deltaorion.common.animation.RunningAnimation;
import me.deltaorion.common.command.parser.ParserRegistry;
import me.deltaorion.common.plugin.depend.DependencyManager;
import me.deltaorion.common.locale.translator.PluginTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

//shared api functions
public interface BaseApiPlugin extends EPlugin, DependencyManager, ParserRegistry {

    PluginTranslator getTranslator();


    /**
     * Plugin Disable Logic. This should be overiden by the plugin and any disable logic should be defined. This will be called on reloads
     * and on server shutdowns.
     */
    void onPluginDisable();

    /**
     * Plugin Enable Logic. This should be overriden by the plugin extending this. This will be called on
     * reloads and on server startups
     */

    void onPluginEnable();

    /**
     * Called by a {@link me.deltaorion.common.animation.MinecraftAnimation} when a running animation is created.
     * All animations will be weakly stored here and when the server shuts down all will be automatically cancelled.
     *
     * @param animation The animation to cache.
     */
    void cacheRunning(RunningAnimation<?> animation);

    /**
     * Gets a list of all cached running animations
     */

    @NotNull
    Collection<RunningAnimation<?>> getCachedRunning();

    /**
     * Called by {@link me.deltaorion.common.animation.MinecraftAnimation} when a running animation has finished
     * and should be removed.
     *
     * @param animation The animation to remove from the cache
     */
    void removeCachedRunning(RunningAnimation<?> animation);
}
