package me.deltaorion.extapi.test.unit;

import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.junit.Assert.*;

public class EMaterialTest implements MinecraftTest {

    @McTest
    public void testMaterial() {
        for(EMaterial material : EMaterial.values()) {
            if(material.rawGasLiquid())
                return;

            ItemBuilder builder = new ItemBuilder(material)
                    .addTag("Gamer","Gamer");
            ItemStack itemStack = builder.build();
            Inventory inventory = Bukkit.createInventory(null,54);
            inventory.addItem(itemStack);
            inventory.clear();
            assertEquals(material.getBukkitMaterial(),itemStack.getType());
        }
    }
}
