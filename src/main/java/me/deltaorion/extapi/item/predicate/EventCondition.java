package me.deltaorion.extapi.item.predicate;

import me.deltaorion.extapi.item.position.SlotType;

public enum EventCondition {
    //Checks whether the item in the main hand is the custom item.
    MAIN_HAND(-1),
    //Checks whether the item in the off hand is the custom item
    OFF_HAND(SlotType.OFF_HAND.getRelease()),
    //Checks whether the item is in any hand
    ANY_HAND(-1),
    //Checks whether the item resides anywhere inside of the entities inventory including their armor slots
    INVENTORY(-1),
    //Checks whether the entity is wearing the item inside of their armor slots
    ARMOR(-1),
    //Checks if the item is anywhere in the HotBar including the main hand and the offhand.
    HOTBAR(-1);

    private final int compatible;
    EventCondition(int compatible) {
        this.compatible = compatible;
    }

    public int getCompatibleVersion() {
        return compatible;
    }
}
