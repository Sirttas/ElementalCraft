package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SalmonJewel extends EffectJewel {

    public static final String NAME = "salmon";

    public SalmonJewel() {
        super(ElementType.WATER,10, new MobEffectInstance(MobEffects.WATER_BREATHING, 2));
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return entity.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value()) && !entity.level().getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN) && super.isActive(entity, elementStorage);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.salmon").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
