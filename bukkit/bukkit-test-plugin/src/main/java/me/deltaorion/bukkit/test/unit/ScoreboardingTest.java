package me.deltaorion.bukkit.test.unit;

import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.display.bukkit.APIPlayerSettings;
import me.deltaorion.bukkit.display.bukkit.BukkitApiPlayer;
import me.deltaorion.bukkit.display.bukkit.EApiPlayer;
import me.deltaorion.bukkit.display.scoreboard.EScoreboard;
import me.deltaorion.bukkit.display.scoreboard.WrapperScoreboard;
import me.deltaorion.bukkit.test.bukkit.TestPlayer;
import me.deltaorion.common.locale.message.Message;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import org.bukkit.entity.Player;

import static org.junit.Assert.*;

public class ScoreboardingTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public ScoreboardingTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void testScoreboard() {
        TestPlayer tPlayer = new TestPlayer("gamer");
        Player player = null;
        try {
            player = tPlayer.asPlayer();
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
            return;
        }
        BukkitApiPlayer p = new EApiPlayer(plugin,player,new APIPlayerSettings());
        EScoreboard scoreboard = p.setScoreboard("gamer",10);
        assertEquals(10,scoreboard.getSize());
        assertEquals("",scoreboard.getTitle().toString());

        scoreboard.setLine("Gamer",0);
        scoreboard.setLine("Gamer",1,"Hallo");
        scoreboard.setLine(Message.valueOf("Gamer {0}"),2,3);
        scoreboard.setLine(Message.valueOf("Hello World"),3,"Gamer");
        try {
            scoreboard.setLine("Gamer",4,"Gamer");
            fail();
        } catch (IllegalArgumentException e) {

        }

        try {
            scoreboard.setLine("Gamer",1,"Hallo");
        } catch (IllegalArgumentException e) {
            fail();
        }

        assertEquals("Gamer",scoreboard.getLineAt(0).toString());
        assertEquals("Gamer",scoreboard.getLineFromName("Hallo").toString());
        assertEquals("",scoreboard.getLineAt(9).toString());
        assertEquals("Gamer",scoreboard.getLineAt(1).toString());
        assertEquals("Gamer {0}",scoreboard.getLineAt(2).toString());
        assertEquals("Gamer 3",scoreboard.getDisplayedAt(2));
        assertNull(scoreboard.getLineFromName("rrerpok"));
        assertEquals("Hello World",scoreboard.getLineFromName("Gamer").toString());

        scoreboard.setLineByName("Hallo","Hallo");
        scoreboard.setLineByName(Message.valueOf("Gamer"),"Gamer");

        assertEquals("Hallo",scoreboard.getLineFromName("Hallo").toString());
        assertEquals("Gamer",scoreboard.getLineFromName("Gamer").toString());

        scoreboard.setLineByName(Message.valueOf("Gamer {0}"),"Hallo");
        scoreboard.setLineByName(Message.valueOf("Gamer {0}"),"Gamer");

        scoreboard.setLineArgs(3,"3");
        scoreboard.setLineArgs("Hallo",3);

        assertEquals("Gamer 3",scoreboard.getDisplayedAt("Hallo"));
        assertEquals("Gamer 3",scoreboard.getDisplayedAt("Gamer"));

        try {
            new WrapperScoreboard(player,"epwok",plugin,17);
            fail();
        } catch (IllegalStateException e) {
        }

        try {

        } catch (ArrayIndexOutOfBoundsException e) {
            scoreboard.setLine("ewoifkw",10);
            fail();
        }

        try {
            new WrapperScoreboard(player,"epwok",plugin,-5);
            fail();
        } catch (IllegalStateException e) {
        }

        try {
            new WrapperScoreboard(player,"wepokf",plugin,0);
            new WrapperScoreboard(player,"ewopk",plugin,16);
        } catch (IllegalStateException e) {
            fail();
        }

    }

    @Override
    public String getName() {
        return "Scoreboard Test";
    }
}
