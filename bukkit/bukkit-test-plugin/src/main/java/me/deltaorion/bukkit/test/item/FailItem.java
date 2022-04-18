package me.deltaorion.bukkit.test.item;

import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.custom.ItemEventHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Assert;

public class FailItem extends CustomItem {

    public FailItem() {
        super("Test_Item_Fail", new ItemStack(Material.NAME_TAG));
    }

    @ItemEventHandler
    public void onEvent(String event) {
        Assert.fail();
    }

}