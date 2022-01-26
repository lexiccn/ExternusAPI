package me.deltaorion.extapi.test.unit.item;

import com.google.common.collect.ImmutableList;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.predicate.EventCondition;
import me.deltaorion.extapi.item.wrapper.CustomEventWrapper;
import me.deltaorion.extapi.locale.message.Message;
import me.deltaorion.extapi.test.unit.bukkit.TestEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TestItem extends CustomItem {

    private final List<String> helper;

    public TestItem(List<String> helper) {
        super("Test_Item", new ItemStack(Material.EMERALD));
        this.helper = helper;
        setDefaultDisplayName(Message.valueOfTranslatable("hello"));
        setDefaultLore(ImmutableList.of(Message.valueOfTranslatable("hello")));
    }

    @ItemEventHandler(predicate = EventCondition.HOTBAR)
    private void onEvent(CustomItemEvent<TestEvent> event) {
        helper.add(event.getEvent().getMessage());
    }

    @ItemEventHandler(predicate = EventCondition.MAIN_HAND, wrappers = CustomEventWrapper.PLAYER_EVENT)
    public void onEvent2(CustomItemEvent<TestEvent> event) {
        helper.add(event.getEvent().getMessage());
    }

    @ItemEventHandler(predicate = EventCondition.INVENTORY)
    public void onEvent3(CustomItemEvent<TestEvent> event) {
        helper.add(event.getEvent().getMessage());
    }

    @ItemEventHandler(predicate = EventCondition.ARMOR)
    public void onEvent4(CustomItemEvent<TestEvent> event) {
        helper.add(event.getEvent().getMessage());
    }




}
