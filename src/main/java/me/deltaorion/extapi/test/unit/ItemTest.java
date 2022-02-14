package me.deltaorion.extapi.test.unit;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.common.plugin.ApiPlugin;
import me.deltaorion.extapi.common.plugin.BukkitAPIDepends;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.test.unit.generic.McTest;
import me.deltaorion.extapi.test.unit.generic.MinecraftTest;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.Assert.*;

public class ItemTest implements MinecraftTest {

    private final ApiPlugin plugin;

    public ItemTest(ApiPlugin plugin) {
        this.plugin = plugin;
    }

    @McTest
    public void itemTest() {
        //basic test
        if(!plugin.getDependency(BukkitAPIDepends.NBTAPI.name()).isActive()) {
            plugin.getPluginLogger().warn("------------------------");
            plugin.getPluginLogger().warn("Externus API WARNING");
            plugin.getPluginLogger().warn("NBT API is not enabled");
            plugin.getPluginLogger().warn("This may cause various errors when interacting with ItemBuilders.");
            plugin.getPluginLogger().warn("Adding or removing NBT tags will not work.");
            plugin.getPluginLogger().warn("");
            plugin.getPluginLogger().warn("If NO Item API or NBT functionality is needed then ignore this warning");
            plugin.getPluginLogger().warn("Otherwise please install the NBT API here - https://www.spigotmc.org/resources/nbt-api.7939/");
            plugin.getPluginLogger().warn("------------------------");
            return;
        }

        ItemStack testAdd = new ItemBuilder(EMaterial.DIAMOND_CHESTPLATE)
                .setAmount(1)
                .setAmount(1)
                .setDurability(200)
                .setDurability(300)
                .setType(Material.DIAMOND_LEGGINGS)
                .setType(Material.DIAMOND_LEGGINGS)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .addEnchantment(Enchantment.DEPTH_STRIDER,7)
                .addEnchantment(Enchantment.DEPTH_STRIDER,7)
                .setLore("Hello","World")
                .setLore("Hello","World")
                .addLoreLine("Line 3")
                .addLoreLine(Message.valueOfTranslatable("hello-arg"), Locale.ENGLISH,"Hello")
                .addTag("Gamer","DeltaOrion")
                .setUnstackable()
                .setDisplayName("Title")
                .build();

        assertEquals(1,testAdd.getAmount());
        assertEquals((short) 300,testAdd.getDurability());
        assertEquals(Material.DIAMOND_LEGGINGS,testAdd.getType());
        assertTrue(testAdd.getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        assertEquals(7,testAdd.getItemMeta().getEnchantLevel(Enchantment.DEPTH_STRIDER));
        assertEquals(testAdd.getItemMeta().getLore(), ImmutableList.of("Hello","World","Line 3","Hello Hello"));
        assertEquals("Title",testAdd.getItemMeta().getDisplayName());

        ItemBuilder builder = new ItemBuilder(testAdd);
        assertEquals("DeltaOrion",builder.getTagValue("Gamer"));
        assertTrue(builder.hasTag("Gamer"));
        assertTrue(builder.isUnstackable());

        ItemBuilder airBuilder = new ItemBuilder(EMaterial.AIR)
                .addTag("Gamer","Gamer")
                .removeTag("Gamer")
                .removeTag("This tag is not on the item");

        try {
            airBuilder.setAmount(-1)
                    .build();

            fail();
        } catch (IllegalArgumentException ignored) {

        }

        //ensure skull doesnt lose properties
        ItemStack skull = new ItemBuilder(EMaterial.PLAYER_HEAD)
                .setAmount(3)
                .setAmount(5)
                .setUnstackable()
                .addTag("Gamer","DeltaOrion")
                .skull(skullBuilder -> {
                    skullBuilder.setType(SkullType.CREEPER);
                }).build();

        assertEquals(Material.SKULL_ITEM,skull.getType());
        assertEquals(skull.getDurability(),SkullType.CREEPER.ordinal());
        assertEquals(5,skull.getAmount());

        ItemBuilder skullBuilder = new ItemBuilder(skull);

        assertTrue(skullBuilder.isUnstackable());
        assertTrue(skullBuilder.hasTag("Gamer"));

        ItemStack unbreakable = new ItemBuilder(EMaterial.NETHER_BRICK)
                .setUnbreakable(true)
                .setUnstackable()
                .allFlags()
                .addEnchantment(Enchantment.KNOCKBACK,5)
                .addEnchantment(Enchantment.KNOCKBACK,4)
                .build();

        assertTrue(unbreakable.getItemMeta().spigot().isUnbreakable());
        assertTrue(unbreakable.getItemMeta().getItemFlags().equals(new HashSet<>(Arrays.asList(ItemFlag.values()))));
        assertEquals(4,unbreakable.getEnchantmentLevel(Enchantment.KNOCKBACK));

        ItemBuilder unbreakBuilder = new ItemBuilder(unbreakable);
        assertTrue(unbreakBuilder.isUnstackable());

        ItemStack potion = new ItemBuilder(EMaterial.POTION)
                .addLoreLine("Glass Cannon")
                .potion(potionBuilder -> {
                    potionBuilder.setColor(PotionType.JUMP)
                            .addEffect(new PotionEffect(PotionEffectType.POISON,100,5))
                            .addEffect(new PotionEffect(PotionEffectType.SPEED,100,10));
                }).build();

        assertEquals(Material.POTION,potion.getType());
        assertEquals(5,((PotionMeta) potion.getItemMeta()).getCustomEffects().get(0).getAmplifier());
        assertEquals(10,((PotionMeta) potion.getItemMeta()).getCustomEffects().get(1).getAmplifier());
        assertEquals(ImmutableList.of("Glass Cannon"),potion.getItemMeta().getLore());

        ItemStack potionClear = new ItemBuilder(EMaterial.POTION)
                .potion(potionBuilder -> {
                    potionBuilder.setSplash(true)
                            .setColor(PotionType.INSTANT_DAMAGE)
                            .addEffect(new PotionEffect(PotionEffectType.BLINDNESS,5,3))
                            .addEffect(new PotionEffect(PotionEffectType.POISON,10,6))
                            .clearEffects()
                            .addEffect(new PotionEffect(PotionEffectType.CONFUSION,5,1));
                }).build();

        assertEquals(PotionEffectType.CONFUSION,((PotionMeta) potionClear.getItemMeta()).getCustomEffects().get(0).getType());

        potionClear = new ItemBuilder(potionClear)
                .potion(potionBuilder -> {
                    potionBuilder.clearEffects()
                            .addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,1));
                }).build();

        assertEquals(PotionEffectType.DAMAGE_RESISTANCE,((PotionMeta) potionClear.getItemMeta()).getCustomEffects().get(0).getType());

        ItemStack hiddenEnchant = new ItemBuilder(EMaterial.WITCH_SPAWN_EGG)
                .addHiddenEnchant().build();

        assertTrue(hiddenEnchant.getItemMeta().getEnchants().containsKey(Enchantment.DAMAGE_ARTHROPODS));

        hiddenEnchant = new ItemBuilder(hiddenEnchant).removeHiddenEnchant().build();

        assertFalse(hiddenEnchant.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS));
        assertFalse(hiddenEnchant.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS));

        hiddenEnchant = new ItemBuilder(hiddenEnchant)
                .addEnchantment(Enchantment.DAMAGE_ARTHROPODS,4)
                .addFlags(ItemFlag.HIDE_ENCHANTS)
                .removeHiddenEnchant().build();

        assertTrue(hiddenEnchant.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS));
        assertEquals(4,hiddenEnchant.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ARTHROPODS));

        ItemStack skullNothing = new ItemBuilder(EMaterial.WITCH_SPAWN_EGG)
                .skull( skullBuilder2 -> {})
                .build();

        assertEquals(SkullType.PLAYER.ordinal(),skullNothing.getDurability());

        ItemStack potionNothing = new ItemBuilder(EMaterial.WITCH_SPAWN_EGG)
                .potion(potionBuilder -> {})
                .build();

        ItemStack stripTest = new ItemBuilder(EMaterial.RED_WOOL)
                .addFlags(ItemFlag.HIDE_POTION_EFFECTS)
                .removeFlags(ItemFlag.HIDE_POTION_EFFECTS)
                .removeFlags(ItemFlag.HIDE_ENCHANTS)
                .addHiddenEnchant()
                .removeHiddenEnchant()
                .addLoreLine("Gamer")
                .setLore("Hallo")
                .setDisplayName("Gamer")
                .clearLore()
                .clearLore()
                .addEnchantment(Enchantment.DEPTH_STRIDER,3)
                .removeEnchantment(Enchantment.ARROW_DAMAGE)
                .clearEnchantments()
                .hideAll()
                .clearFlags()
                .removeDisplayName()
                .build();

        assertEquals(0,stripTest.getEnchantments().size());
        assertNull(stripTest.getItemMeta().getLore());
        assertEquals(0,stripTest.getItemMeta().getItemFlags().size());
        assertFalse(stripTest.getItemMeta().hasDisplayName());

        ItemStack nbtStrip = new ItemBuilder(EMaterial.DIAMOND)
                .removeTag("RandomTagExtAPI")
                .addTag("DeltaOrion","100")
                .removeTag("DeltaOrion")
                .build();

        assertFalse(new ItemBuilder(nbtStrip).hasTag("DeltaOrion"));
        assertNull(new ItemBuilder(nbtStrip).getTagValue("eriojeogjre"));


    }

    @Override
    public String getName() {
        return "ItemBuilder Test";
    }
}
