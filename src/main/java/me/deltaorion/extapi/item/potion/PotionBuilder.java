package me.deltaorion.extapi.item.potion;

import org.bukkit.Color;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface PotionBuilder {

    /**
     * Sets what type of potion this is
     *   - Drinkable
     *   - Splash
     *   - Lingering
     *
     * @param type The new type for the potion
     * @return This potion builder
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException If the version does not support
      * that potion type.
     */
    public PotionBuilder setType(@NotNull Type type);

    /**
     * Sets the color of the potion. This will not change any effects the potion has, only the display
     * color of the potion
     *
     * @param color The new color for the potion
     * @return This potion builder
     * @throws me.deltaorion.extapi.common.exception.UnsupportedVersionException If the version does not support
     * RGB Potion colors. In this case use {@link #setColor(PotionType)} instead.
     */
    @NotNull
    public PotionBuilder setColor(@NotNull Color color);

    /**
     * Adds the given potion effect. If the potion already has this effect this will do nothing
     *
     * @param effect The effect to add
     * @return this potion builder
     */
    @NotNull
    public PotionBuilder addEffect(@NotNull PotionEffect effect);

    /**
     * Sets the color of the potion using one of the existing potion types. This will not effect what potion
     * effects the potion receives, only its visible color
     *
     *  Note in versions 1.9 and 1.10 there is no way ot control the color of the potion as it is determined as a result
     *  of mixing the effects. There is nothing that can be done to fix this.
     *
     *  In version 1.8 you can set the color, however you cannot have a particular color without having at least one potion
     *  effect. That being said if a custom effect is given, the potion will lose its original effect.
     *
     * @param type The potion type to change the color to
     * @return this potion builder
     */
    @NotNull
    public PotionBuilder setColor(@NotNull PotionType type);

    /**
     *
     * @param effect Removes the specified effect from the potion. if the potion does not have
     *               this effect then nothing will happen
     * @return this potion builder
     */
    @NotNull
    public PotionBuilder removeEffect(@NotNull PotionEffect effect);

    /**
     * Removes all current potion effects
     *
     * @return this potion builder
     */
    @NotNull
    public PotionBuilder clearEffects();

    /**
     * @return All of the potion effects that the potion currently has
     */
    @NotNull
    public Collection<PotionEffect> getEffects();

    /**
     * Moves a potion effect type to the top of the potion effects list. This does not effect the actual potion effects, just how the
     * list of potion effects is displayed on the physical item.
     *
     * @param type
     * @return
     */
    @NotNull
    public PotionBuilder setMainEffect(@NotNull PotionEffectType type);

    public static enum Type {
        DRINKABLE,
        SPLASH,
        LINGERING,
        TIPPED_ARROW;
    }

}
