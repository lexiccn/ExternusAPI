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
     *  CAST {@link Dependency#getDependency()} to get the actual plugin.
     *
     * @param name The name of the dependency
     * @return a dependency object.
     */

    @Nullable
    public Dependency getDependency(String name);

    /**
     * Checks whether the dependency manager has a dependency of the given name
     *
     * @param name the name of the dependency as defined in the plugin.yml
     * @return whether teh dependency is active or not
     */

    public boolean hasDependency(String name);

    /**
     * Gets an immutable list of the names of all dependencies registered in the dependency manager
     *
     * @return a list of the names of all dependencies registered.
     */

    public Set<String> getDependencies();
}
