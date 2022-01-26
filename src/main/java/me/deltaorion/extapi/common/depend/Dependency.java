package me.deltaorion.extapi.common.depend;

import me.deltaorion.extapi.common.plugin.EPlugin;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a helper class for plugin dependencies. This contains information on the fly about the plugin such as
 * whether it is enabled or not. An easy way to grab the plugin object. For a dependency there is a master-slave relation,
 * that being where the master plugin depends on the slave plugin. Sometimes the slave plugin is not required.
 *
 * This class allows a user to access
 *   - whether it is enabled or not {@link Dependency#isActive()}
 *   - the plugin jar {@link Dependency#getDependency()}
 *
 * This class also automatically checks if the plugin jar is enabled or not and acts appropiately whether it is required or
 * not.
 *
 * To register a dependency use {@link me.deltaorion.extapi.common.plugin.ApiPlugin#registerDependency(String, boolean)} to use this class.
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

        Validate.notNull(master,"Master plugin in dependency cannot be null");

        this.name = name;
        this.required = required;
        this.master = master;

        this.depend = null;
        this.dependObject = null;
    }

    /**
     * This function should grab the dependency plugin object from the server and wrap it. in a new Eplugin. This
     * could be the {@link me.deltaorion.extapi.common.plugin.BukkitPluginWrapper} or the
     * {@link me.deltaorion.extapi.common.plugin.BungeePluginWrapper} or any other plugin wrapper.
     *
     * @return a eplugin wrapping the dependency plugin file.
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
}
