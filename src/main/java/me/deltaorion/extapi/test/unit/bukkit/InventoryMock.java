package me.deltaorion.extapi.test.unit.bukkit;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class InventoryMock implements Iterable<ItemStack>
{
    private final ItemStack[] items;
    private final InventoryType type;

    public InventoryMock(int size, @NotNull InventoryType type)
    {
        Validate.isTrue(9 <= size && size <= 54 && size % 9 == 0,
                "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got " + size + ")");
        Validate.notNull(type, "The InventoryType must not be null!");

        this.type = type;

        items = new ItemStack[size];
    }

    public InventoryMock(@NotNull InventoryType type)
    {
        Validate.notNull(type, "The InventoryType must not be null!");
        this.type = type;

        int size = type.getDefaultSize();
        if(type==InventoryType.PLAYER) {
            size = 40;
        }

        items = new ItemStack[size];
    }

    public Inventory asInventory() {
        return (Inventory) Proxy.newProxyInstance(Inventory.class.getClassLoader(),
                new Class[]{Inventory.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        if(methodName.equals("assertTrueForAll")) {
                        }  else if(methodName.equals("getSize")) {
                            return getSize();
                        } else if(methodName.equals("getItem")) {
                            return getItem((Integer) args[0]);
                        } else if(methodName.equals("setItem")) {
                            setItem((Integer)args[0],(ItemStack)args[1]);
                            return null;
                        } else if(methodName.equals("addItem")) {
                            addItem((ItemStack) args[0]);
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
                            return InventoryMock.this.equals(args[0]);
                        } else if(methodName.equals("hashCode")) {
                            return InventoryMock.this.hashCode();
                        } else if(methodName.equals("toString")) {
                            return InventoryMock.this.toString();
                        } else if(methodName.equals("clear")) {
                            clear();
                            return null;
                        }
                        throw new UnsupportedOperationException("Unsupported Method - "+methodName);
                    }
                });
    }
    /**
     * Asserts that a certain condition is true for all items, even {@code nulls}, in this inventory.
     *
     * @param condition The condition to check for.
     */
    public void assertTrueForAll(@NotNull Predicate<ItemStack> condition)
    {
        for (ItemStack item : items)
        {
            assertTrue(condition.test(item));
        }
    }

    /**
     * Assets that a certain condition is true for all items in this inventory that aren't null.
     *
     * @param condition The condition to check for.
     */
    public void assertTrueForNonNulls(@NotNull Predicate<ItemStack> condition)
    {
        assertTrueForAll(itemstack -> itemstack == null || condition.test(itemstack));
    }

    /**
     * Asserts that a certain condition is true for at least one item in this inventory. It will skip any null items.
     *
     * @param condition The condition to check for.
     */
    public void assertTrueForSome(@NotNull Predicate<ItemStack> condition)
    {
        for (ItemStack item : items)
        {
            if (item != null && condition.test(item))
            {
                return;
            }
        }
        fail("Condition was not met for any items");
    }

    /**
     * Asserts that the inventory contains at least one itemstack that is compatible with the given itemstack.
     *
     * @param item The itemstack to compare everything to.
     */
    public void assertContainsAny(@NotNull ItemStack item)
    {
        assertTrueForSome(item::isSimilar);
    }

    /**
     * Asserts that the inventory contains at least a specific amount of items that are compatible with the given
     * itemstack.
     *
     * @param item   The itemstack to search for.
     * @param amount The minimum amount of items that one should have.
     */
    public void assertContainsAtLeast(@NotNull ItemStack item, int amount)
    {
        int n = getNumberOfItems(item);
        String message = String.format("Inventory contains only <%d> but expected at least <%d>", n, amount);
        assertTrue(message, n >= amount);
    }

    /**
     * Get the number of times a certain item is in the inventory.
     *
     * @param item The item to check for.
     * @return The number of times the item is present in this inventory.
     */
    public int getNumberOfItems(@NotNull ItemStack item)
    {
        int amount = 0;
        for (ItemStack itemstack : items)
        {
            if (itemstack != null && item.isSimilar(itemstack))
            {
                amount += itemstack.getAmount();
            }
        }
        return amount;
    }

    
    public int getSize()
    {
        return items.length;
    }

    
    public ItemStack getItem(int index)
    {
        return items[index];
    }

    
    public void setItem(int index, ItemStack item)
    {
        items[index] = item == null ? null : item.clone();
    }

    /**
     * Adds a single item to the inventory. Returns whatever item it couldn't add.
     *
     * @param item The item to add.
     * @return The remaining stack that couldn't be added. If it's empty it just returns {@code null}.
     */
    @Nullable
    public ItemStack
    addItem(@NotNull ItemStack item)
    {
        item = item.clone();
        for (int i = 0; i < items.length; i++)
        {
            ItemStack oItem = items[i];
            if (oItem == null)
            {
                int toAdd = Math.min(item.getAmount(), item.getMaxStackSize());
                items[i] = item.clone();
                items[i].setAmount(toAdd);
                item.setAmount(item.getAmount() - toAdd);
            }
            else if (item.isSimilar(oItem) && oItem.getAmount() < oItem.getMaxStackSize())
            {
                int toAdd = Math.min(item.getAmount(), item.getMaxStackSize() - oItem.getAmount());
                oItem.setAmount(oItem.getAmount() + toAdd);
                item.setAmount(item.getAmount() - toAdd);
            }

            if (item.getAmount() == 0)
            {
                return null;
            }
        }

        return item;
    }

    
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException
    {
        HashMap<Integer, ItemStack> notSaved = new HashMap<>();
        for (int i = 0; i < items.length; i++)
        {
            ItemStack item = items[i];
            ItemStack left = addItem(item);
            if (left != null)
            {
                notSaved.put(i, left);
            }
        }
        return notSaved;
    }

    
    public ItemStack[] getContents()
    {
        return items;
    }

    
    public void setContents(ItemStack[] items)
    {
        for (int i = 0; i < getSize(); i++)
        {
            if (i < items.length && items[i] != null)
            {
                this.items[i] = items[i].clone();
            }
            else
            {
                this.items[i] = null;
            }
        }
    }

    
    public boolean contains(int materialId) {
        return false;
    }

    
    public InventoryHolder getHolder() {
        return null;
    }

    
    public ListIterator<ItemStack> iterator()
    {
        List<ItemStack> list = Arrays.asList(items).stream().filter(Objects::nonNull).collect(Collectors.toList());
        return list.listIterator();
    }

    
    public InventoryType getType()
    {
        return type;
    }

    
    public int getMaxStackSize()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public void setMaxStackSize(int size)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public String getName() {
        return null;
    }

    
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }


    
    public boolean contains(Material material) throws IllegalArgumentException
    {
        if (material == null)
        {
            throw new IllegalArgumentException("Material cannot be null.");
        }
        for (ItemStack itemStack : this.getContents())
        {
            if (itemStack != null && itemStack.getType() == material)
            {
                return true;
            }
        }
        return false;
    }

    
    public boolean contains(ItemStack item)
    {
        return contains(Objects.requireNonNull(item).getType());
    }

    
    public boolean contains(int materialId, int amount) {
        return false;
    }

    
    public boolean contains(Material material, int amount) throws IllegalArgumentException
    {
        if (material == null)
        {
            throw new IllegalArgumentException("Material cannot be null.");
        }
        return amount < 1 || getNumberOfItems(new ItemStack(material)) == amount;
    }

    
    public boolean contains(ItemStack item, int amount)
    {
        return getNumberOfItems(item) == amount;
    }

    
    public boolean containsAtLeast(ItemStack item, int amount)
    {
        return getNumberOfItems(item) >= amount;
    }

    
    public HashMap<Integer, ? extends ItemStack> all(int materialId) {
        return null;
    }

    
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public int first(int materialId) {
        return 0;
    }

    
    public int first(Material material) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public int first(ItemStack item)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public int firstEmpty()
    {
        for (int i = 0; i < getSize(); i++)
        {
            if (items[i] == null || items[i].getType() == Material.AIR)
            {
                return i;
            }
        }

        return -1;
    }

    
    public void remove(int materialId) {

    }

    
    public void remove(Material material) throws IllegalArgumentException
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public void remove(ItemStack item)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public void clear(int index)
    {
        items[index] = null;
    }

    
    public void clear()
    {
        Arrays.fill(items, null);
    }

    
    public List<HumanEntity> getViewers()
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    
    public String getTitle() {
        return null;
    }

    
    public ListIterator<ItemStack> iterator(int index)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }


}