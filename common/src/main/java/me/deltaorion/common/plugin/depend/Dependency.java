package me.deltaorion.common.plugin.depend;

import com.google.common.base.MoreObjects;
import me.deltaorion.common.plugin.plugin.EPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * This class is a helper class for plugin dependencies. This contains information on the fly about the plugin such as
 * whether it is enabled or not. An easy way to grab the plugin object. For a dependency there is a master-slave relation,
 * that being where the master plugin depends on the slave plugin. The slave plugin may or may not be enabled(is active). The slave plugin
 * may or may not be on the server(is active) . The slave plugin may or may not be required (isRequired).
 *
 *
 * This class allows a user to access
 *   - whether it is enabled or not {@link Dependency#isActive()}
 *   - the plugin jar {@link Dependency#getDependency()}
 *
 * This class also automatically checks if the plugin jar is enabled or not and acts appropiately whether it is required or
 * not.
 *
 * To register a dependency use {@link me.deltaorion.common.plugin.plugin.ApiPlugin#registerDependency(String, boolean)} to use this class.
 *
 */

public class Dependency {

    //the name needed to fetch the plugin
    @NotNull private final String name;
    //whether the plugin is required or not
    private final boolean required;
    private boolean active = false;
    @NotNull private final EPlugin master;
    //the plugin which the master depends on
    @Nullable private EPlugin depend;
    @Nullable private Object dependObject;

    public Dependency(@NotNull EPlugin master, @NotNull String name, boolean required) {

        Objects.requireNonNull(master,"Master plugin in dependency cannot be null");

        this.name = name;
        this.required = required;
        this.master = master;

        this.depend = null;
        this.dependObject = null;
    }

    /**
     * This function should grab the dependency plugin object from the server and wrap it. in a new {@link EPlugin}. The function
     * should then check whether the dependant plugin is enabled.
     *   - If the plugin is enabled then do nothing
     *   - If the plugin is enabled and required = true then disable the master plugin
     *   - If the plugin is enabled and not required = false then warn that the dependent plugin is not in the server
     */


    public void check() {
        EPlugin wrapped = master.getEServer().getPlugin(name);
        Object object = master.getEServer().getPluginObject(name);

        if(wrapped==null) {
            dependInactive();
        } else if(!wrapped.isPluginEnabled()) {
            dependInactive();
        } else {
            dependActive(wrapped,object);
        }
    }

    private void dependActive(EPlugin wrapped, Object pluginObject) {
        this.active = true;
        this.depend = wrapped;
        this.dependObject = pluginObject;
    }

    private void dependInactive() {
        if(required) {
            shutdown();
        } else {
            warn();
        }
    }



    private void warn() {
        master.getPluginLogger().warn("Dependency '"+name+"' was not found. However " +
                "it is not essential to run the plugin!");
    }



    public void shutdown() {

        master.getPluginLogger().severe("=======================================================");
        master.getPluginLogger().severe("");
        master.getPluginLogger().severe("Required Dependency '"+name + "' was NOT found. Please install" +
                "the dependency to use the plugin! ");
        master.getPluginLogger().severe("");
        master.getPluginLogger().severe("=======================================================");

        master.disablePlugin();
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isActive() {
        if(active && depend != null) {
            return depend.isPluginEnabled();
        } else {
            return false;
        }
    }

    @Nullable
    public Object getDependency() {
        return dependObject;
    }

    @Nullable
    public EPlugin getDependEPlugin() {
        return depend;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Dependency))
            return false;

        Dependency dependency = (Dependency) o;
        //other information isn't relevant, as you cant register two dependencies with the same plugin and name
        return dependency.name.equals(this.name) && this.master.equals(dependency.master);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name",name)
                .add("required",required)
                .add("active",active).toString();
    }
}
