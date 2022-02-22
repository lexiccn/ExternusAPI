package me.deltaorion.bukkit.test.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MockEntityEquipment {

    private final PlayerInventoryMock inventory;

    public MockEntityEquipment(PlayerInventoryMock inventory) {
        this.inventory = inventory;
    }

    public EntityEquipment asEquipment() {
        return (EntityEquipment) Proxy.newProxyInstance(EntityEquipment.class.getClassLoader(),
                new Class[]{EntityEquipment.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String methodName = method.getName();
                        if(method.getName().equals("getHelmet")) {
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
                        } else if(method.getName().equals("getItemInMainHand")) {
                            return getItemInMainHand();
                        }
                        else if(method.getName().equals("setItemInOffHand")) {
                            setItemInOffHand((ItemStack) args[0]);
                            return null;
                        } else if(method.getName().equals("getItemInOffHand")) {
                            return getItemInOffHand();
                        } else if(method.getName().equals("clear")) {
                            clear();
                            return null;
                        } else if(method.getName().equals("equals")) {
                            return MockEntityEquipment.this.equals(args[0]);
                        } else if(method.getName().equals("hashCode")) {
                            return MockEntityEquipment.this.hashCode();
                        } else if(method.getName().equals("toString")) {
                            return MockEntityEquipment.this.toString();
                        }
                        throw new UnsupportedOperationException("Unsupported Method - "+methodName);
                    }
                });
    }

    private ItemStack getItemInMainHand() {
        return inventory.getItemInMainHand();
    }


    public ItemStack getItemInOffHand() {
        return inventory.getItemInOffHand();
    }

    
    public void setItemInOffHand(ItemStack item) {
        inventory.setItemInOffHand(item);
    }

    
    public ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    
    public void setItemInHand(ItemStack stack) {
        inventory.setItemInHand(stack);
    }

    
    public ItemStack getHelmet() {
        return inventory.getHelmet();
    }

    
    public void setHelmet(ItemStack helmet) {
        inventory.setHelmet(helmet);
    }

    
    public ItemStack getChestplate() {
        return inventory.getChestplate();
    }

    
    public void setChestplate(ItemStack chestplate) {
        inventory.setChestplate(chestplate);
    }

    
    public ItemStack getLeggings() {
        return inventory.getLeggings();
    }

    
    public void setLeggings(ItemStack leggings) {
        inventory.setLeggings(leggings);
    }

    
    public ItemStack getBoots() {
        return inventory.getBoots();
    }

    
    public void setBoots(ItemStack boots) {
        inventory.setBoots(boots);
    }

    
    public ItemStack[] getArmorContents() {
        return inventory.getArmorContents();
    }

    
    public void setArmorContents(ItemStack[] items) {
        inventory.setArmorContents(items);
    }

    
    public void clear() {
        inventory.clear();
    }

    
    public float getItemInHandDropChance() {
        throw new UnsupportedOperationException();
    }

    
    public void setItemInHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    
    public float getHelmetDropChance() {
        throw new UnsupportedOperationException();
    }

    
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    
    public float getChestplateDropChance() {
        throw new UnsupportedOperationException();
    }

    
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    
    public float getLeggingsDropChance() {
        throw new UnsupportedOperationException();
    }

    
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    
    public float getBootsDropChance() {
        throw new UnsupportedOperationException();
    }

    
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    
    public Entity getHolder() {
        return null;
    }
}