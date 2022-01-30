package me.deltaorion.extapi.test.unit.item;

import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import me.deltaorion.extapi.item.custom.CustomItem;
import me.deltaorion.extapi.item.custom.CustomItemEvent;
import me.deltaorion.extapi.item.custom.ItemEventHandler;
import me.deltaorion.extapi.item.position.InventoryItem;
import me.deltaorion.extapi.item.position.LivingEntityItem;
import me.deltaorion.extapi.item.position.SlotType;
import me.deltaorion.extapi.item.predicate.EventCondition;
import me.deltaorion.extapi.item.wrapper.CustomEventWrapper;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SlotTypeTest extends CustomItem {

    private final Random random;

    public SlotTypeTest() {
        super("Slot_Type_Test",new ItemStack(Material.DIAMOND_CHESTPLATE));
        this.random = new Random();
    }

    @ItemEventHandler(condition = EventCondition.INVENTORY, wrapper = CustomEventWrapper.DAMAGE_ANOTHER_ENTITY, playerOnly = false)
    public void onHit(CustomItemEvent<EntityDamageByEntityEvent> event) {
        InventoryItem item = event.getItemStacks().get(0);
        ItemStack itemStack = Objects.requireNonNull(item.getItemStack());
        SlotType original = item.getSlotType();
        SlotType next = null;
        item.setItem(null);
        List<SlotType> slotTypes = new ArrayList<>(Arrays.asList(SlotType.values()));
        slotTypes.remove(SlotType.OTHER);
        for(int i=0;i<slotTypes.size();i++) {
            SlotType type = slotTypes.get(i);
            if(type.equals(original)) {
                if(i==slotTypes.size()-1) {
                    next = slotTypes.get(0);
                } else {
                    next = slotTypes.get(i+1);
                }
                break;
            }
        }

        if(next==null)
            return;

        InventoryItem i = new LivingEntityItem(event.getEntity(),next,itemStack);
        i.setItem(itemStack);
    }

    @ItemEventHandler
    public void onInventoryClick(CustomItemEvent<InventoryClickEvent> event) {
        new ItemBuilder(event.getItemStacks().get(0).getItemStack()).setType(randomMaterial()).build();
        event.getEvent().setCancelled(true);
    }

    private EMaterial randomMaterial() {
        return EMaterial.values()[random.nextInt(EMaterial.values().length)];
    }
}
