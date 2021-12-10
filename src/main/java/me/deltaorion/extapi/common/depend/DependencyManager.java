package me.deltaorion.extapi.common.depend;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface DependencyManager {

    /**
     * Registers a plugin dependency. A dependency has useful methods such as getting the plugin and checking whether
     * it is enabled or not. Dependencies are identified by the plugin name which is the name used by the plugin loader
     * to identify a plugin. One can use {@link Dependency#isActive()}
     *
     * For example if a plugin were to depend on 'TownyAdvanced - https://github.com/TownyAdvanced/Towny' The plugin name
     * would be Towny.
     *
     * @param name The name used by the plugin loader to identify the dependency
     * @param required Whether the dependency is essential for this plugin or not
     */
    public void registerDependency(String name, boolean required);

    /**
     * Gets a plugin dependency object.
     *
     * Use {@link Dependency#isActive()} to check if the dependency is active
     *  CAST {@link Dependency#getPlugin()#getPluginObject()} to get the actual plugin.
     *
     * @param name The name of the dependency
     * @return a dependency object.
     */

    @Nullable
    public Dependency getDependency(String name);

    public boolean hasDependency(String name);

    public Set<String> getDependencies();
}
