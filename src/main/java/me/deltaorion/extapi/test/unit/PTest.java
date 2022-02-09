package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.display.bukkit.BukkitApiPlayer;
import me.deltaorion.extapi.display.bukkit.BukkitPlayerManager;
import me.deltaorion.extapi.display.bukkit.SimpleBukkitPlayerManager;
import me.deltaorion.extapi.test.unit.bukkit.TestPlayer;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        System.out.println(players.getPlayers());
        assertEquals(0,players.getPlayers().size());
        assertFalse(players.hasPlayer(new TestPlayer("Gamer")));
        Map<Player,BukkitApiPlayer> playerList = new HashMap<>();
        for(int i=0;i<100;i++) {
            Player player = new TestPlayer("Jimmy");
            playerList.put(player,players.getPlayer(player));
        }
        for(Map.Entry<Player,BukkitApiPlayer> entry : playerList.entrySet()) {
            assert entry.getValue() == players.getPlayer(entry.getKey());
            assert entry.getKey() == entry.getValue().getPlayer();
        }

        assertNotNull(players.getPlayer(new ArrayList<>(playerList.keySet()).get(0).getUniqueId()));
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
