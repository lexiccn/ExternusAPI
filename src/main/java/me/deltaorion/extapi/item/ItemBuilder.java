package me.deltaorion.extapi.item;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.deltaorion.extapi.common.exception.UnsupportedVersionException;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.potion.PotionBuilder;
import me.deltaorion.extapi.item.potion.PotionBuilderFactory;
import me.deltaorion.extapi.item.potion.PotionBuilder_8;
import me.deltaorion.extapi.locale.message.Message;
import org.apache.commons.lang.Validate;
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
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

/**
 * An ItemBuilder is a data structure that allows for the easy and quick manipulation of an {@link ItemStack}. ItemStacks currently
 * do not have any easy creational interface and rely on a boilerplate code. This class attempts to fix this issue by wrapping
 * an itemstack. This class also allows for easy manipulation of, and editing of the NBT of an itemstack.
 *
 * Itemstacks are passed into this class by reference and can be manipulated by reference, meaning suppose you are editing an itemstack
 * in an inventory or in a collection. You can simply edit it here and not have to replace it in said container.
 */
public class ItemBuilder {

    @NotNull private final ItemStack itemStack;

    public ItemBuilder(@NotNull ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack);
    }

    /**
     * Creates an Itembuilder with the given universal EMaterial.
     *
     * @param material The material to use
     * @throws UnsupportedVersionException if the material is not available for this version.
     */
    public ItemBuilder(@NotNull EMaterial material) {
        Objects.requireNonNull(material);
        Material bukkitMaterial = material.getBukkitMaterial();
        if(bukkitMaterial==null)
            throw new UnsupportedVersionException(material);
        this.itemStack = new ItemStack(material.getBukkitMaterial(),1,material.getItemData());
    }

    public ItemBuilder(@NotNull Material material) {
        Objects.requireNonNull(material);
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(@NotNull CustomItem item, Locale locale) {
        Objects.requireNonNull(item);
        this.itemStack = item.newCustomItem(locale);
    }

    public ItemBuilder(@NotNull CustomItem item) {
        this(item, EServer.DEFAULT_LOCALE);
    }

    /**
     * Creates an ItemBuilder from a BASE Potion. This will create it from a potion that you could expect to see from the
     * creative menu.
     *
     * @param type The type of potion
     * @param potionForm what form the potion takes
     * @param upgraded Whether the potion is level I or II
     * @param extended Whether the potion's duration is extended
     * @throws UnsupportedVersionException If this version is not supported
     */
    public ItemBuilder(@NotNull PotionType type, PotionBuilder.Type potionForm, boolean upgraded, boolean extended) {
        this(PotionBuilderFactory.BY_VERSION.fromBase(type,potionForm,upgraded,extended));
    }

    /**
     * Sets the type using the Ematerial Enum
     *
     * @param type The ematerial to set.
     * @return This builder
     * @throws UnsupportedVersionException if the type does not exist for this version
     */
    public ItemBuilder setType(@NotNull EMaterial type) {
        Material bukkitMaterial = type.getBukkitMaterial();
        if(bukkitMaterial==null)
            throw new UnsupportedVersionException(type);
        this.itemStack.setType(type.getBukkitMaterial());
        this.itemStack.setDurability(type.getItemData());
        return this;
    }

    public ItemBuilder setType(@NotNull Material type) {
        this.itemStack.setType(type);
        return this;
    }

    @NotNull
    public EMaterial getType() {
        return Objects.requireNonNull(EMaterial.matchMaterial(itemStack),
                "Could not match material. Did EMaterial initialise correctly?");
    }

    /**
     * Sets the quantity of the itemstack. The quantity must be above 0.
     *
     * @param amount the amount to set to
     * @return the current itembuilder
     * @throws IllegalArgumentException if the quantity is 0 or less
     */
    public ItemBuilder setAmount(int amount) {
        Validate.isTrue(amount>0,"Itemstack quantity must be above 0");
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

    public ItemBuilder addFlags(@NotNull Iterable<ItemFlag> flags) {
        Objects.requireNonNull(flags);
        return transformMeta(itemMeta -> {
            for(ItemFlag flag : flags) {
                itemMeta.addItemFlags(flag);
            }
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
            if(EMaterial.getVersion()==null ||EMaterial.getVersion().getMajor()<12) {
                itemMeta.spigot().setUnbreakable(unbreakable);
            } else {
                itemMeta.setUnbreakable(unbreakable);
            }
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
        PotionBuilder builder = PotionBuilderFactory.BY_VERSION.get(this);
        consumer.accept(builder);
        return this;
    }

    public ItemBuilder skull(@NotNull Consumer<SkullBuilder> consumer) {
        Objects.requireNonNull(consumer);
        SkullBuilder builder = new SkullBuilder();
        consumer.accept(builder);
        builder.build(this);
        return this;
    }

    public ItemBuilder makeCustom(@NotNull CustomItem item) {
        item.makeCustom(itemStack);
        return this;
    }

    public ItemBuilder removeCustom(@NotNull CustomItem item) {
        item.removeCustom(itemStack);
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ItemStack",itemStack)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ItemStack) {
            return this.itemStack.equals(o);
        } else if(o instanceof ItemBuilder) {
            return ((ItemBuilder) o).itemStack.equals(this.itemStack);
        } else {
            return false;
        }
    }

    /**
     * Returns a built itemstack. If an itemstack of some kind was passed into this, then you will not get a cloned
     * itemstack, just the original. For a clone use {@link ItemStack#clone()}
     *
     * @return the itemstack
     */
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

    public static class SkullBuilder {
        @Nullable private Plugin plugin;
        @Nullable private UUID playerUUID;
        @Nullable private String url;
        private boolean isTexture;
        private EMaterial skullMat;
        private final static EMaterial DEFAULT_SKULL_MAT = EMaterial.PLAYER_HEAD;

        public SkullBuilder() {
            isTexture = false;
            url = null;
            playerUUID = null;
            skullMat = DEFAULT_SKULL_MAT;
        }

        public SkullBuilder setType(SkullType type) {
            switch (type) {
                case WITHER:
                    skullMat = EMaterial.WITHER_SKELETON_SKULL;
                    break;
                case ZOMBIE:
                    skullMat = EMaterial.ZOMBIE_HEAD;
                    break;
                case CREEPER:
                    skullMat = EMaterial.CREEPER_HEAD;
                    break;
                case SKELETON:
                    skullMat = EMaterial.SKELETON_SKULL;
                    break;
                default:
                    skullMat = EMaterial.PLAYER_HEAD;
            }
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
                builder.setType(skullMat);
            }
        }

        private void applyUUID(ItemBuilder builder) {

            ItemStack skull = new ItemBuilder(DEFAULT_SKULL_MAT).build();
            //preserve meta
            skull.setItemMeta(builder.itemStack.getItemMeta());

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(Objects.requireNonNull(plugin).getServer().getOfflinePlayer(playerUUID).getName());
            skull.setItemMeta(meta);
        }

        private void applyTexture(ItemBuilder builder) {
            ItemStack head = new ItemBuilder(DEFAULT_SKULL_MAT).build();
            //preserve item meta
            head.setItemMeta(builder.itemStack.getItemMeta());

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
            builder.setType(DEFAULT_SKULL_MAT);
        }
    }

}
