package me.deltaorion.bukkit.item.position;

/**
 * This enum represents a special slot inside of an entity inventory.
 *
 * Some inventory types such as the Living Entities equipment cannot be represented as inventory slots. Thus the Slot Representation
 * exists to give these impossible slots a number if it is so required. For most purposes this number isn't very useful.
 */
public enum SlotType {
    MAIN_HAND(-1, -1, -1),
    CHESTPLATE(-2, 38, -1),
    LEGGINGS(-3, 37, -1),
    HELMET(-4, 39, -1),
    BOOTS(-5, 36, -1),
    OFF_HAND(-6,40, 10),
    OTHER(-9001,-9001, -1)
    ;

    private final int slotRepresentation;
    private final int bukkitRepresentation;
    private final int release;

    SlotType(int slotRepresentation, int bukkitRepresentation, int release) {
        this.slotRepresentation = slotRepresentation;
        this.bukkitRepresentation = bukkitRepresentation;
        this.release = release;
    }

    public int getSlot() {
        return this.slotRepresentation;
    }

    public int getBukkitSlot() {
        return bukkitRepresentation;
    }

    /**
     * @return The version this slot type was added
     */
    public int getRelease() {
        return release;
    }
}
