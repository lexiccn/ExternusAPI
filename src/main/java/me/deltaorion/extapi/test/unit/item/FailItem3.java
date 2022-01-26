package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.test.unit.bukkit.FailEvent;
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
