package me.deltaorion.extapi.common.plugin;

public enum BukkitAPIDepends {

    NBTAPI("NBTAPI"),
    PROTOCOL_LIB("ProtocolLib");

    private final String name;

    BukkitAPIDepends(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
