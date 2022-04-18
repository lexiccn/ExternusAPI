# ExternusAPI

ExtAPI, or Externus API is a lightweight API which wraps around the spigot API to make plugins easier to develop, eliminate common boilerplate, automate common tasks and unify syntax across all versions.

---

## Building ExtAPI

If you would like to build the Externus API it should be done so using <a href="https://maven.apache.org/">Apache Maven</a>.

### Maven

<ul>
  <li>Open the source directory in your favourite terminal/console</li>
  <li>Run the command ```mvn clean install```</li>
</ul>

---

## Including ExtAPI

To include the ExtAPI into your pom.xml do the following steps

### Maven

<ul>
  <li>Build ExtAPI using the build tutorial above to install ExtAPI into your local .m2 repository
  <li>Include the following depend tag into your pom.xml that is relevant to the plugin you are making
</ul>

#### Bukkit

```xml
<dependency>
  <groupId>me.deltaorion</groupId>
  <artifactId>bukkit-api</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

#### Bungee

```xml
<dependency>
  <groupId>me.deltaorion</groupId>
  <artifactId>bungee-api</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

---

## Usage

To learn how to use the API simply follow the wiki. There are also examples avaliable in the bukkit-test-plugin and bungee-test-plugin modules respectively

---

##Licensing 

Add something here if this is being released to public, preferably the GNU license or some kind of source avaliable license 
