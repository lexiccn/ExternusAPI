package me.deltaorion.extapi.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.deltaorion.extapi.locale.message.Message;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    @NotNull private final ItemStack itemStack;

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack);
    }

    public ItemBuilder(@NotNull EMaterial material) {
        Objects.requireNonNull(material);
        this.itemStack = new ItemStack(material.getBukkitMaterial(),1,material.getItemData());
    }

    public ItemBuilder(@NotNull Material material) {
        Objects.requireNonNull(material);
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder setType(@NotNull EMaterial type) {
        this.itemStack.setType(type.getBukkitMaterial());
        this.itemStack.setDurability(type.getItemData());
        return this;
    }

    public ItemBuilder setType(@NotNull Material type) {
        this.itemStack.setType(type);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        Validate.isTrue(validStack(amount));
        this.itemStack.setAmount(amount);
        return this;
    }

    private boolean validStack(int amount) {
        if(amount < 0)
            return false;

        if(itemStack.getMaxStackSize()==-1)
            return true;

        return amount <= itemStack.getMaxStackSize();
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder transformMeta(@NotNull Consumer<ItemMeta> metaConsumer) {
        Objects.requireNonNull(metaConsumer);
        ItemMeta meta = itemStack.getItemMeta();
        metaConsumer.accept(meta);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLoreLine(@NotNull String lore) {
        Objects.requireNonNull(lore);
        return transformMeta( meta -> {
            List<String> l = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
            l.add(lore);
            meta.setLore(l);
        });
    }

    public ItemBuilder addLoreLine(@NotNull Message message, @NotNull Locale locale, Object... args) {
        Objects.requireNonNull(locale);
        Objects.requireNonNull(message);
        return addLoreLine(message.toString(locale,args));
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(@NotNull Iterable<String> lore) {
        Objects.requireNonNull(lore);
        return transformMeta( meta -> {
            List<String> l = new ArrayList<>();
            for(String str : lore) {
                l.add(str);
            }
            meta.setLore(l);
        });
    }

    public ItemBuilder clearLore() {
        return transformMeta(itemMeta -> {
            itemMeta.setLore(new ArrayList<>());
        });
    }

    public ItemBuilder setDisplayName(@NotNull Message displayName, @NotNull Locale locale, Object... args) {
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(locale);
        return transformMeta( itemMeta -> {
            itemMeta.setDisplayName(displayName.toString(locale,args));
        });
    }

    public ItemBuilder setDisplayName(@NotNull String displayName) {
        Objects.requireNonNull(displayName);
        return transformMeta(itemMeta -> {
            itemMeta.setDisplayName(displayName);
        });
    }

    public ItemBuilder removeDisplayName() {
        return transformMeta(itemMeta -> {
            itemMeta.setDisplayName(null);
        });
    }

    public ItemBuilder addFlags(@NotNull ItemFlag... flags) {
        Objects.requireNonNull(flags);
        return transformMeta(itemMeta -> {
            itemMeta.addItemFlags(flags);
        });
    }

    public ItemBuilder removeFlags(@NotNull ItemFlag... flags) {
        Objects.requireNonNull(flags);
        return transformMeta(itemMeta -> {
            itemMeta.removeItemFlags(flags);
        });
    }

    public ItemBuilder clearFlags() {
        return transformMeta(itemMeta -> {
            itemMeta.removeItemFlags(ItemFlag.values());
        });
    }

    public ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        Validate.isTrue(level >= 0);
        Objects.requireNonNull(enchantment);
        return transformMeta(itemMeta -> {
            itemMeta.addEnchant(enchantment,level,true);
        });
    }

    public ItemBuilder removeEnchantment(@NotNull Enchantment enchantment) {
        Objects.requireNonNull(enchantment);
        return transformMeta(itemMeta ->  {
            itemMeta.removeEnchant(enchantment);
        });
    }

    public ItemBuilder clearEnchantments() {
        return transformMeta(itemMeta -> {
            itemMeta.getEnchants().keySet().forEach(key -> {
                itemMeta.removeEnchant(key);
            });
        });
    }

    public ItemBuilder hideAll() {
        return addFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS);
    }

    public ItemBuilder showAll() {
        return removeFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS);
    }

    public ItemBuilder allFlags() {
        return addFlags(ItemFlag.values());
    }

    public ItemBuilder addHiddenEnchant() {
        if(itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)==0) {
                addEnchantment(Enchantment.DAMAGE_ARTHROPODS,1);
                addFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemBuilder removeHiddenEnchant() {
        if(itemStack.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS)) {
            if(itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS) == 1) {
                removeEnchantment(Enchantment.DAMAGE_ARTHROPODS);
                removeFlags(ItemFlag.HIDE_ENCHANTS);
            }
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return transformMeta(itemMeta -> {
            itemMeta.spigot().setUnbreakable(unbreakable);
        });
    }


    private final static String UNSTACKABLE = "EXTAPI_UNSTACKABLE";

    public ItemBuilder setUnstackable() {
        if(!hasTag(UNSTACKABLE)) {
            String uuid = UUID.randomUUID().toString();
            addTag(UNSTACKABLE,uuid);
        }
        return this;
    }

    public ItemBuilder removeUnstackableIfSet() {
        return removeTag(UNSTACKABLE);
    }

    public boolean isUnstackable() {
        return hasTag(UNSTACKABLE);
    }

    public ItemBuilder potion(@NotNull Consumer<PotionBuilder> consumer) {
        Objects.requireNonNull(consumer);
        PotionBuilder builder = new PotionBuilder();
        consumer.accept(builder);
        builder.build(this);
        return this;
    }

    public ItemBuilder skull(@NotNull Consumer<SkullBuilder> consumer) {
        Objects.requireNonNull(consumer);
        SkullBuilder builder = new SkullBuilder();
        consumer.accept(builder);
        builder.build(this);
        return this;
    }

    public ItemBuilder removeTag(@NotNull String key) {
        Validate.notNull(key,"Cannot remove null key");
        if(!isValidStack()) {
            return this;
        }

        NBTItem item = new NBTItem(itemStack);
        if(item.hasKey(key)) {
            item.removeKey(key);
        }
        itemStack.setItemMeta(item.getItem().getItemMeta());
        return this;
    }

    public ItemBuilder addTag(@NotNull String key, @NotNull String value) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if(!isValidStack()) {
            return this;
        }

        NBTItem item = new NBTItem(itemStack);
        item.setString(key, value);
        itemStack.setItemMeta(item.getItem().getItemMeta());
        return this;
    }

    public ItemStack build() {
        return this.itemStack;
    }

    @Nullable
    public String getTagValue(String tag) {

        if(!isValidStack()) {
            return null;
        }

        if(!hasTag(tag))
            return null;

        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getString(tag);
    }

    @Nullable
    public NBTCompound getCompound(String tag) {
        if(!isValidStack()) {
            return null;
        }

        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getCompound(tag);
    }

    public ItemBuilder transformNBT(@NotNull Consumer<NBTItem> itemConsumer) {
        Objects.requireNonNull(itemConsumer);
        NBTItem item = new NBTItem(itemStack);
        itemConsumer.accept(item);
        itemStack.setItemMeta(item.getItem().getItemMeta());
        return this;
    }

    public boolean hasTag(String tag) {
        if(!isValidStack()) {
            return false;
        }

        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey(tag);
    }

    private boolean isValidStack() {
        return !itemStack.getType().equals(Material.AIR);
    }

    public static class PotionBuilder {

        @NotNull
        private PotionType color = PotionType.WATER;
        private boolean splash = false;
        private boolean clear = false;
        @NotNull private Set<PotionEffect> effects;

        public PotionBuilder() {
            this.effects = new HashSet<>();
        }

        public PotionBuilder setSplash(boolean splash) {
            this.splash = splash;
            return this;
        }

        public PotionBuilder setColor(@NotNull PotionType color) {
            this.color = Objects.requireNonNull(color);
            return this;
        }

        public PotionBuilder addEffect(@NotNull PotionEffect effect) {
            this.effects.add(effect);
            return this;
        }

        public PotionBuilder clearEffects() {
            this.effects.clear();
            this.clear = true;
            return this;
        }

        private void build(ItemBuilder builder) {
            Potion potion = new Potion(color);
            potion.setSplash(splash);
            ItemStack potionStack = potion.toItemStack(1);
            builder.setType(potionStack.getType());
            builder.setDurability(potionStack.getDurability());
            builder.transformMeta(itemMeta -> {
                PotionMeta meta = (PotionMeta) itemMeta;
                if(clear) {
                    meta.clearCustomEffects();
                }
                for(PotionEffect effect : effects) {
                    meta.addCustomEffect(effect,true);
                }
            });
        }
    }

    public static class SkullBuilder {
        @Nullable private Plugin plugin;
        @Nullable private UUID playerUUID;
        @Nullable private String url;
        private SkullType type;
        private boolean isTexture;
        private final Material SKULL_MAT = Material.SKULL_ITEM;

        public SkullBuilder() {
            isTexture = false;
            url = null;
            playerUUID = null;
            type = SkullType.PLAYER;
        }

        public SkullBuilder setType(SkullType type) {
            this.type = type;
            return this;
        }

        public SkullBuilder setPlayer(@NotNull Plugin plugin, @NotNull UUID player) {
            this.playerUUID = Objects.requireNonNull(player);
            this.plugin = Objects.requireNonNull(plugin);
            this.isTexture = false;
            return this;
        }

        public SkullBuilder setTexture(@NotNull String url) {
            this.isTexture = true;
            this.url = Objects.requireNonNull(url);
            return this;
        }

        private void build(ItemBuilder builder) {
            if(isTexture && url != null) {
                applyTexture(builder);
            } else if(!isTexture && playerUUID!=null) {
                applyUUID(builder);
            } else {
                builder.setType(SKULL_MAT);
                builder.setDurability(type.ordinal());
            }
        }

        private void applyUUID(ItemBuilder builder) {

            ItemStack skull = new ItemStack(SKULL_MAT,1, (short) type.ordinal());
            //preserve meta
            skull.setItemMeta(builder.itemStack.getItemMeta());
            skull.setType(SKULL_MAT);
            skull.setDurability((short) type.ordinal());

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(Bukkit.getOfflinePlayer(playerUUID).getName());
            skull.setItemMeta(meta);
        }

        private void applyTexture(ItemBuilder builder) {
            ItemStack head = new ItemStack(SKULL_MAT,1, (short) type.ordinal());
            //preserve item meta
            head.setItemMeta(builder.itemStack.getItemMeta());
            head.setType(SKULL_MAT);
            head.setDurability((short) type.ordinal());

            //do relevant transform
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", url));
            Field profileField;
            try {
                profileField = headMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(headMeta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {

            }
            //steal the item meta and set here
            builder.itemStack.setItemMeta(headMeta);
            builder.setType(SKULL_MAT);
            builder.setDurability(type.ordinal());
        }
    }

}
