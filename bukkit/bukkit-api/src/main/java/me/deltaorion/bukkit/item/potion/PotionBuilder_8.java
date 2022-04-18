package me.deltaorion.bukkit.item.potion;

import com.google.common.collect.ImmutableMap;
import me.deltaorion.bukkit.plugin.UnsupportedVersionException;
import me.deltaorion.bukkit.item.EMaterial;
import me.deltaorion.bukkit.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PotionBuilder_8 implements PotionBuilder {

    @NotNull private final ItemBuilder builder;
    @NotNull private PotionType type = PotionType.WATER;
    private boolean isSplash = false;

    private final static Map<Type,Boolean> typeMap = ImmutableMap.of(
            Type.DRINKABLE,false,
            Type.SPLASH,true
    );

    public PotionBuilder_8(@NotNull ItemBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
        makePotion();
    }

    private void makePotion() {
        if(!builder.getType().equals(EMaterial.POTION)) {
            builder.setType(EMaterial.POTION);
        } else {
            setSplashAndType();
        }
    }

    private void setSplashAndType() {
        for(PotionType type : PotionType.values()) {
            if(testPotion(type,true))
                break;
            if(testPotion(type,false))
                break;
        }
    }

    private boolean testPotion(PotionType type, boolean isSplash) {
        Potion potion = new Potion(type);
        potion.setSplash(isSplash);
        if(potion.toItemStack(1).getDurability() == builder.build().getDurability()) {
            this.isSplash = isSplash;
            this.type = type;
            return true;
        }
        return false;
    }

    @Override
    public PotionBuilder setType(@NotNull Type type) {
        if(!typeMap.containsKey(type))
            throw new UnsupportedVersionException("The potion type '"+type+"' is not supported in this version!");

        this.isSplash = typeMap.get(type);
        return transformPotion(potion -> {
            potion.setSplash(isSplash);
        });
    }

    @NotNull
    @Override
    public PotionBuilder setColor(@NotNull Color color) {
        throw new UnsupportedVersionException("RGB Potion colors are not supported in 1.8");
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
    public PotionBuilder setColor(@NotNull PotionType type) {
        this.type = type;
        return transformPotion(potion -> {
            potion.setType(Objects.requireNonNull(type));
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
        return transformMeta(PotionMeta::clearCustomEffects);
    }

    @NotNull @Override
    public Collection<PotionEffect> getEffects() {
        PotionMeta meta = (PotionMeta) builder.build().getItemMeta();
        return Collections.unmodifiableList(meta.getCustomEffects());
    }

    @NotNull
    @Override
    public PotionBuilder setMainEffect(@NotNull PotionEffectType type) {
        return transformMeta(potionMeta -> {
            potionMeta.setMainEffect(type);
        });
    }

    private PotionBuilder transformPotion(@NotNull Consumer<Potion> potionConsumer) {
        Potion potion = new Potion(type);
        potion.setSplash(isSplash);
        potionConsumer.accept(potion);
        ItemStack potionStack = potion.toItemStack(1);
        builder.setDurability(potionStack.getDurability());
        builder.setType(potionStack.getType());
        return this;
    }

    private PotionBuilder transformMeta(@NotNull Consumer<PotionMeta> metaConsumer) {
        builder.transformMeta(itemMeta -> {
            if(itemMeta instanceof PotionMeta) {
                metaConsumer.accept((PotionMeta) itemMeta);
            } else {
                throw new IllegalStateException("ItemBuilder of type '"+builder.getType()+"' does not have potion meta!");
            }
        });
        return this;
    }

    public static ItemStack FROM_BASE(@NotNull PotionType type,@NotNull Type form, boolean upgraded, boolean extended) {

        Objects.requireNonNull(type);
        Objects.requireNonNull(form);
        if(upgraded && extended)
            throw new IllegalArgumentException("Potion cannot be upgraded and extended at the same time!");

        if(extended && type.isInstant())
            throw new IllegalArgumentException("Potion '"+type+"' is instant and cannot be extended");

        Potion potion = new Potion(type);
        if(!typeMap.containsKey(form))
            throw new UnsupportedVersionException("The potion type '"+type+"' is not supported in this version!");

        potion.setSplash(typeMap.get(form));
        potion.setLevel(upgraded ? 2 : 1);
        if(!type.isInstant()) {
            potion.setHasExtendedDuration(extended);
        }
        potion.setType(type);
        return potion.toItemStack(1);
    }

}
