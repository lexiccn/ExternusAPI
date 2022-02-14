package me.deltaorion.extapi.test.unit.bukkit;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PlayerInventoryMock extends InventoryMock implements PlayerInventory
{
    protected static final int HOTBAR = 0;
    protected static final int SLOT_BAR = 9;
    protected static final int BOOTS = 36;
    protected static final int LEGGINGS = 37;
    protected static final int CHESTPLATE = 38;
    protected static final int HELMET = 39;
    private int mainHandSlot = 0;

    public PlayerInventoryMock(HumanEntity holder)
    {
        super(holder, InventoryType.PLAYER);
    }

    @Override
    public HumanEntity getHolder()
    {
        return (HumanEntity) super.getHolder();
    }


    @Override
    public ItemStack[] getArmorContents()
    {
        return Arrays.copyOfRange(getContents(), BOOTS, BOOTS + 4);
    }


    @Override
    public ItemStack getHelmet()
    {
        return getItem(HELMET);
    }

    @Override
    public ItemStack getChestplate()
    {
        return getItem(CHESTPLATE);
    }

    @Override
    public ItemStack getLeggings()
    {
        return getItem(LEGGINGS);
    }

    @Override
    public ItemStack getBoots()
    {
        return getItem(BOOTS);
    }

    @Override
    public void setArmorContents(ItemStack[] items)
    {
        if (items == null)
            throw new NullPointerException("ItemStack was null");
        else if (items.length > 4)
            throw new IllegalArgumentException("ItemStack array too large (max: 4, was: " + items.length + ")");
        items = (items.length == 4) ? items : Arrays.copyOf(items, 4);
        setItem(BOOTS, items[0]);
        setItem(LEGGINGS, items[1]);
        setItem(CHESTPLATE, items[2]);
        setItem(HELMET, items[3]);
    }


    @Override
    public void setHelmet(ItemStack helmet)
    {
        setItem(HELMET, helmet);
    }

    @Override
    public void setChestplate(ItemStack chestplate)
    {
        setItem(CHESTPLATE, chestplate);
    }

    @Override
    public void setLeggings(ItemStack leggings)
    {
        setItem(LEGGINGS, leggings);
    }

    @Override
    public void setBoots(ItemStack boots)
    {
        setItem(BOOTS, boots);
    }

    @Override
    public @NotNull ItemStack getItemInHand()
    {
        return notNull(getItem(mainHandSlot));
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        setItem(mainHandSlot, stack);
    }

    @Override
    public int getHeldItemSlot()
    {
        return mainHandSlot;
    }

    @Override
    public void setHeldItemSlot(int slot)
    {
        if (slot < 0 || slot > 8)
            throw new ArrayIndexOutOfBoundsException("Slot should be within [0-8] (was: " + slot + ")");
        mainHandSlot = slot;
    }

    @Override
    public int clear(int id, int data) {
        return 0;
    }

    private @NotNull ItemStack notNull(@Nullable ItemStack itemStack)
    {
        return itemStack == null ? new ItemStack(Material.AIR) : itemStack;
    }

    @Override
    public void forEach(Consumer<? super ItemStack> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<ItemStack> spliterator() {
        return super.spliterator();
    }
}