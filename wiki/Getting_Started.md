# Getting Started
## Making an API Plugin

To begin creating plugins using the ExtAPI start by extending the relevant plugin class. To execute logic on enable simply override the method 

### Bukkit 

```java
package me.deltaorion.bukkit;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;

public final class MyPlugin extends BukkitPlugin {
    
}
```

### Bungee

```java
package me.deltaorion.bukkit;

import me.deltaorion.bungee.plugin.plugin.BungeePlugin;

public final class MyPlugin extends BungeePlugin {
    
}
```

## Enable and Disable

To do onEnable and onDisable logic simply override their respective methods as shown below. 

```java
package me.deltaorion.bukkit;

import me.deltaorion.bungee.plugin.plugin.BungeePlugin;

public final class MyPlugin extends BungeePlugin {
    @Override
    public void onPluginEnable() {
        getPluginLogger().info("I am being enabled!");
    }

    @Override
    public void onPluginDisable() {
        getPluginLogger().info("I am being disabled!");
    }
}
```

Once you have done this you can get started on making a plugin with the API. 

## Common Features

A Bukkit or Bungee plugin are all API Plugins. The interface APIPlugin is simply the common interface between both of them. 

```java
//Note this will work the same for a BungeePlugin!!!
public final class MyPlugin extends BukkitPlugin {
    
    public MyPlugin() {
        APIPlugin plugin = this;
    }
}
```

An API Plugin regardless of whether it is bukkit, bungee or any other variant has several useful shared features. Below are some especially handy ones. 

```java
APIPlugin plugin = this;
//Represents any plugin logger
plugin.getPluginLogger().info("Logs to the console on info level");

//returns a generic server object
plugin.getEServer();

//represents a generic command sender whether this be a player, or the console.
Sender sender = plugin.getEServer().wrapSender();

//represents the version of the server
MinecraftVersion version = plugin.getEServer().getServerVersion();
```

These features are useful if you are going to do any shared actions between plugin variants. Others like the generic server version can simply be useful if an action is dependent on the servers version. 

There are also plenty of other features that I have ommited from the above list. 

## Dependencies

Another useful feature of the API is dependency management. This allows you to quickly check and retrieve a dependency plugin instance exists. This is especially useful for soft-depends

To use the enhanced dependency API first add to the plugin yml

```yaml
softdepend: [ NBTAPI ]
```

To register your dependency do so as follows. Note when you register a dependency it will run checks to see if it is enabled, this it is most appropriate to do so once the plugin has enabled. 

```java
public void onPluginEnable(){
    //APIPlugin is simply the common interface between any plugin whether it be bukkit or bungee.
    APIPlugin plugin=this;
    //The following should be set to
    //  - true: if the dependency is needed for the plugin to run
    //  - false: if the plugin can run without the dependency, i.e. it is a soft-depend
    boolean isDependencyRequiredToRun=false;
    plugin.registerDependency("NBTAPI",isDependencyRequiredToRun);
}
```

The dependency can then be manipulated as follows.
```java
//The following returns true if
//  - the dependency plugin is on the server
//  - the dependency plugin is enabled
System.out.println("Dependency Active: " + getDependency("NBTAPI").isActive());

//The following returns the actual an instance of the plugin. This will return a java.lang.Object
//you can now cast this to the original plugin object!
System.out.println("Dependency Plugin Object: "+getDependency("NBTAPI").getDependency());

//The following returns an API Plugin wrapper of the plugin instance, allowing you to quickly access
//common features such as if it is enabled, or its logger. This cannot be cast to the original plugin object
System.out.println("Plugin Wrapper: "+getDependency("NBTAPI").getDependEPlugin());
```




