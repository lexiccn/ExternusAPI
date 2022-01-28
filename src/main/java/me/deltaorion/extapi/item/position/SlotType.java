package me.deltaorion.extapi.item.position;

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
