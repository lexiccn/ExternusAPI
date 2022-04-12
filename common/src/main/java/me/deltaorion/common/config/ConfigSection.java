package me.deltaorion.common.config;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import me.deltaorion.common.config.file.ConfigLoader;
import me.deltaorion.common.config.options.ConfigSectionOptions;
import me.deltaorion.common.config.nested.ConfigObjectValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigSection {

    @NotNull private final MemoryConfig root;
    @Nullable private final ConfigSection parent;
    @NotNull private final String path;
    @NotNull protected final ConfigAdapter adapter;

    public ConfigSection(@NotNull AdapterFactory adapter) {
        if(!(this instanceof MemoryConfig))
            throw new IllegalStateException("Cannot create a root section without being a memory config!");

        Objects.requireNonNull(adapter);
        this.root = (MemoryConfig) this;
        this.path = "";
        this.parent = null;
        this.adapter = adapter.getNew(this);
    }

    public ConfigSection(@NotNull ConfigAdapter adapter, @NotNull String path, @NotNull ConfigSection parent, @NotNull MemoryConfig root) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(parent);
        Objects.requireNonNull(root);
        Objects.requireNonNull(adapter);

        this.root = root;
        this.parent = parent;
        this.path = path;
        this.adapter = adapter;
    }

    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigSection}s (and their children, etc). These
     * will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *             list.
     * @return Set of keys contained within this ConfigSection.
     */
    @NotNull
    public Set<String> getKeys(boolean deep) {
        if(!deep) {
            if(!root.options().copyDefaults())
                return adapter.getKeys();

            Set<String> keys = new HashSet<>(adapter.getKeys());
            ConfigSection defaults = getDefaultSection();
            if(defaults!=null) {
                keys.addAll(defaults.getKeys(false));
            }
            return keys;
        }

        return Collections.unmodifiableSet(getValues(true).keySet());
    }

    private ConfigSection getDefaultSection() {
        ConfigSection defaults = root.getDefaults();
        if(defaults==null)
            return null;

        if(parent==null) {
            return defaults;
        } else {
            return defaults.getConfigurationSection(path);
        }
    }

    /**
     * Gets a Map containing all keys and their values for this section.
     * <p>
     * If deep is set to true, then this will contain all the keys and values
     * within any child {@link ConfigSection}s (and their children,
     * etc). These keys will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys and
     * values of any direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *             list.
     * @return Map of keys and values of this section.
     */
    @NotNull
    public Map<String, Object> getValues(boolean deep) {
        Map<String,Object> values = getValues("",this,new HashMap<>(),deep);
        return Collections.unmodifiableMap(values);
    }

    private Map<String,Object> getValues(String path, ConfigSection section, Map<String,Object> values, boolean deep) {
        for(String key : section.getKeys(false)) {
            final String absKey = path+key;
            values.put(absKey,section.get(key));
            if(deep && section.isConfigurationSection(key)) {
                getValues(absKey + String.valueOf(root.options().pathSeparator()),section.getConfigurationSection(key),values,true);
            }
        }
        return values;
    }

    /**
     * Checks if this {@link ConfigSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will return true.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, either via
     * default or being set.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    
    public boolean contains(@NotNull String path) {
        return contains(path,false);
    }

    /**
     * Checks if this {@link ConfigSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist, the boolean parameter
     * of true has been specified, a default value for the path exists, this
     * will return true.
     * <p>
     * If a boolean parameter of false has been specified, true will only be
     * returned if there is a set value for the specified path.
     *
     * @param path          Path to check for existence.
     * @param ignoreDefault Whether or not to ignore if a default value for the
     *                      specified path exists.
     * @return True if this section contains the requested path, or if a default
     * value exist and the boolean parameter for this method is true.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        if(getValue(path).isObject())
            return true;

        if(ignoreDefault && !root.options().copyDefaults()) {
            return false;
        }

        ConfigSection defaults = getDefaultSection();
        if(defaults==null)
            return false;

        return defaults.contains(path,ignoreDefault);
    }

    /**
     * Checks if this {@link ConfigSection} has a value set for the
     * given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will still return false.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, regardless of
     * having a default.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    
    public boolean isSet(@NotNull String path) {
        return contains(path,true);
    }

    /**
     * Gets the path of this {@link ConfigSection} from its root {@link
     * ConfigLoader}
     * <p>
     * For any {@link ConfigLoader} themselves, this will return an empty
     * string.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     * <p>
     * To retrieve the single name of this section, that is, the final part of
     * the path returned by this method, you may use {@link #getName()}.
     *
     * @return Path of this section relative to its root
     */
    @NotNull
    public String getCurrentPath() {
        if(parent==null)
            return path;

        return parent.getPath(path);
    }


    @NotNull
    private String getPath(String path) {
        if(parent==null)
            return path;

        return parent.getPath(this.path + options().pathSeparator() + path);
    }

    /**
     * Gets the name of this individual {@link ConfigSection}, in the
     * path.
     * <p>
     * This will always be the final part of {@link #getCurrentPath()}, unless
     * the section is orphaned.
     *
     * @return Name of this section
     */
    @NotNull
    public String getName() {
        List<String> split = Splitter.on('.').splitToList(path);
        if(split.size()==0)
            return "";

        return split.get(split.size()-1);
    }

    /**
     * Gets the root {@link ConfigSection} that contains this {@link
     * ConfigSection}
     * <p>
     * For any {@link ConfigSection} themselves, this will return its own
     * object.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Root configuration containing this section.
     */
    @NotNull
    public MemoryConfig getRoot() {
        return root;
    }

    /**
     * Gets the parent {@link ConfigSection} that directly contains
     * this {@link ConfigSection}.
     * <p>
     * For any {@link ConfigSection} themselves, this will return null.
     * <p>
     * If the section is no longer contained within its parent for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Parent section containing this section.
     */
    @Nullable
    public ConfigSection getParent() {
        return parent;
    }


    private Object getDefault(String path) {
        Objects.requireNonNull(path);
        ConfigSection defaults = getDefaultSection();
        if(defaults==null)
            return null;

        return defaults.get(path);
    }

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Object to get.
     * @return Requested Object.
     */
    
    public Object get(String path) {
        return getValue(path).asObject();
    }

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link ConfigLoader}.
     *
     * @param path Path of the Object to get.
     * @param def  The default value to return if the path is not found.
     * @return Requested Object.
     */
    
    public Object get(String path, Object def) {
        return getValue(path,def).asObject();
    }


    private ConfigValue getValue(String path) {
        return getValue(path,getDefault(path));
    }


    private ConfigValue getValue(String path, Object def) {
        Objects.requireNonNull(path);
        return new MemoryValue(adapter.getValue(path),new ConfigObjectValue(def),root);
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link ConfigLoader}s or {@link ConfigSection}s,
     * please use {@link #createSection(java.lang.String)} for that.
     *
     * @param path  Path of the object to set.
     * @param value New value to set the path to.
     */
    
    public void set(String path, Object value) {
        Objects.requireNonNull(path);
        if(value==null) {
            adapter.remove(path);
        } else {
            adapter.set(path,value);
        }
    }

    /**
     * Creates an empty {@link ConfigSection} at the specified path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @return Newly created section
     * @throws UnsupportedOperationException if the config type does not support nesting
     */
    @NotNull
    public ConfigSection createSection(String path) {
        return adapter.createSection(path);
    }

    /**
     * Creates a {@link ConfigSection} at the specified path, with
     * specified values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @param map  The values to used.
     * @return Newly created section
     * @throws UnsupportedOperationException if the config type does not support nesting
     */
    @NotNull
    public ConfigSection createSection(String path, Map<String, Object> map) {
        return adapter.createSection(path,map);
    }

    // Primitives

    /**
     * Gets the requested String by path.
     * <p>
     * If the String does not exist but a default value has been specified,
     * this will return the default value. If the String does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the String to get.
     * @return Requested String.
     */
    @Nullable
    public String getString(String path) {
        return getValue(path).asString();
    }

    /**
     * Gets the requested String by path, returning a default value if not
     * found.
     * <p>
     * If the String does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link ConfigLoader}.
     *
     * @param path Path of the String to get.
     * @param def  The default value to return if the path is not found or is
     *             not a String.
     * @return Requested String.
     */
    @Nullable
    public String getString(String path, String def) {
        return getValue(path).asString();
    }

    /**
     * Checks if the specified path is a String.
     * <p>
     * If the path exists but is not a String, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a String and return appropriately.
     *
     * @param path Path of the String to check.
     * @return Whether or not the specified path is a String.
     */
    
    public boolean isString(String path) {
        return getValue(path).isString();
    }

    /**
     * Gets the requested int by path.
     * <p>
     * If the int does not exist but a default value has been specified, this
     * will return the default value. If the int does not exist and no default
     * value was specified, this will return 0.
     *
     * @param path Path of the int to get.
     * @return Requested int.
     */
    
    public int getInt(String path) {
        return getValue(path).asInt();
    }

    /**
     * Gets the requested int by path, returning a default value if not found.
     * <p>
     * If the int does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link MemoryConfig}.
     *
     * @param path Path of the int to get.
     * @param def  The default value to return if the path is not found or is
     *             not an int.
     * @return Requested int.
     */
    
    public int getInt(String path, int def) {
        return getValue(path,def).asInt();
    }

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not a int, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a int and return appropriately.
     *
     * @param path Path of the int to check.
     * @return Whether or not the specified path is an int.
     */
    
    public boolean isInt(String path) {
        return getValue(path).isInt();
    }

    /**
     * Gets the requested boolean by path.
     * <p>
     * If the boolean does not exist but a default value has been specified,
     * this will return the default value. If the boolean does not exist and
     * no default value was specified, this will return false.
     *
     * @param path Path of the boolean to get.
     * @return Requested boolean.
     */
    
    public boolean getBoolean(String path) {
        return getValue(path).asBoolean();
    }

    /**
     * Gets the requested boolean by path, returning a default value if not
     * found.
     * <p>
     * If the boolean does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link MemoryConfig}.
     *
     * @param path Path of the boolean to get.
     * @param def  The default value to return if the path is not found or is
     *             not a boolean.
     * @return Requested boolean.
     */
    
    public boolean getBoolean(String path, boolean def) {
        return getValue(path,def).asBoolean();
    }

    /**
     * Checks if the specified path is a boolean.
     * <p>
     * If the path exists but is not a boolean, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a boolean and return appropriately.
     *
     * @param path Path of the boolean to check.
     * @return Whether or not the specified path is a boolean.
     */
    
    public boolean isBoolean(String path) {
        return getValue(path).isBoolean();
    }

    /**
     * Gets the requested double by path.
     * <p>
     * If the double does not exist but a default value has been specified,
     * this will return the default value. If the double does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path of the double to get.
     * @return Requested double.
     */
    
    public double getDouble(String path) {
        return getValue(path).asDouble();
    }

    /**
     * Gets the requested double by path, returning a default value if not
     * found.
     * <p>
     * If the double does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link ConfigLoader}.
     *
     * @param path Path of the double to get.
     * @param def  The default value to return if the path is not found or is
     *             not a double.
     * @return Requested double.
     */
    
    public double getDouble(String path, double def) {
        return getValue(path,def).asDouble();
    }

    /**
     * Checks if the specified path is a double.
     * <p>
     * If the path exists but is not a double, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a double and return appropriately.
     *
     * @param path Path of the double to check.
     * @return Whether or not the specified path is a double.
     */
    
    public boolean isDouble(String path) {
        return getValue(path).isDouble();
    }

    /**
     * Gets the requested long by path.
     * <p>
     * If the long does not exist but a default value has been specified, this
     * will return the default value. If the long does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path of the long to get.
     * @return Requested long.
     */
    
    public long getLong(String path) {
        return getValue(path).asLong();
    }

    /**
     * Gets the requested long by path, returning a default value if not
     * found.
     * <p>
     * If the long does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link MemoryConfig}.
     *
     * @param path Path of the long to get.
     * @param def  The default value to return if the path is not found or is
     *             not a long.
     * @return Requested long.
     */
    
    public long getLong(String path, long def) {
        return getValue(path,def).asLong();
    }

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a long and return appropriately.
     *
     * @param path Path of the long to check.
     * @return Whether or not the specified path is a long.
     */
    
    public boolean isLong(String path) {
        return getValue(path).isLong();
    }

    // Java

    /**
     * Gets the requested List by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the List to get.
     * @return Requested List.
     */
    @Nullable
    public List<?> getList(String path) {
        return getValue(path).asList();
    }

    /**
     * Gets the requested List by path, returning a default value if not
     * found.
     * <p>
     * If the List does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link ConfigLoader}.
     *
     * @param path Path of the List to get.
     * @param def  The default value to return if the path is not found or is
     *             not a List.
     * @return Requested List.
     */
    @Nullable
    public List<?> getList(String path, List<?> def) {
        return getValue(path,def).asList();
    }

    /**
     * Checks if the specified path is a List.
     * <p>
     * If the path exists but is not a List, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a List and return appropriately.
     *
     * @param path Path of the List to check.
     * @return Whether or not the specified path is a List.
     */
    
    public boolean isList(String path) {
        return getValue(path).isList();
    }

    /**
     * Gets the requested List of String by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a String if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of String.
     */
    @NotNull
    public List<String> getStringList(String path) {
        return getValue(path).asStringList();
    }

    /**
     * Gets the requested List of Integer by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Integer if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Integer.
     */
    @NotNull
    public List<Integer> getIntegerList(String path) {
        return getValue(path).asIntegerList();
    }

    /**
     * Gets the requested List of Boolean by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Boolean if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Boolean.
     */
    @NotNull
    public List<Boolean> getBooleanList(String path) {
        return getValue(path).asBooleanList();
    }

    /**
     * Gets the requested List of Double by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Double if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Double.
     */
    @NotNull
    public List<Double> getDoubleList(String path) {
        return getValue(path).asDoubleList();
    }

    /**
     * Gets the requested List of Float by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Float if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Float.
     */
    @NotNull
    public List<Float> getFloatList(String path) {
        return getValue(path).asFloatList();
    }

    /**
     * Gets the requested List of Long by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Long if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @param path Path of the List to get.
     * @return Requested List of Long.
     */
    @NotNull
    public List<Long> getLongList(String path) {
        return getValue(path).asLongList();
    }

    /**
     * Gets the requested List of Byte by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Byte if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Byte.
     */
    @NotNull
    public List<Byte> getByteList(String path) {
        return getValue(path).asByteList();
    }

    /**
     * Gets the requested List of Character by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Character if
     * possible, but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Character.
     */
    @NotNull
    public List<Character> getCharacterList(String path) {
        return getValue(path).asCharacterList();
    }

    /**
     * Gets the requested List of Short by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Short if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Short.
     */
    @NotNull
    public List<Short> getShortList(String path) {
        return getValue(path).asShortList();
    }

    /**
     * Gets the requested List of Maps by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Map if possible, but
     * may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Maps.
     */
    @NotNull
    public List<Map<?, ?>> getMapList(String path) {
        return getValue(path).asMapList();
    }

    /**
     * Gets the requested ConfigSection by path.
     * <p>
     * If the ConfigSection does not exist but a default value has been
     * specified, this will return the default value. If the
     * ConfigSection does not exist and no default value was specified,
     * this will return null.
     *
     * @param path Path of the ConfigSection to get.
     * @return Requested ConfigSection.
     */
     @Nullable
    public ConfigSection getConfigurationSection(String path) {
        return getValue(path).asConfigSection();
    }
    /**
     * Checks if the specified path is a ConfigSection.
     * <p>
     * If the path exists but is not a ConfigSection, this will return
     * false. If the path does not exist, this will return false. If the path
     * does not exist but a default value has been specified, this will check
     * if that default value is a ConfigSection and return
     * appropriately.
     *
     * @param path Path of the ConfigSection to check.
     * @return Whether or not the specified path is a ConfigSection.
     */
    
    public boolean isConfigurationSection(String path) {
        return getValue(path).isConfigSection();
    }

    /**
     * A config has a nested structure if a key can link to either a primitive value (such as a string) or another key. By allowing
     * a key to link to another key the config can have an infinite and recursive nest.
     *
     * @return whether this config supports a nested structure.
     */
    
    public boolean supportsNesting() {
        return adapter.supportsNesting();
    }

    /**
     * @return Whether given this config is saved, comments will be preserved.
     */
    
    public boolean supportsCommentPreservation() {
        return adapter.supportsCommentPreservation();
    }

    /**
     * All methods in this class are chainable.
     *
     * @return relevant options regarding this config file.
     */
    @NotNull 
    public ConfigSectionOptions options() {
        return root.options();
    }


    /**
     * Gets the requested comment list by path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param path Path of the comments to get.
     * @return A unmodifiable list of the requested comments, every entry
     * represents one line.
     * @throws UnsupportedOperationException if {@link #supportsCommentPreservation()} is false
     */
    @NotNull 
    public List<String> getComments(@NotNull String path) {
        return adapter.getComments(path);
    }


    /**
     * Gets the requested inline comment list by path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param path Path of the comments to get.
     * @return A unmodifiable list of the requested comments, every entry
     * represents one line.
     * @throws UnsupportedOperationException if {@link #supportsCommentPreservation()} is false
     */
    @NotNull 
    public List<String> getInlineComments(@NotNull String path) {
        return adapter.getInlineComments(path);
    }


    /**
     * Sets the comment list at the specified path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comments will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     *
     * @param path Path of the comments to set.
     * @param comments New comments to set at the path, every entry represents
     * one line.
     * @throws UnsupportedOperationException if {@link #supportsCommentPreservation()} is false
     */
    
    public void setComments(@NotNull String path, @Nullable List<String> comments) {
        adapter.setComments(path,comments);
    }


    /**
     * Sets the inline comment list at the specified path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comment will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     *
     * @param path Path of the comments to set.
     * @param comments New comments to set at the path, every entry represents
     * one line.
     * @throws UnsupportedOperationException if {@link #supportsCommentPreservation()} is false
     */
    
    public void setInlineComments(@NotNull String path, @Nullable List<String> comments) {
        adapter.setInlineComments(path,comments);
    }

    
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path",path)
                .add("values",getValues(true))
                .toString();
    }

    
    public boolean equals(Object o) {
        if(!(o instanceof ConfigSection))
            return false;

        ConfigSection section = (ConfigSection) o;
        if(!section.getCurrentPath().equals(this.getCurrentPath()))
            return false;

        return section.getValues(true).equals(this.getValues(true));
    }

}
