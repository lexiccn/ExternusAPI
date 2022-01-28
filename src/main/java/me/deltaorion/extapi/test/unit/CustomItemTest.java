package me.deltaorion.extapi.test.unit;

import de.tr7zw.nbtapi.NBTCompound;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemException;
import me.deltaorion.extapi.item.position.HumanEntityItem;
import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.item.position.LivingEntityItem;
import me.deltaorion.extapi.item.position.SlotType;
import me.deltaorion.extapi.test.unit.bukkit.TestEvent;
import me.deltaorion.extapi.test.unit.bukkit.TestLivingEntity;
import me.deltaorion.extapi.test.unit.bukkit.TestPlayer;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import me.deltaorion.extapi.test.unit.item.*;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class CustomItemTest implements MinecraftTest {

    private final BukkitPlugin plugin;

    public CustomItemTest(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void customItemTest() {
        CustomItem item = new BowSword(plugin);
        CustomItem enchant = new ParryEnchant();
        ItemStack generated = item.newCustomItem();
        ItemStack generated2 = item.newCustomItem(new ItemStack(Material.STAINED_CLAY));
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
        CustomItem everythingEnchant = new EverythingItem(plugin);
        ItemStack itemStack = everythingEnchant.newCustomItem(Locale.ENGLISH);
        assertEquals("Hello World",itemStack.getItemMeta().getDisplayName());
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

        try {
            plugin.getCustomItemManager().registerItem(item);
            fail();
        } catch (IllegalArgumentException ignored) {

        }

        plugin.getCustomItemManager().registerIfAbsent(item);

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

        helper.clear();
        player.getInventory().clear();
        player.getInventory().addItem(item.newCustomItem());
        player.getInventory().addItem(item.newCustomItem());
        player.getInventory().addItem(item.newCustomItem());
        player.getInventory().addItem(item.newCustomItem());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"count"));
        assertEquals(4,helper.size());

        helper.clear();
        player.getInventory().clear();
        player.getInventory().setLeggings(item.newCustomItem());
        player.getInventory().setBoots(item.newCustomItem());
        player.getInventory().setChestplate(item.newCustomItem());
        player.getInventory().setHelmet(item.newCustomItem());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"armor"));
        assertEquals(4,helper.size());

        helper.clear();
        player.getInventory().clear();
        player.getInventory().setItem(13,item.newCustomItem());
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(1,helper.size());
    }

    @McTest
    public void inventoryPositionTest() {
        Player player = new TestPlayer("Jerry");
        player.getInventory().setItemInHand(new ItemStack(Material.NAME_TAG));
        InventoryItem item = new HumanEntityItem(player,player.getInventory().getHeldItemSlot(),player.getItemInHand());
        assertEquals(SlotType.MAIN_HAND,item.getSlotType());
        assertEquals(player.getInventory().getHeldItemSlot(),item.getRawSlot());
        item.setItem(null);
        assertEquals(SlotType.MAIN_HAND,item.getSlotType());
        assertTrue(player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR));
        assertNull(item.getItemStack());
        item.setItem(new ItemStack(Material.EMERALD));
        assertEquals(Material.EMERALD,item.getItemStack().getType());
        LivingEntity entity = new TestLivingEntity();
        entity.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        item = new LivingEntityItem(entity,SlotType.LEGGINGS,entity.getEquipment().getLeggings());
        assertEquals(SlotType.LEGGINGS,item.getSlotType());
        assertEquals(SlotType.LEGGINGS.getSlot(),item.getRawSlot());
        assertEquals(Material.DIAMOND_LEGGINGS,item.getItemStack().getType());
        item.setItem(null);
        assertTrue(entity.getEquipment().getLeggings() == null || entity.getEquipment().getLeggings().getType().equals(Material.AIR));
        assertNull(item.getItemStack());
        item.setItem(new ItemStack(Material.EMERALD));
        assertEquals(Material.EMERALD,entity.getEquipment().getLeggings().getType());
        ;

    }
}
