package me.deltaorion.extapi.item.predicate;

public enum EventCondition {
    //Checks whether the item in the main hand is the custom item.
    MAIN_HAND,
    //Checks whether the item resides anywhere inside of the entities inventory including their armor slots
    INVENTORY,
    //Checks whether the entity is wearing the item inside of their armor slots
    ARMOR,
    //Checks if the item is anywhere in the HotBar including the main hand.
    HOTBAR;
}
