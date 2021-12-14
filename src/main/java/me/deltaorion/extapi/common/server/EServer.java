package me.deltaorion.extapi.common.server;

import me.deltaorion.extapi.common.entity.sender.Sender;
import me.deltaorion.extapi.common.plugin.EPlugin;
import me.deltaorion.extapi.common.scheduler.SchedulerAdapter;
import me.deltaorion.extapi.common.version.MinecraftVersion;
import org.bukkit.Server;

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

    public MinecraftVersion getVersion();

    /**
     * The brand of the server. If a server is a Bukkit Server for example this would return CraftBukkit.
     * If this were a bungee server this would return BungeeCord.
     *
     * @return The brand of the server.
     */

    public String getBrand();

    /**
     * Gets a list of online players on the server. This does NOT include the console. To get the player respective
     * to the server software you will need to use the relevant getPlayer() method in the {@link #getServerObject()} method.
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

    public boolean isPlayerOnline(UUID uuid);

    /**
     * Gets a plugin of a given name. To get the stored plugin use {@link EPlugin#getPluginObject()} as cast
     * it to the relevant plugin type. The plugin name should be the name specified in the relevant plugin.yml
     * For example the TownyAdvanced Plugin has the name Towny.
     *
     * @param name The name of the plugin
     * @return The relevant plugin object.
     */

    public EPlugin getPlugin(String name);

    /**
     * Checks whether a plugin of a given name is enabled or not.
     *
     * @param name
     * @return
     */

    public boolean isPluginEnabled(String name);

    public Object getServerObject();

    public String translateColorCodes(String raw);

}
