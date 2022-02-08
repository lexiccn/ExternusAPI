package me.deltaorion.extapi.common.plugin;

import me.deltaorion.extapi.animation.RunningAnimation;
import me.deltaorion.extapi.command.parser.ParserRegistry;
import me.deltaorion.extapi.common.depend.DependencyManager;
import me.deltaorion.extapi.locale.translator.PluginTranslator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

//shared api functions
public interface BaseApiPlugin extends EPlugin, DependencyManager, ParserRegistry {

    public PluginTranslator getTranslator();


    /**
     * Plugin Disable Logic. This should be overiden by the plugin and any disable logic should be defined. This will be called on reloads
     * and on server shutdowns.
     */
    public void onPluginDisable();

    /**
     * Plugin Enable Logic. This should be overriden by the plugin extending this. This will be called on
     * reloads and on server startups
     */

    public void onPluginEnable();

    /**
     * Called by a {@link me.deltaorion.extapi.animation.MinecraftAnimation} when a running animation is created.
     * All animations will be weakly stored here and when the server shuts down all will be automatically cancelled.
     *
     * @param animation The animation to cache.
     */
    public void cacheRunning(RunningAnimation<?> animation);

    /**
     * Gets a list of all cached running animations
     */

    @NotNull
    public Collection<RunningAnimation<?>> getCachedRunning();

    /**
     * Called by {@link me.deltaorion.extapi.animation.MinecraftAnimation} when a running animation has finished
     * and should be removed.
     *
     * @param animation The animation to remove from the cache
     */
    public void removeCachedRunning(RunningAnimation<?> animation);
}
