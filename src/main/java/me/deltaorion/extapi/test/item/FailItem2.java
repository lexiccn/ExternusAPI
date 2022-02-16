package me.deltaorion.extapi.test.item;

import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import org.junit.Assert;

public class FailItem2 extends CustomItem {
    public FailItem2() {
        super("Test_Item_Fail", null);
    }

    @ItemEventHandler
    public void onEvent() {
        Assert.fail();
    }
}
