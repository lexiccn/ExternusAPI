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
        assertEquals(onePointNine.toString(),"1.9.1");
    }

    @Test
    public void parseTest() {
        MinecraftVersion minecraftVersion = VersionFactory.parse("1.8.8-R0.2-SNAPSHOT");

        assertEquals(minecraftVersion.getMajor(),8);
        assertEquals(minecraftVersion.getMinor(),8);
        assertEquals(minecraftVersion.getSnapShot(),"R0.2");

        MinecraftVersion minecraftVersion2 = VersionFactory.parse("1.9.4-R0.2-SNAPSHOT");

        assertEquals(minecraftVersion2.getMajor(),9);
        assertEquals(minecraftVersion2.getMinor(),4);
        assertEquals(minecraftVersion2.getSnapShot(),"R0.2");

        MinecraftVersion minecraftVersion3 = VersionFactory.parse("1.32.16-abc-SNAPSHOT");

        assertEquals(minecraftVersion3.getMajor(),32);
        assertEquals(minecraftVersion3.getMinor(),16);
        assertEquals(minecraftVersion3.getSnapShot(),"abc");

        MinecraftVersion bungeeVersion = VersionFactory.parseBungee("git:BungeeCord-Bootstrap:1.18-R0.1-SNAPSHOT:425ee4e:1618");

        assertEquals(bungeeVersion.getMajor(),18);
        assertEquals(bungeeVersion.getMinor(),0);
        assertEquals(bungeeVersion.getSnapShot(),"R0.1");
    }
}
