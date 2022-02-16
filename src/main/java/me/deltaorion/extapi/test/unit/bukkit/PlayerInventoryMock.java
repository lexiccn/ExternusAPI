package me.deltaorion.extapi.test.unit.bukkit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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


public class PlayerInventoryMock extends InventoryMock {
    protected static final int HOTBAR = 0;
    protected static final int SLOT_BAR = 9;
    protected static final int BOOTS = 36;
    protected static final int LEGGINGS = 37;
    protected static final int CHESTPLATE = 38;
    protected static final int HELMET = 39;
    private int mainHandSlot = 0;
    
    private ItemStack offHand = null;

    public PlayerInventoryMock(TestPlayer holder)
    {
        super(InventoryType.PLAYER);
    }

    public PlayerInventory asPlayerInventory() {
        return (PlayerInventory) Proxy.newProxyInstance(PlayerInventory.class.getClassLoader(),
                new Class[]{PlayerInventory.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        if(method.getName().equals("getHolder")) {
                            return getHolder();
                        } else if(method.getName().equals("getArmorContents")) {
                            return getArmorContents();
                        } else if(method.getName().equals("getHelmet")) {
                            return getHelmet();
                        } else if(method.getName().equals("setHelmet")) {
                            setHelmet((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("getChestplate")) {
                            return getChestplate();
                        } else if(method.getName().equals("setChestplate")) {
                            setChestplate((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("setLeggings")) {
                            setLeggings((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("getLeggings")) {
                            return getLeggings();
                        } else if(method.getName().equals("getBoots")) {
                            return getBoots();
                        } else if(method.getName().equals("setBoots")) {
                            setBoots((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("setItemInHand")) {
                            setItemInHand((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("getItemInHand")) {
                            return getItemInHand();
                        } else if(method.getName().equals("setItemInOffHand")) {
                            setItemInOffHand((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("getItemInOffHand")) {
                            return getItemInOffHand();
                        } else if(method.getName().equals("getHeldItemSlot")) {
                            return getHeldItemSlot();
                        } else if(methodName.equals("getSize")) {
                            return getSize();
                        } else if(methodName.equals("getItem")) {
                            int slot = (int) args[0];
                            if(slot==40) {
                                return getItemInOffHand();
                            } else {
                                return getItem(slot);
                            }
                        } else if(methodName.equals("setItem")) {
                            int slot = (int) args[0];
                            if(slot==40) {
                                setItemInOffHand((ItemStack) args[1]);
                            } else {
                                setItem(slot, (ItemStack) args[1]);
                            }
                            return null;
                        } else if(methodName.equals("addItem")) {
                            try {
                                addItem((ItemStack) args[0]);
                            } catch (ClassCastException e) {
                                addItem((ItemStack[]) args[0]);
                            }
                            return null;
                        } else if(methodName.equals("iterator")) {
                            return iterator();
                        } else if(methodName.equals("forEach")) {
                            forEach((Consumer<? super ItemStack>) args[0]);
                            return null;
                        } else if(methodName.equals("getName")) {
                            return getName();
                        } else if(methodName.equals("getType")) {
                            return getType();
                        } else if(methodName.equals("equals")) {
                            return PlayerInventoryMock.this.equals(args[0]);
                        } else if(methodName.equals("hashCode")) {
                            return PlayerInventoryMock.this.hashCode();
                        } else if(methodName.equals("toString")) {
                            return PlayerInventoryMock.this.toString();
                        } else if(methodName.equals("clear")) {
                            clear();
                            return null;
                        } else if(methodName.equals("setHeldItemSlot")) {
                            setHeldItemSlot((Integer) args[0]);
                            return null;
                        }
                        throw new UnsupportedOperationException("Unsupported Method - "+methodName);
                     }
                });
    }

    
    public HumanEntity getHolder()
    {
        return (HumanEntity) super.getHolder();
    }


    
    public ItemStack[] getArmorContents()
    {
        return Arrays.copyOfRange(getContents(), BOOTS, BOOTS + 4);
    }


    
    public ItemStack getHelmet()
    {
        return getItem(HELMET);
    }

    
    public ItemStack getChestplate()
    {
        return getItem(CHESTPLATE);
    }

    
    public ItemStack getLeggings()
    {
        return getItem(LEGGINGS);
    }

    
    public ItemStack getBoots()
    {
        return getItem(BOOTS);
    }

    
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


    
    public void setHelmet(ItemStack helmet)
    {
        setItem(HELMET, helmet);
    }

    
    public void setChestplate(ItemStack chestplate)
    {
        setItem(CHESTPLATE, chestplate);
    }

    
    public void setLeggings(ItemStack leggings)
    {
        setItem(LEGGINGS, leggings);
    }

    
    public void setBoots(ItemStack boots)
    {
        setItem(BOOTS, boots);
    }

    
    public ItemStack getItemInMainHand() {
        return getItemInHand();
    }

    
    public void setItemInMainHand(ItemStack item) {
        setItemInHand(item);
    }

    
    public ItemStack getItemInOffHand() {
        return offHand;
    }

    
    public void setItemInOffHand(ItemStack item) {
        this.offHand = item;
    }

    
    public @NotNull ItemStack getItemInHand()
    {
        return notNull(getItem(mainHandSlot));
    }

    
    public void setItemInHand(ItemStack stack) {
        setItem(mainHandSlot, stack);
    }

    
    public int getHeldItemSlot() {
        return mainHandSlot;
    }

    
    public void setHeldItemSlot(int slot)
    {
        if (slot < 0 || slot > 8)
            throw new ArrayIndexOutOfBoundsException("Slot should be within [0-8] (was: " + slot + ")");
        mainHandSlot = slot;
    }

    
    public int clear(int id, int data) {
        return 0;
    }

    private @NotNull ItemStack notNull(@Nullable ItemStack itemStack)
    {
        return itemStack == null ? new ItemStack(Material.AIR) : itemStack;
    }

    
    public void forEach(Consumer<? super ItemStack> action) {
        super.forEach(action);
    }

    
    public Spliterator<ItemStack> spliterator() {
        return super.spliterator();
    }
}