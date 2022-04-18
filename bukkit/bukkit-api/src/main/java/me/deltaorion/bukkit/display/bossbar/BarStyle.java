package me.deltaorion.bukkit.display.bossbar;

import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.item.EMaterial;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum BarStyle {
    PROGRESS("SOLID"),
    NOTCHED_6("SEGMENTED_6"),
    NOTCHED_10("SEGMENTED_10"),
    NOTCHED_12("SEGMENTED_12"),
    NOTCHED_20("SEGMENTED_20"),
    ;

    @NotNull private final String bukkitName;
    @Nullable private org.bukkit.boss.BarStyle bukkitStyle;

    BarStyle(@NotNull String bukkitStyle) {
        this.bukkitName = bukkitStyle;
        this.bukkitStyle = null;
    }

    @NotNull
    public org.bukkit.boss.BarStyle getBukkitStyle() {
        if(EMaterial.getVersion()==null || EMaterial.getVersion().getMajor()<12)
            throw new UnsupportedVersionException("Cannot get the org.bukkit.boss.BarColor as the version is less than 1.12!");

        if(bukkitStyle!=null)
            return bukkitStyle;

        org.bukkit.boss.BarStyle style = null;
        try {
            style = org.bukkit.boss.BarStyle.valueOf(bukkitName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Bar Color '"+name()+"' corresponding bukkit color '"+bukkitName+"' does not exist!");
        }

        this.bukkitStyle = style;
        return style;
    }
}
