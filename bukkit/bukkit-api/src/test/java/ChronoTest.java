import me.deltaorion.bukkit.plugin.server.BukkitServer;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class ChronoTest {
    @Test
    public void testBukkitUnit() {
        //ensure that the bukkit tick time unit correctly represents 50 milliseconds and all the surrounding
        //temporal unit functions work
        assertEquals(BukkitServer.MILLIS_PER_TICK, Duration.of(1,BukkitServer.TICK_UNIT).toMillis());
        assertEquals(BukkitServer.MILLIS_PER_TICK*20,Duration.of(20,BukkitServer.TICK_UNIT).toMillis());
        assertEquals(1,Duration.of(1000/BukkitServer.MILLIS_PER_TICK*60,BukkitServer.TICK_UNIT).toMinutes());
    }
}
