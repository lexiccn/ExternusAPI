package me.deltaorion.extapi.display.bossbar;

import me.deltaorion.extapi.common.exception.UnsupportedVersionException;
import me.deltaorion.extapi.item.EMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BarColor {
    PINK("PINK"),
    BLUE("BLUE"),
    RED("RED"),
    GREEN("GREEN"),
    YELLOW("YELLOW"),
    PURPLE("PURPLE"),
    WHITE("WHITE")
    ;

    @NotNull private final String bukkitName;
    @Nullable private org.bukkit.boss.BarColor bukkitColor;

    BarColor(@NotNull String bukkitName) {
        this.bukkitName = bukkitName;
        bukkitColor = null;
    }

    /**
     * @return The matching Bukkit Bar color
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException If the version is less than 1.12
     */
    @NotNull
    public org.bukkit.boss.BarColor getBukkitColor() {
        if(EMaterial.getVersion()==null || EMaterial.getVersion().getMajor()<12)
            throw new UnsupportedVersionException("Cannot get the org.bukkit.boss.BarColor as the version is less than 1.12!");

        if(bukkitColor!=null)
            return bukkitColor;

        org.bukkit.boss.BarColor color = null;
        try {
            color = org.bukkit.boss.BarColor.valueOf(bukkitName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Bar Color '"+name()+"' corresponding bukkit color '"+bukkitName+"' does not exist!");
        }
        bukkitColor = color;
        return bukkitColor;
    }
}