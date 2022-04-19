package me.deltaorion.bukkit.test.unit;

import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.plugin.version.MinecraftVersion;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.junit.Assert.*;

public class EMaterialTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public EMaterialTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void test() {
        if(!EMaterial.isInitialised()) {
            printNotInitialised();
            return;
        }
        basicTest();
        testExist();
        testVersion();
        testVersion2();
        testNBT();
        testMatching();
        testMatching2();
        testMatching3();
    }

    public void basicTest() {
        EMaterial material = EMaterial.PLAYER_HEAD;
        MinecraftVersion lowest = material.getRelease();
        MinecraftVersion highest = material.getDeprecation();

        MinecraftVersion thisVersion = plugin.getEServer().getServerVersion();

        boolean compatible = false;

        if(thisVersion.compareTo(lowest)>=0) {
            if(highest!=null) {
                if(thisVersion.compareTo(highest)<=0) {
                    compatible = true;
                }
            } else {
                compatible = true;
            }
        }

        assertEquals(compatible,material.isCompatible(thisVersion));

        assertEquals(EMaterial.matchMaterialAnyVersion(material.name()),material);
        assertNull(EMaterial.matchMaterialAnyVersion("reiojgreoi"));
        assertEquals(EMaterial.matchMaterial(material.getId()),material);
        assertEquals(EMaterial.matchMaterial(new ItemBuilder(material).build()),material);
    }

    public void testNBT() {
        List<EMaterial> notNoNBTButFailed = new ArrayList<>();
        List<EMaterial> noNBTButPassed = new ArrayList<>();
        for(EMaterial material : EMaterial.values(plugin.getEServer().getServerVersion())) {
            ItemBuilder builder = null;
            try {
                 builder = new ItemBuilder(material)
                        .addTag("Gamer", "Gamer");
            } catch (Exception e) {
                if (!material.noNBT()) {
                    notNoNBTButFailed.add(material);
                }
                continue;
            }
            if(material.noNBT()) {
                noNBTButPassed.add(material);
                continue;
            }
            ItemStack itemStack = builder.build();
            Inventory inventory = Bukkit.createInventory(null,54);
            inventory.addItem(itemStack);
            inventory.clear();
            assertEquals(material.getBukkitMaterial(),itemStack.getType());
            assertEquals(material.getItemData(),itemStack.getDurability());
        }
        assertEquals("NBT Values marked as noNBT received nbt tags anyway - "+noNBTButPassed,0,noNBTButPassed.size());
        assertEquals("NBT Values not marked as noNBT failed - "+notNoNBTButFailed,0,notNoNBTButFailed.size());

    }

    public void testMatching() {
        int duraDependent = 0;
        for(EMaterial material : EMaterial.valuesThisVersion()) {
            ItemStack itemStack = new ItemBuilder(material).build();
            EMaterial match = EMaterial.matchMaterial(itemStack);
            checkMatch(match,material);
            if(material.isDurabilityDependent()) {
                duraDependent++;
            } else {
                itemStack.setDurability((short) 32);
                match = EMaterial.matchMaterial(itemStack);
                checkMatch(match,material);
            }
        }
        assertNotSame(EMaterial.values().length,duraDependent);
    }

    private void checkMatch(EMaterial match, EMaterial material) {
        assertTrue("Expected '"+material+"' but received '"+match+"'",material.equals(match) ||
                material.getDuplicates().contains(match));
    }

    public void testMatching2() {
        for(EMaterial material : EMaterial.values()) {
            assertEquals(material,EMaterial.matchMaterialAnyVersion(material.name()));
        }
    }

    public void testMatching3() {
        for(EMaterial material : EMaterial.values()) {
            assertEquals(material,EMaterial.matchMaterial(material.getId()));
        }
    }

    public void testExist() {
        Set<Material> materialSet = new HashSet<>(Arrays.asList(Material.values()));
        for(EMaterial material : EMaterial.valuesThisVersion()) {
            materialSet.remove(material.getBukkitMaterial());
        }

        assertEquals("Material's exist - "+materialSet,0,materialSet.size());
    }

    public void testVersion() {
        for(EMaterial material : EMaterial.values()) {
            assertEquals(material.isCompatible(),material.isCompatible(plugin.getEServer().getServerVersion()));
            if(material.isCompatible()) {
                new ItemBuilder(material).build();
            } else {
                try {
                    new ItemBuilder(material).build();
                    fail("Incompatible Material '"+material+"' built anyway!");
                } catch (UnsupportedVersionException ignored) {
                }
            }
        }
    }

    public void testVersion2() {
        assertEquals(EMaterial.valuesThisVersion(),EMaterial.values(plugin.getEServer().getServerVersion()));
        Collection<EMaterial> values = EMaterial.valuesThisVersion();
        for(EMaterial material : EMaterial.values()) {
            if(material.isCompatible(plugin.getEServer().getServerVersion())) {
                assertTrue(values.contains(material));
            }
        }
    }

    private void printNotInitialised() {
        plugin.getPluginLogger().warn("--------------");
        plugin.getPluginLogger().warn("EMaterial did not initialise properly!");
        plugin.getPluginLogger().warn("This may cause serious errors with the Item API");
        plugin.getPluginLogger().warn("--------------");
    }

    @Override
    public String getName() {
        return "EMaterial Test";
    }
}
