package me.deltaorion.bukkit.item.potion;

import com.google.common.collect.ImmutableMap;
import me.deltaorion.bukkit.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PotionBuilder_11 extends ModernPotionBuilder {

    private static final Map<PotionType,Color> typeColorMap = new ImmutableMap.Builder<PotionType,Color>()
            .put(PotionType.WATER,Color.fromRGB(55,92,195))
            .put(PotionType.AWKWARD,Color.fromRGB(55,92,195))
            .put(PotionType.MUNDANE,Color.fromRGB(55,92,195))
            .put(PotionType.UNCRAFTABLE,Color.fromRGB(244,0,197))
            .put(PotionType.THICK,Color.fromRGB(55,92,195))
            .put(PotionType.NIGHT_VISION,Color.fromRGB(94,43,255))
            .put(PotionType.INVISIBILITY,Color.fromRGB(152,117,242))
            .put(PotionType.JUMP,Color.fromRGB(78,215,188))
            .put(PotionType.FIRE_RESISTANCE,Color.fromRGB(232,135,174))
            .put(PotionType.SPEED,Color.fromRGB(149,152,255))
            .put(PotionType.SLOWNESS,Color.fromRGB(121,99,227))
            .put(PotionType.WATER_BREATHING,Color.fromRGB(86,77,244))
            .put(PotionType.INSTANT_HEAL,Color.fromRGB(246,41,151))
            .put(PotionType.INSTANT_DAMAGE,Color.fromRGB(103,20,130))
            .put(PotionType.POISON,Color.fromRGB(110,128,160))
            .put(PotionType.REGEN,Color.fromRGB(211,86,255))
            .put(PotionType.STRENGTH,Color.fromRGB(165,42,152))
            .put(PotionType.WEAKNESS,Color.fromRGB(107,73,180))
            .put(PotionType.LUCK,Color.fromRGB(98,136,148)).build();

    public PotionBuilder_11(@NotNull ItemBuilder builder) {
        super(builder);
    }



    @NotNull
    @Override
    public PotionBuilder setColor(@NotNull Color color) {
        return transformMeta(potionMeta -> {
            potionMeta.setColor(color);
        });
    }

    @NotNull
    @Override
    public PotionBuilder setColor(@NotNull PotionType type) {
        return transformMeta(potionMeta -> {
            potionMeta.setColor(getColor(type));
        });
    }

    private Color getColor(PotionType type) {
        if(!typeColorMap.containsKey(type))
            return typeColorMap.get(PotionType.WATER);
        return typeColorMap.get(type);
    }
}
