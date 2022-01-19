package me.deltaorion.extapi.common.depend;

import me.deltaorion.extapi.common.plugin.EPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleDependencyManager implements DependencyManager {

    private final Map<String,Dependency> dependencies;
    private final EPlugin master;

    public SimpleDependencyManager(EPlugin master) {
        this.dependencies = new HashMap<>();
        this.master = master;
    }

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
    public void registerDependency(String name, boolean required) {
        Dependency dependency = new Dependency(master,name,required);
        dependency.check();

        this.dependencies.put(name.toUpperCase(),dependency);
    }

    /**
     * Gets a plugin dependency object.
     *
     * Use {@link Dependency#isActive()} to check if the dependency is active
     *  CAST {@link Dependency#getDependency()}  to get the actual plugin.
     *
     * @param name The name of the dependency
     * @return a dependency object.
     */

    @Nullable
    public Dependency getDependency(String name) {
        return dependencies.get(name.toUpperCase());
    }

    public boolean hasDependency(String name) {
        return dependencies.containsKey(name.toUpperCase());
    }

    public Set<String> getDependencies() {
        return Collections.unmodifiableSet(dependencies.keySet());
    }
}
