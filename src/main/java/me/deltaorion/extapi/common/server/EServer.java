package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface EServer {

    UUID CONSOLE_UUID = new UUID(0,0);

    String CONSOLE_NAME = "Console";

    Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * Returns the Minecraft version of the server. For bukkit server variants this means the version which
     * the game is set at. For the proxy server this is the maximum version which is supported.
     *
     * A version consists of a major, minor and snapshot.
     *
     * For example if the version was 1.8.6 The major would be 8, and the minor 6
     *
     * @return The minecraft version of the server
     */
    public MinecraftVersion getServerVersion();

    /**
     * The brand of the server. If a server is a Bukkit Server for example this would return CraftBukkit.
     * If this were a bungee server this would return BungeeCord.
     *
     * @return The brand of the server.
     */

    public String getServerBrand();

    /**
     * Gets a list of online players on the server. This does NOT include the console. .
     *
     * @return A list of all online players on the server
     */

    public List<UUID> getOnlinePlayers();

    /**
     * Gets a list of online players on the server as senders. This does NOT include the console.
     *
     * @return A list of all online players on the server as senders.
     */

    public List<Sender> getOnlineSenders();

    /**
     * Retrieves a sender by name ignoring case. If the sender does not exist
     * or is not online. Then this will return null
     *
     * @param name The sender name
     * @return A sender who's name matches or null
     */

    default Sender getSenderExact(String name) {
        for(Sender sender : getOnlineSenders()) {
            if(sender.getName().equalsIgnoreCase(name)) {
                return sender;
            }
        }
        return null;
    }

    /**
     * Gets the console as a sender.
     *
     * @return The console as a sender.
     */

    public Sender getConsoleSender();

    /**
     * Gets the maximum player count of the server
     *
     * @return The maximum player count of the server
     */

    public int getMaxPlayer();

    /**
     * Checks whether a player of the given UUID is online. If one uses the CONSOLE UUID this will
     * return false whether someone is connected to it or not.
     *
     * @param uuid The UUID of the player who to check if online
     * @return Whether the player is or is NOT online.
     */

    public boolean isPlayerOnline(@NotNull UUID uuid);

    /**
     * Gets a plugin of a given name. The plugin name should be the name specified in the relevant plugin.yml
     * For example the TownyAdvanced Plugin has the name Towny. If the plugin cannot be found then this will
     * return null
     *
     * @param name The name of the plugin
     * @return The relevant plugin object.
     */

    @Nullable
    public EPlugin getPlugin(@NotNull String name);

    @Nullable
    public Object getPluginObject(@NotNull String name);

    /**
     * Checks whether a plugin of a given name is enabled or not.
     *
     * @param name The name of the plugin as defined in its plugin.yml
     * @return Whether said plugin is enabled on the server ot not
     */

    public boolean isPluginEnabled(@NotNull String name);

    /**
     * Translates a string using an alternate color code character into a
     * string that uses the internal ChatColor.COLOR_CODE color code
     * character. The alternate color code character will only be replaced if
     * it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
     *
     * @param textToTranslate Text containing the alternate color code character.
     * @return Text containing the ChatColor.COLOR_CODE color code character.
     */

    public String translateColorCodes(@NotNull String textToTranslate);

    public String getServerName();

    /**
     * Wraps a command sender, player or variant into a plugin command sender.
     *
     * @param commandSender The command sender object, this could be a commandsender, player, the console etc
     * @return a sender
     * @throws IllegalArgumentException if the sender is not a valid command sender type
     */

    @NotNull
    public Sender wrapSender(@NotNull Object commandSender);
}
