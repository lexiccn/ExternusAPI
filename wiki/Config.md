# Config API

The config API works extremely similarly to the bukkit config API. It is so similar that I have linked to the bukkit config API here https://bukkit.fandom.com/wiki/Configuration_API_Reference as an introductory guide.

## Creating a Config

To create a config simply use the following

```java
try {
    //Note this will create a YML config file.
    FileConfig config = FileConfig.loadConfiguration(new YamlAdapter(),getResourceStream("config.yml"));
} catch (IOException e) {
   //An error occured when reading the file!
} catch (InvalidConfigurationException e) {
    //The syntax of the config is broken
}
```

It should be noted that creating a yml config is not the only option. Other config files can be made
### Yaml

```java
//Config file that reads .yml files
FileConfig config = FileConfig.loadConfiguration(new YamlAdapter(),getResourceStream("config.yml"));
```

### Properties
```java
//Config file that reads .properties files
FileConfig config = FileConfig.loadConfiguration(new PropertiesAdapter(),getResourceStream("config.properties"));
```

## Reading, Writing and Saving the Config

Reading, Writing and Saving the config follows the same syntax as in bukkit.

## Comments 

The config API supports comment preservation. Not all comments will be preserved however. The following will be preserved
 - Inline Comments for a regular node
 - Block comments above a regular node
 - Block comments above a nested node
 - Block comments above a list
 - The header of the yml file

Any other comment types will not be preserved. 

```yaml
#EPIC HEADER
#Preserved
---
#Block Comment Line 1
#Block Comment Line 2
a: a #Inline 

#Not preserved

#Preserved
b: b #Preserved

#Preserved
nest: #Not preserved
  #preserved
  d: d #preserved

#preserved
list: #not preserved
  - a #not preserved
  - b
  - c #not preserved
```

You can read or set a comment for a node using the following 

```java
//retrieves the comments for the node "a"
config.getComments("a");
//retrieves the inline comments for the node "a"
config.getInlineComments("a");
//sets the comments for node a
config.setComments("a", ImmutableList.of("comment line 1","comment line 2"));
```

### Custom Config Adapter 

If you want to create another config type such as toml, ini or JSON you can easily do so by creating a new `Config Adapter`. To do this you will need to make both a `AdapterFactory` and a `ConfigAdapter`. To do this make a class that implements both of these interfaces. Once you do so then you can make a new FileConfig but instead of using the `YamlAdapter` class use the custom `AdapterFactory`




