package me.deltaorion.bukkit.test.item;

import me.deltaorion.bukkit.item.custom.CustomItem;
import me.deltaorion.bukkit.item.custom.CustomItemEvent;
import me.deltaorion.bukkit.item.custom.ItemEventHandler;
import me.deltaorion.bukkit.test.bukkit.FailEvent;
import org.junit.Assert;

public class FailItem3 extends CustomItem {
    public FailItem3() {
        super("Fail_Item", null);
    }

    @ItemEventHandler
    public void onEvent(CustomItemEvent<FailEvent> event) {
        Assert.fail();
    }
}
