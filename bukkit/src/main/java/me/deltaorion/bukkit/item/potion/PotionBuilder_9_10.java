package me.deltaorion.bukkit.item.potion;

import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class PotionBuilder_9_10 extends ModernPotionBuilder {
    public PotionBuilder_9_10(@NotNull ItemBuilder builder) {
        super(builder);
    }

    @NotNull
    @Override
    public PotionBuilder setColor(@NotNull Color color) {
        throw new UnsupportedVersionException("RGB Potion colors are not supported in this version!");
    }

    @NotNull
    @Override
    public PotionBuilder setColor(@NotNull PotionType type) {
        return this;
    }
}
