package me.deltaorion.extapi.common.depend;

import me.deltaorion.extapi.common.plugin.EPlugin;

/**
 * This class is a helper class for plugin dependencies. This contains information on the fly about the plugin such as
 * whether it is enabled or not. An easy way to grab the plugin object. For a dependency there is a master-slave relation,
 * that being where the master plugin depends on the slave plugin. Sometimes the slave plugin is not required.
 *
 * This class allows a user to access
 *   - whether it is enabled or not {@link Dependency#isActive()}
 *   - the plugin jar {@link Dependency#getPlugin()}
 *
 * This class also automatically checks if the plugin jar is enabled or not and acts appropiately whether it is required or
 * not.
 *
 * To register a dependency use {@link EPlugin#registerDependency(String, boolean)} to use this class.
 *
 */

public class Dependency {

    //the name needed to fetch the plugin
    private final String name;
    //wheher the plugin is required or not
    private final boolean required;
    private boolean active = false;
    private EPlugin master;
    //the plugin which the master depends on
    private EPlugin plugin;

    public Dependency(EPlugin master, String name, boolean required) {
        this.name = name;
        this.required = required;
        this.master = master;
    }

    /**
     * This function should grab the dependency plugin object from the server and wrap it. in a new Eplugin. This
     * could be the {@link me.deltaorion.extapi.common.plugin.BukkitPluginWrapper} or the
     * {@link me.deltaorion.extapi.common.plugin.BungeePluginWrapper} or any other plugin wrapper.
     *
     * @return a eplugin wrapping the dependency plugin file.
     */
    public EPlugin wrap() {
        return master.getEServer().getPlugin(name);
    }

    public void check() {
        EPlugin wrapped = wrap();
        if(wrapped.isPluginEnabled()) {
            this.active = true;
            this.plugin = wrapped;
        } else {
            if(required) {
                shutdown();
            } else {
                warn();
            }
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

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isActive() {
        return active;
    }

    public EPlugin getPlugin() {
        return plugin;
    }
}
