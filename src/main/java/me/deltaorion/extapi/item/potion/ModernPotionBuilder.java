package me.deltaorion.extapi.item.potion;

import com.google.common.collect.ImmutableMap;
import me.deltaorion.extapi.common.exception.UnsupportedVersionException;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ModernPotionBuilder implements PotionBuilder {

    @NotNull private final ItemBuilder builder;
    private final static Map<Type,EMaterial> POTION_TYPES = ImmutableMap.of(
            Type.DRINKABLE,EMaterial.POTION,
            Type.SPLASH,EMaterial.SPLASH_POTION,
            Type.LINGERING,EMaterial.LINGERING_POTION,
            Type.TIPPED_ARROW,EMaterial.TIPPED_ARROW
    );

    public ModernPotionBuilder(@NotNull ItemBuilder builder) {
        this.builder = builder;
        if(!POTION_TYPES.containsValue(builder.getType())) {
            builder.setType(EMaterial.POTION);
            transformMeta(potionMeta -> {
                potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
            });
        }
    }

    @Override
    public PotionBuilder setType(@NotNull Type type) {
        if(!POTION_TYPES.containsKey(type))
            throw new UnsupportedVersionException("The potion type '"+type+"' is not supported in this version!");

        builder.setType(POTION_TYPES.get(type));
        return this;
    }

    @NotNull
    @Override
    public PotionBuilder addEffect(@NotNull PotionEffect effect) {
        return transformMeta(potionMeta -> {
            potionMeta.addCustomEffect(effect,true);
        });
    }

    @NotNull
    @Override
    public PotionBuilder removeEffect(@NotNull PotionEffect effect) {
        return transformMeta(potionMeta -> {
            potionMeta.removeCustomEffect(effect.getType());
        });
    }

    @NotNull
    @Override
    public PotionBuilder clearEffects() {
        return  transformMeta(PotionMeta::clearCustomEffects);
    }

    @NotNull
    @Override
    public Collection<PotionEffect> getEffects() {
        return Collections.unmodifiableList(((PotionMeta) builder.build().getItemMeta()).getCustomEffects());
    }

    @NotNull
    @Override
    public PotionBuilder setMainEffect(@NotNull PotionEffectType type) {
        return transformMeta(potionMeta -> {
            potionMeta.setMainEffect(type);
        });
    }

    protected PotionBuilder transformMeta(@NotNull Consumer<PotionMeta> metaConsumer) {
        builder.transformMeta(itemMeta -> {
            if(itemMeta instanceof PotionMeta) {
                metaConsumer.accept((PotionMeta) itemMeta);
            } else {
                throw new IllegalStateException("ItemBuilder of type '"+builder.getType()+"' does not have potion meta!");
            }
        });
        return this;
    }

    public static ItemStack FROM_BASE(@NotNull PotionType type, @NotNull Type form , boolean upgraded, boolean extended) {
        if(!POTION_TYPES.containsKey(form))
            throw new UnsupportedVersionException("The potion type '"+type+"' is not supported in this version!");

        return new ItemBuilder(POTION_TYPES.get(form))
                .transformMeta(itemMeta -> {
                    PotionMeta meta = (PotionMeta) itemMeta;
                    meta.setBasePotionData(new PotionData(type,extended,upgraded));
                }).build();
    }
}
