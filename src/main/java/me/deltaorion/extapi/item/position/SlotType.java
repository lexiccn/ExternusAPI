package me.deltaorion.extapi.item.position;

/**
 * This enum represents a special slot inside of an entity inventory.
 *
 * Some inventory types such as the Living Entities equipment cannot be represented as inventory slots. Thus the Slot Representation
 * exists to give these impossible slots a number if it is so required. For most purposes this number isn't very useful.
 */
public enum SlotType {
    MAIN_HAND(-1),
    CHESTPLATE(-2),
    LEGGINGS(-3),
    HELMET(-4),
    BOOTS(-5),
    OTHER(-9001)
    ;

    private final int slotRepresentation;

    SlotType(int slotRepresentation) {
        this.slotRepresentation = slotRepresentation;
    }

    public int getSlot() {
        return this.slotRepresentation;
    }
}
