package me.deltaorion.extapi.test.unit.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MockEntityEquipment implements EntityEquipment {

    private final PlayerInventory inventory;
    private final LivingEntity player;

    public MockEntityEquipment(PlayerInventory inventory, LivingEntity entity) {
        this.player = entity;
        this.inventory = inventory;
    }

    @Override
    public ItemStack getItemInHand() {
        return inventory.getItemInHand();
    }

    @Override
    public void setItemInHand(ItemStack stack) {
        inventory.setItemInHand(stack);
    }

    @Override
    public ItemStack getHelmet() {
        return inventory.getHelmet();
    }

    @Override
    public void setHelmet(ItemStack helmet) {
        inventory.setHelmet(helmet);
    }

    @Override
    public ItemStack getChestplate() {
        return inventory.getChestplate();
    }

    @Override
    public void setChestplate(ItemStack chestplate) {
        inventory.setChestplate(chestplate);
    }

    @Override
    public ItemStack getLeggings() {
        return inventory.getLeggings();
    }

    @Override
    public void setLeggings(ItemStack leggings) {
        inventory.setLeggings(leggings);
    }

    @Override
    public ItemStack getBoots() {
        return inventory.getBoots();
    }

    @Override
    public void setBoots(ItemStack boots) {
        inventory.setBoots(boots);
    }

    @Override
    public ItemStack[] getArmorContents() {
        return inventory.getArmorContents();
    }

    @Override
    public void setArmorContents(ItemStack[] items) {
        inventory.setArmorContents(items);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public float getItemInHandDropChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHelmetDropChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getChestplateDropChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getLeggingsDropChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getBootsDropChance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Entity getHolder() {
        return  player;
    }
}