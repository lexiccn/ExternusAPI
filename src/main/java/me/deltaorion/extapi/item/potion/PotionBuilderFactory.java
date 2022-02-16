package me.deltaorion.extapi.item.potion;

import me.deltaorion.extapi.common.exception.UnsupportedVersionException;
import me.deltaorion.extapi.item.EMaterial;
import me.deltaorion.extapi.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public interface PotionBuilderFactory {

    public PotionBuilder get(@NotNull ItemBuilder builder);

    public ItemStack fromBase(@NotNull PotionType type,@NotNull PotionBuilder.Type form, boolean upgraded, boolean extended);

    public PotionBuilderFactory BY_VERSION = new PotionBuilderFactory() {
        @Override
        public PotionBuilder get(@NotNull ItemBuilder builder) {
            if(EMaterial.getVersion().getMajor()==8) {
                return new PotionBuilder_8(builder);
            } else if(EMaterial.getVersion().getMajor()>=9 && EMaterial.getVersion().getMajor()<=10) {
                return new PotionBuilder_9_10(builder);
            } else if(EMaterial.getVersion().getMajor()>=11) {
                return new PotionBuilder_11(builder);
            }
            throw new UnsupportedVersionException("Cannot find an appropiate Potion Builder for this version!");
        }

        @Override
        public ItemStack fromBase(@NotNull PotionType type, @NotNull PotionBuilder.Type form, boolean upgraded, boolean extended) {
            if(EMaterial.getVersion().getMajor()==8) {
                return PotionBuilder_8.FROM_BASE(type,form,upgraded,extended);
            } else if(EMaterial.getVersion().getMajor()>=9) {
                return ModernPotionBuilder.FROM_BASE(type,form,upgraded,extended);
            }
            throw new UnsupportedVersionException("Cannot find an appropriate Potion Builder for this version!");
        }
    };

}
