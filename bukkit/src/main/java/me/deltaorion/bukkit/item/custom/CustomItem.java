package me.deltaorion.bukkit.item.custom;

import com.google.common.base.MoreObjects;
import de.tr7zw.nbtapi.NBTCompound;
import me.deltaorion.bukkit.plugin.plugin.BukkitPlugin;
import me.deltaorion.common.plugin.EServer;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import me.deltaorion.bukkit.item.predicate.EventPredicate;
import me.deltaorion.bukkit.item.wrapper.CIEventWrapper;
import me.deltaorion.common.locale.message.Message;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Bukkit currently does not have any (easy) way to make custom materials, or make an item have special properties. This class
 * attempts to rectify this by providing the framework for creating a custom item.
 *   - A custom item has a name, this is similar to a material name. There may only be one custom item with said name, if two
 *     custom items have the same name, they will be treated as the same
 *   - Any item can be 'made custom' A custom item is similar to a 'modifier', it can be applied to any existing itemstack
 *   - Any effects on the custom item can work on both entities and players
 *   - A custom item is truly made custom by registering ItemEventHandler. This essentially registers a listener for when they
 *     are using this item
 *   - To register a custom item event simply annotate the method with {@link ItemEventHandler} and have the first parameter take
 *     a {@link CustomItemEvent<org.bukkit.event.Event>} The parameter must be the type of event you are registering.
 *   - One can also register actual bukkit events as well as this class is a listener
 *   - A default stack can be registered for the item. If one is registered then {@link #newCustomItem()} will hand them
 *     an itemstack. Localisation is supported by setting the default lore and displayname
 *
 */
public class CustomItem implements Listener {

    //holds event listener, underlying object
    @NotNull private final String name;
    @Nullable private final ItemStack defaultStack;
    @Nullable private List<Message> defaultLore;
    @Nullable private Message defaultDisplayName;

    private final String CUSTOM_ITEM_TAG = "EXTAPI_CUSTOM_ITEM";

    /**
     * Creates a new custom item.
     *
     * @param name The name of the custom item
     * @param defaultStack The default itemstack to be given with {@link #newCustomItem()}. If this is null then
     *                     it is assumed there will be no default stack.
     * @throws IllegalArgumentException if the itemstack material is bad see {@link #isBadStack(ItemStack)}
     */
    protected CustomItem(@NotNull String name, @Nullable ItemStack defaultStack) {
        this.name = Objects.requireNonNull(name).toLowerCase(Locale.ROOT);
        this.defaultStack = defaultStack;
        if(defaultStack!=null) {
            if (isBadStack(defaultStack)) {
                throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");
            }
        }
    }

    /**
     * Checks whether the itemstack can be made custom or not. Air, water or lava cannot be made custom due to
     * restrictions on the NBT system and due to the fact that it is hard to call them 'custom' sometimes.
     *
     * @param itemStack The itemstack to check
     * @return whether it is custom or not
     */
    public boolean isBadStack(ItemStack itemStack) {
        if(itemStack==null)
            return true;

        EMaterial material = EMaterial.matchMaterial(itemStack);
        if(material==null)
            return false;

        return material.noNBT();
    }

    /**
     * Sets the default localised display name. To give a localised custom item use {@link #newCustomItem(Locale)}
     *
     * @param defaultDisplayName the default display name
     */
    protected void setDefaultDisplayName(@Nullable Message defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    protected void removeDefaultDisplayName() {
        setDefaultDisplayName(null);
    }

    /**
     * Sets the default localised lore. To give a localised custom item use {@link #newCustomItem(Locale)}. Note, this will
     * not support any Message Parameters (%s). To furthur customise the opening of the custom item override {@link #afterApply(ItemStack)}
     *
     * @param defaultLore The default lore to set
     */
    protected void setDefaultLore(@Nullable Collection<Message> defaultLore) {
        if(defaultLore==null) {
            this.defaultLore = null;
            return;
        }
        this.defaultLore = Collections.unmodifiableList(new ArrayList<>(defaultLore));
    }

    protected void removeDefaultLore() {
        setDefaultLore(null);
    }


    /**
     * Checks whether the given ItemStack is this custom item. This is not a generalised check as to whether it is a custom item
     * at all. It checks whether it is this particular custom item
     *
     * @param itemStack The itemstack to check
     * @return whether the checked itemstack is this custom item.
     */
    public boolean isCustomItem(@Nullable ItemStack itemStack) {
        if(itemStack==null)
            return false;

        if(isBadStack(itemStack))
            return false;

        ItemBuilder builder = new ItemBuilder(itemStack);
        NBTCompound compound = builder.getCompound(CUSTOM_ITEM_TAG);
        if(compound==null)
            return false;

        return Objects.equals(compound.getString(name),name);
    }

    public ItemStack newCustomItem() {
        return newCustomItem(EServer.DEFAULT_LOCALE);
    }

    /**
     * Creates a new custom item using the default stack. If a defaultTitle and lore are set and not null then the title and
     * lore will be set and localised based on them. The beforeApply and afterApply will be called.
     *
     * @param locale The locale to create the itemstack with
     * @return a custom item using the default stack
     * @throws IllegalStateException if the default itemstack is not set
     */
    @NotNull
    public ItemStack  newCustomItem(@NotNull Locale locale) {
        Objects.requireNonNull(locale);
        if(defaultStack==null)
            throw new IllegalStateException("Cannot make custom item without specifying the itemstack as the default stack is null!");

        ItemStack clone = defaultStack.clone();
        ItemBuilder builder = new ItemBuilder(clone);
        if(this.defaultDisplayName!=null) {
            builder.setDisplayName(defaultDisplayName,locale);
        }
        if(this.defaultLore!=null) {
            builder.clearLore();
            for(Message message : defaultLore) {
                builder.addLoreLine(message,locale);
            }
        }

        return newCustomItem(builder.build());
    }

    /**
     * Takes the given itemstack, clones it and then uses the clone to make a new itemstack. This does not make the existing
     * stack custom by reference. To do that use {@link #makeCustom(ItemStack)} instead.
     *
     * @param itemStack The itemstack to use as a template for the custom item
     * @return a new custom item that is similar to the itemstack used
     * @throws IllegalArgumentException if the itemstack material is bad see {@link #isBadStack(ItemStack)}
     */
    @NotNull
    public ItemStack newCustomItem(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if(isBadStack(itemStack))
            throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");

        ItemStack clone = itemStack.clone();
        return apply(clone);
    }

    /**
     * Makes an existing itemstack custom by reference. If the itemstack is in a collection or an inventory, it will still
     * made be custom. This process may or may not work depending on the Custom Item.
     *
     * @param itemStack The itemstack to make custom.
     * @throws IllegalArgumentException if the itemstack material is bad see {@link #isBadStack(ItemStack)}
     */
    public void makeCustom(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if(isBadStack(itemStack))
            throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");

        apply(itemStack);
    }

    /**
     * When registering additional custom item events it must be done here. It should not be done in the constructor.
     * Not all possible item events outcomes are covered by the {@link me.deltaorion.bukkit.item.wrapper.CustomEventWrapper}
     * and not all predicates by {@link me.deltaorion.bukkit.item.predicate.EventCondition}. To register any {@link CustomItemEventListener}
     * because your predicate or event wrapper was not covered simply use {@link CustomItemEventListener#register(BukkitPlugin, Class, CustomItem, EventPredicate, CIEventWrapper, Consumer, boolean, EventPriority, boolean)}
     */
    public void onRegister() {

    }

    /**
     * Makes an itemstack not custom by reference. Is the itemstack is in a collection or inventory it will not longer
     * be custom. This process may or may not work depending on the Custom Item.
     *
     * @param itemStack the itemstack to make custom
     * @throws IllegalArgumentException if the itemstack material is bad see {@link #isBadStack(ItemStack)}
     */
    public void removeCustom(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if(isBadStack(itemStack))
            throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");

        try {
            boolean cancel = beforeRemove(itemStack);
            if (cancel)
                return;
        } catch (Throwable e) {
            try {
                throw new CustomItemException("An error occurred when attempting to remove a custom item modifier from '"+itemStack+"' on an overrided before remove",e);
            } catch (CustomItemException customItemException) {
                customItemException.printStackTrace();
            }
            return;
        }

        ItemBuilder builder = new ItemBuilder(itemStack);
        builder.transformNBT(nbtItem -> {
            NBTCompound compound = nbtItem.getCompound(CUSTOM_ITEM_TAG);
            if(compound==null)
                return;

            if(compound.hasKey(name)) {
                compound.removeKey(name);
            }
        });

        try {
            afterRemove(itemStack);
        } catch (Throwable e) {
            try {
                throw new CustomItemException("An error occurred when attempting to remove a custom item modifier from '"+itemStack+"' on an overrided after remove",e);
            } catch (CustomItemException customItemException) {
                customItemException.printStackTrace();
            }
        }
    }

    /**
     * Functionality that is called after an itemstack is made no longer custom. This should be overriden by the class extending
     * this. One should call the super method especially when chaining
     *
     * @param itemStack The itemstack that is no longer custom
     */
    private void afterRemove(ItemStack itemStack) {

    }

    @Nullable
    protected List<Message> getDefaultLore() {
        return defaultLore;
    }

    @Nullable
    protected Message getDefaultDisplayName() {
        return defaultDisplayName;
    }

    /**
     * Functionality that is called after an itemstack before its custom functionality is removed. This should be overriden by the class extending
     * this. One should call the super method especially when chaining. This should not throw any runtime exceptions. To cancel
     * the removal of the customality of the itemstack simply return true.
     *
     * @param itemStack The itemstack that is no longer custom
     * @return false if the itemstack custom removal should still happen or true if it should be cancelled
     */
    private boolean beforeRemove(ItemStack itemStack) {
        return false;
    }

    private ItemStack apply(@NotNull ItemStack itemStack) {

        //run before apply, handle any errors
        try {
            boolean cancel = beforeApply(itemStack);
            if (cancel)
                return itemStack;
        } catch (Throwable e) {
            try {
                throw new CustomItemException("An error occurred when attempting to apply a custom item modifier from '"+itemStack+"' caused by the before apply",e);
            } catch (CustomItemException customItemException) {
                customItemException.printStackTrace();
            }
            return itemStack;
        }
        //actually apply
        ItemBuilder builder = new ItemBuilder(itemStack);
        builder.transformNBT(nbtItem -> {
            NBTCompound compound = nbtItem.getCompound(CUSTOM_ITEM_TAG);
            if(compound==null) {
                compound = nbtItem.addCompound(CUSTOM_ITEM_TAG);
            }

            if(!compound.hasKey(name)) {
                compound.setString(name,name);
            }
        });

        ItemStack complete = builder.build();
        //run after apply, handling any errors
        try {
            afterApply(complete);
        } catch (Throwable e) {
            try {
                throw new CustomItemException("An error occurred when attempting to apply a custom item modifier from '"+itemStack+"' caused by the after apply",e);
            } catch (CustomItemException customItemException) {
                customItemException.printStackTrace();
            }
        }

        return complete;
    }


    protected void afterApply(ItemStack complete) {

    }

    /**
     * Functionality that is called after an itemstack before its custom is added. This should be overriden by the class extending
     * this. One should call the super method especially when chaining. This should not throw any runtime exceptions. To cancel
     * the applying of the custom functionality of the itemstack simply return true.
     *
     * @param itemStack The itemstack that is no longer custom
     * @return false if the itemstack custom removal should still happen or true if it should be cancelled
     */
    protected boolean beforeApply(ItemStack itemStack) {
        return false;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CustomItem))
            return false;

        return this.name.equals(((CustomItem) o).name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name",name)
                .add("default stack",defaultStack)
                .add("default lore",defaultLore)
                .add("default title",defaultDisplayName).toString();
    }
}
