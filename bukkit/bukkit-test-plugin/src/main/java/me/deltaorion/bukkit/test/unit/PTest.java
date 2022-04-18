package me.deltaorion.bukkit.test.unit;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.display.bukkit.BukkitApiPlayer;
import me.deltaorion.bukkit.display.bukkit.BukkitPlayerManager;
import me.deltaorion.bukkit.test.bukkit.TestPlayer;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class PTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public PTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void testCache() {
        BukkitPlayerManager players = plugin.getBukkitPlayerManager();
        assertEquals(0,players.getPlayers().size());
        try {
            assertFalse(players.hasPlayer(new TestPlayer("Gamer").asPlayer()));
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
            return;
        }
        Map<Player, BukkitApiPlayer> playerList = new HashMap<>();
        for(int i=0;i<100;i++) {
            Player player = new TestPlayer("Jimmy").asPlayer();
            playerList.put(player,players.getPlayer(player));
        }
        for(Map.Entry<Player,BukkitApiPlayer> entry : playerList.entrySet()) {
            assert entry.getValue() == players.getPlayer(entry.getKey());
            assert entry.getKey().getUniqueId() == entry.getValue().getUniqueID();
        }

        assertNull(players.getPlayer(UUID.randomUUID()));

        for(Player player : playerList.keySet()) {
            players.removeCached(player);
        }
        assertEquals(0,players.getPlayers().size());
    }

    @Override
    public String getName() {
        return "BukkitApiPlayer Test";
    }
}
