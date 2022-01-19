import me.deltaorion.extapi.common.version.MinecraftVersion;
import me.deltaorion.extapi.common.version.VersionFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinecraftVersionTest {

    @Test
    public void testMinecraftVersion() {
        MinecraftVersion onePointEight = new MinecraftVersion(8,8);
        MinecraftVersion onePointNine = new MinecraftVersion(9,1);
        MinecraftVersion onePointEightPointNine = new MinecraftVersion(8,9,"");

        assertEquals(onePointEight.compareTo(onePointNine),-1);
        assertEquals(onePointEight.compareTo(onePointEight),0);
        assertEquals(onePointEight.compareTo(onePointEightPointNine),-1);
        assertEquals(onePointEight.toString(),"1.8.8");
    }

    @Test
    public void parseTest() {
        MinecraftVersion minecraftVersion = VersionFactory.parse("1.8.8-R0.2-SNAPSHOT");

        assertEquals(minecraftVersion.getMajor(),8);
        assertEquals(minecraftVersion.getMinor(),8);
        assertEquals(minecraftVersion.getSnapShot(),"R0.2");

        MinecraftVersion bungeeVersion = VersionFactory.parseBungee("git:BungeeCord-Bootstrap:1.18-R0.1-SNAPSHOT:425ee4e:1618");

        assertEquals(bungeeVersion.getMajor(),18);
        assertEquals(bungeeVersion.getMinor(),0);
        assertEquals(bungeeVersion.getSnapShot(),"R0.1");
    }
}
