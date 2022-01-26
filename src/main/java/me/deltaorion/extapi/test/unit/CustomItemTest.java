package me.deltaorion.extapi.test.unit;

import de.tr7zw.nbtapi.NBTCompound;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.test.unit.bukkit.TestEvent;
import me.deltaorion.extapi.test.unit.bukkit.TestPlayer;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import me.deltaorion.extapi.test.unit.item.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class CustomItemTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public CustomItemTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void customItemTest() {
        CustomItem item = new BowSword();
        CustomItem enchant = new ParryEnchant();
        ItemStack generated = item.newCustomItem();
        ItemStack generated2 = item.newCustomItem(new ItemStack(Material.STAINED_CLAY));
        TestItem t = new TestItem(new ArrayList<>());
        ItemStack trickery = new ItemBuilder(Material.DIAMOND_SWORD)
                .transformNBT(nbtItem -> {
                    NBTCompound compound = nbtItem.getCompound("EXTAPI_CUSTOM_ITEM");
                    if(compound==null) {
                        compound = nbtItem.addCompound("EXTAPI_CUSTOM_ITEM");
                    }
                    compound.setString("Bow_Sword","bow_sword");
                }).build();
        ItemStack abc = new ItemStack(Material.DIAMOND_SWORD);
        assertTrue(item.isCustomItem(generated));
        assertTrue(item.isCustomItem(generated2));
        assertFalse(item.isCustomItem(trickery));
        assertFalse(item.isCustomItem(abc));
        assertFalse(item.isCustomItem(null));
        assertFalse(item.isCustomItem(new ItemStack(Material.AIR)));
        item.makeCustom(generated);
        assertTrue(item.isCustomItem(generated));
        enchant.makeCustom(generated);
        assertTrue(enchant.isCustomItem(generated));
        assertTrue(item.isCustomItem(generated));
        enchant.removeCustom(generated);
        assertFalse(enchant.isCustomItem(generated));
        item.removeCustom(generated);
        assertFalse(item.isCustomItem(generated));
        try {
            enchant.newCustomItem();
            fail();
        } catch (IllegalStateException ignored) {

        }
        //TODO -  test locale
    }

    @McTest
    public void registerFailTest() {
        plugin.getLogger().info("The following error messages are as a result of tests");
        plugin.getCustomItemManager().registerItem(new FailItem());
        plugin.getCustomItemManager().registerItem(new FailItem2());
        plugin.getCustomItemManager().registerItem(new FailItem3());
        plugin.getCustomItemManager().registerItem(new PassItem());

        if(plugin.getCustomItemManager().isRegistered(new FailItem()) ||
            plugin.getCustomItemManager().isRegistered(new FailItem2()) ||
            plugin.getCustomItemManager().isRegistered(new FailItem3())
            ) {
            fail();
        }

        if(!plugin.getCustomItemManager().isRegistered(new PassItem()))
            fail();


    }

    @McTest
    public void registerTest() {
        List<String> helper = new ArrayList<>();
        TestItem item = new TestItem(helper);
        plugin.getCustomItemManager().registerItem(item);
        if(!plugin.getCustomItemManager().isRegistered(item))
            fail();

        Player player = new TestPlayer("Jerry");
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(0,helper.size());

        player.getInventory().setItemInHand(item.newCustomItem());
        System.out.println(player.getInventory().getItemInHand());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(3,helper.size());

        helper.clear();
        player.getInventory().setChestplate(item.newCustomItem());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(4,helper.size());

        helper.clear();
        player.getInventory().clear();
        player.getInventory().setLeggings(item.newCustomItem());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(2,helper.size());
    }
}
