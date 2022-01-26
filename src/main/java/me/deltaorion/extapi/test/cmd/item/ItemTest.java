package me.deltaorion.extapi.test.cmd.item;

import de.tr7zw.nbtapi.NBTItem;
import me.deltaorion.extapi.command.CommandException;
import me.deltaorion.extapi.command.FunctionalCommand;
import me.deltaorion.extapi.command.sent.SentCommand;
import me.deltaorion.extapi.common.plugin.BukkitPlugin;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ItemTest extends FunctionalCommand {

    private final BukkitPlugin plugin;

    public ItemTest(BukkitPlugin plugin) {
        super(NO_PERMISSION, NO_USAGE);
        this.plugin = plugin;
    }

    @Override
    public void commandLogic(SentCommand command) throws CommandException {
        if(command.getSender().isConsole())
            throw new CommandException("Test as player");

        Player player = plugin.getServer().getPlayer(command.getSender().getUniqueId());

        if(command.getArgOrBlank(0).asString().equalsIgnoreCase("read")) {
            ItemStack itemStack = player.getItemInHand();
            if(itemStack==null)
                return;

            String tag = command.getArgOrDefault(1,"Gamer").asString();
            command.getSender().sendMessage("Tag "+tag+": " + new ItemBuilder(itemStack).getTagValue(tag));
            return;
        }

        ItemStack testAdd = new ItemBuilder(EMaterial.DIAMOND_CHESTPLATE)
                .setAmount(1)
                .setAmount(1)
                .setDurability(200)
                .setDurability(300)
                .setType(Material.DIAMOND_LEGGINGS)
                .setType(Material.DIAMOND_LEGGINGS)
                //.setUnbreakable(true)
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

        ItemStack potionSimple = new ItemBuilder(EMaterial.POTION)
                .potion(potionBuilder -> {
                    potionBuilder.setColor(PotionType.FIRE_RESISTANCE)
                            .setSplash(true);
                }).build();

        ItemStack potion = new ItemBuilder(EMaterial.POTION)
                .addLoreLine("Glass Cannon")
                .potion(potionBuilder -> {
                    potionBuilder.setColor(PotionType.FIRE_RESISTANCE)
                            .addEffect(new PotionEffect(PotionEffectType.POISON,100,5))
                            .addEffect(new PotionEffect(PotionEffectType.SPEED,100,10));
                }).build();

        ItemStack skull = new ItemBuilder(EMaterial.MOB_HEAD)
                .setAmount(3)
                .setAmount(5)
                .setUnstackable()
                .addTag("Gamer","DeltaOrion")
                .skull(skullBuilder -> {
                    skullBuilder.setType(SkullType.CREEPER);
                }).build();

        ItemStack skullNothing = new ItemBuilder(EMaterial.SPAWN_WITCH_EGG)
                .skull( skullBuilder -> {})
                .build();

        ItemStack skull2 = new ItemBuilder(EMaterial.MOB_HEAD)
                .skull(skullBuilder -> {
                    skullBuilder.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjhkNDA4ODQyZTc2YTVhNDU0ZGMxYzdlOWFjNWMxYThhYzNmNGFkMzRkNjk3M2I1Mjc1NDkxZGZmOGM1YzI1MSJ9fX0=");
                }).build();

        ItemStack potionClear = new ItemBuilder(EMaterial.POTION)
                .potion(potionBuilder -> {
                    potionBuilder.setSplash(true)
                            .setColor(PotionType.INSTANT_DAMAGE)
                            .addEffect(new PotionEffect(PotionEffectType.BLINDNESS,5,3))
                            .addEffect(new PotionEffect(PotionEffectType.POISON,10,6))
                            .clearEffects()
                            .addEffect(new PotionEffect(PotionEffectType.CONFUSION,5,1));
                }).build();


        potionClear = new ItemBuilder(potionClear)
                .potion(potionBuilder -> {
                    potionBuilder.clearEffects()
                            .addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,1));
                }).build();

        ItemStack potionNothing = new ItemBuilder(EMaterial.NETHER_WART_ITEM)
                .potion(potionBuilder -> {})
                .build();

        ItemStack hiddenEnchant = new ItemBuilder(EMaterial.SPAWN_WITCH_EGG)
                .addHiddenEnchant().build();

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

        ItemStack NBTTest = new ItemBuilder(EMaterial.SUGAR_CANE_ITEM)
                .addTag("Gamer", "This should remain after restart!")
                .addTag("This","This should also remain")
                .build();


        player.getInventory().addItem(testAdd);
        player.getInventory().addItem(skull);
        player.getInventory().addItem(skull2);
        player.getInventory().addItem(potion);
        player.getInventory().addItem(potionSimple);
        player.getInventory().addItem(potionClear);
        player.getInventory().addItem(hiddenEnchant);
        player.getInventory().addItem(skullNothing);
        player.getInventory().addItem(potionNothing);
        player.getInventory().addItem(stripTest);
        player.getInventory().addItem(NBTTest);

    }
}
