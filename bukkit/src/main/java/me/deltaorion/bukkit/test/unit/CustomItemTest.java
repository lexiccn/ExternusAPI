package me.deltaorion.bukkit.test.unit;

import de.tr7zw.nbtapi.NBTCompound;
import me.deltaorion.bukkit.plugin.plugin.BukkitAPIDepends;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.inventory.InventoryWrapper;
import me.deltaorion.bukkit.item.inventory.InventoryWrappers;
import me.deltaorion.bukkit.item.position.HumanEntityItem;
import me.deltaorion.bukkit.item.position.InventoryItem;
import me.deltaorion.bukkit.item.position.LivingEntityItem;
import me.deltaorion.bukkit.item.position.SlotType;
import me.deltaorion.bukkit.test.bukkit.TestEvent;
import me.deltaorion.bukkit.test.bukkit.TestLivingEntity;
import me.deltaorion.bukkit.test.bukkit.TestPlayer;
import me.deltaorion.common.test.generic.McTest;
import me.deltaorion.common.test.generic.MinecraftTest;
import me.deltaorion.bukkit.test.item.*;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

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
        if(!plugin.getDependency(BukkitAPIDepends.NBTAPI).isActive())
            return;
        CustomItem item = new BowSword(plugin);
        CustomItem enchant = new ParryEnchant();
        ItemStack generated = item.newCustomItem();
        ItemStack generated2 = item.newCustomItem(new ItemBuilder(EMaterial.WHITE_TERRACOTTA).build());
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
        if(!plugin.getDependency(BukkitAPIDepends.NBTAPI).isActive())
            return;
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
        if(!plugin.getDependency(BukkitAPIDepends.NBTAPI).isActive())
            return;
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

        TestPlayer jerry = new TestPlayer("Jerry");
        Player player = null;
        try {
            player = jerry.asPlayer();
        } catch (IllegalArgumentException e) {
            plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
            return;
        }
        plugin.getServer().getPluginManager().callEvent(new TestEvent(player,"Gamer"));
        assertEquals(0,helper.size());

        player.getInventory().setItemInHand(item.newCustomItem());
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
        TestPlayer jerry = new TestPlayer("Jerry");
        Player player = getPlayer();
        if(player==null)
            return;
        player.getInventory().setItemInHand(new ItemStack(Material.NAME_TAG));
        InventoryItem item = new HumanEntityItem(player,player.getInventory().getHeldItemSlot());
        assertEquals(SlotType.MAIN_HAND,item.getSlotType());
        assertEquals(player.getInventory().getHeldItemSlot(),item.getRawSlot());
        item.setItem(null);
        assertEquals(SlotType.MAIN_HAND,item.getSlotType());
        assertTrue(player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR));
        assertNull(item.getItemStack());
        item.setItem(new ItemStack(Material.EMERALD));
        assertEquals(Material.EMERALD,item.getItemStack().getType());
        TestLivingEntity testEntity = new TestLivingEntity();
        LivingEntity entity = testEntity.asLivingEntity();
        entity.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        item = new LivingEntityItem(entity,SlotType.LEGGINGS,entity.getEquipment().getLeggings());
        assertEquals(SlotType.LEGGINGS,item.getSlotType());
        assertEquals(SlotType.LEGGINGS.getSlot(),item.getRawSlot());
        assertEquals(Material.DIAMOND_LEGGINGS,item.getItemStack().getType());
        item.setItem(null);
        assertTrue(entity.getEquipment().getLeggings() == null || entity.getEquipment().getLeggings().getType().equals(Material.AIR));
        assertNull(item.getItemStack());
        item.setItem(new ItemStack(Material.EMERALD));
        assertEquals(Material.EMERALD,entity.getEquipment().getLeggings().getType());;
    }

    @McTest
    public void inventoryWrapperTest() {
        testLivingEntity();
        testHumanEntity();
    }

    private void testLivingEntity() {
        LivingEntity entity = getLivingEntity();
        if(entity==null)
            return;
        InventoryWrapper wrapper = InventoryWrappers.FROM_VERSION(plugin.getEServer().getServerVersion(),entity);
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkAir(wrapper.getMainHand().getItemStack());

        InventoryItem mainHand = wrapper.getMainHand();
        mainHand.setItem(new ItemBuilder(EMaterial.ACACIA_DOOR).build());
        checkArr(1,wrapper.getInventory());
        checkArr(1,wrapper.getHotBar());
        assertNotNull(wrapper.getMainHand());
        wrapper.fromSlot(SlotType.CHESTPLATE).setItem(new ItemBuilder(EMaterial.DIAMOND_SWORD).build());
        checkArr(2,wrapper.getInventory());
        checkArr(1,wrapper.getArmor());

        entity.getEquipment().clear();

        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkAir(wrapper.getMainHand().getItemStack());

        for(InventoryItem item : wrapper.getArmor()) {
            item.setItem(new ItemBuilder(EMaterial.ITEM_FRAME).build());
        }
        try {
            wrapper.fromSlot(SlotType.OTHER);
            fail();
        } catch (UnsupportedOperationException ignored) {

        }
        checkArr(4,wrapper.getArmor());
        checkArr(4,wrapper.getInventory());
        checkArr(0,wrapper.getHotBar());
        if(plugin.getEServer().getServerVersion().getMajor()>=SlotType.OFF_HAND.getRelease()) {
            checkAir(wrapper.getOffHand().getItemStack());
            entity.getEquipment().setItemInOffHand(new ItemBuilder(EMaterial.ITEM_FRAME).build());
            assertNotNull(wrapper.getOffHand().getItemStack());
        }
    }

    private void checkAir(ItemStack itemStack) {
        assertTrue(itemStack == null || itemStack.getType().equals(Material.AIR));
    }

    private void checkArr(int i, InventoryItem[] inventory) {
        int nonNulls = 0;
        for(InventoryItem item : inventory) {
            if(!(item.getItemStack()==null || item.getItemStack().getType().equals(Material.AIR)))
                nonNulls++;
        }

        assertEquals(i,nonNulls);
    }

    private void testHumanEntity() {
        Player player = getPlayer();
        if(player==null)
            return;
        InventoryWrapper wrapper = InventoryWrappers.FROM_VERSION(plugin.getEServer().getServerVersion(),player);
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkAir(wrapper.getMainHand().getItemStack());
        InventoryItem mainHand = wrapper.getMainHand();
        mainHand.setItem(new ItemBuilder(EMaterial.ACACIA_DOOR).build());
        checkArr(1,wrapper.getInventory());
        checkArr(1,wrapper.getHotBar());
        assertNotNull(wrapper.getMainHand());
        wrapper.fromSlot(SlotType.CHESTPLATE).setItem(new ItemBuilder(EMaterial.DIAMOND_SWORD).build());
        checkArr(2,wrapper.getInventory());
        checkArr(1,wrapper.getArmor());
        player.getInventory().addItem(new ItemBuilder(EMaterial.LIGHT_BLUE_WOOL).build());
        checkArr(3,wrapper.getInventory());
        checkArr(1,wrapper.getArmor());

        player.getInventory().clear();

        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkArr(0,wrapper.getInventory());
        checkAir(wrapper.getMainHand().getItemStack());

        for(InventoryItem item : wrapper.getArmor()) {
            item.setItem(new ItemBuilder(EMaterial.ITEM_FRAME).build());
        }
        try {
            wrapper.fromSlot(SlotType.OTHER);
            fail();
        } catch (UnsupportedOperationException ignored) {

        }
        checkArr(4,wrapper.getArmor());
        checkArr(4,wrapper.getInventory());
        checkArr(0,wrapper.getHotBar());
        if(plugin.getEServer().getServerVersion().getMajor()>=SlotType.OFF_HAND.getRelease()) {
            checkAir(wrapper.getOffHand().getItemStack());
            player.getInventory().setItemInOffHand(new ItemBuilder(EMaterial.ITEM_FRAME).build());
            assertNotNull(wrapper.getOffHand().getItemStack());
        }
    }

    @Nullable
    private Player getPlayer() {
        TestPlayer jerry = new TestPlayer("Jerry");
        Player player = null;
        try {
            return jerry.asPlayer();
        } catch (IllegalArgumentException e) {
            handle(e);
            return null;
        }
    }

    @Nullable
    private LivingEntity getLivingEntity() {
        try {
            return new TestLivingEntity().asLivingEntity();
        } catch (IllegalArgumentException e) {
            handle(e);
            return null;
        }
    }

    private void handle(IllegalArgumentException e) {
        plugin.getPluginLogger().warn("Cannot complete '"+getName()+"' as the Player class is malformed in this version. '"+e.getMessage()+"'");
    }

    @Override
    public String getName() {
        return "Custom Item Test";
    }
}
