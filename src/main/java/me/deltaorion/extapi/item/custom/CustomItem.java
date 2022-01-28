package me.deltaorion.extapi.item.custom;

import de.tr7zw.nbtapi.NBTCompound;
import me.deltaorion.extapi.common.server.EServer;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.locale.message.Message;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class CustomItem implements Listener {

    //holds event listener, underlying object
    @NotNull private final String name;
    @Nullable private final ItemStack defaultStack;
    @Nullable private List<Message> defaultLore;
    @Nullable private Message defaultDisplayName;

    private final String CUSTOM_ITEM_TAG = "EXTAPI_CUSTOM_ITEM";

    protected CustomItem(@NotNull String name, @Nullable ItemStack defaultStack) {
        this.name = Objects.requireNonNull(name).toLowerCase(Locale.ROOT);
        this.defaultStack = defaultStack;
        if(defaultStack!=null) {
            if (isBadStack(defaultStack)) {
                throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");
            }
        }
    }

    private boolean isBadStack(ItemStack itemStack) {
        if(itemStack==null)
            return true;

        return EMaterial.rawGasLiquid(itemStack.getType());
    }

    protected void setDefaultDisplayName(@Nullable Message defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    protected void removeDefaultDisplayName() {
        setDefaultDisplayName(null);
    }

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



    //just tells you if the item stack given is a custom item
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

    @NotNull
    public ItemStack newCustomItem(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if(isBadStack(itemStack))
            throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");

        ItemStack clone = itemStack.clone();
        return apply(clone);
    }

    public void makeCustom(@NotNull ItemStack itemStack) {
        Objects.requireNonNull(itemStack);
        if(isBadStack(itemStack))
            throw new IllegalArgumentException("Custom Items cannot be null or made of air or a liquid");

        apply(itemStack);
    }


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

    private void afterRemove(ItemStack itemStack) {

    }

    private boolean beforeRemove(ItemStack itemStack) {
        return false;
    }

    private ItemStack apply(@NotNull ItemStack itemStack) {


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

    protected boolean beforeApply(ItemStack itemStack) {
        return false;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
