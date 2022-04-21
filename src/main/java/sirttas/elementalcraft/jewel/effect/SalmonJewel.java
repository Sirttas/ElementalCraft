package sirttas.elementalcraft.jewel.effect;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.jewel.effect.EffectJewel;

import java.util.List;

public class SalmonJewel extends EffectJewel {

    public static final String NAME = "salmon";

    public SalmonJewel() {
        super(ElementType.WATER,10, new MobEffectInstance(MobEffects.WATER_BREATHING, 2));
    }

    @Override
    public boolean isActive(Entity entity) {
        return entity.isEyeInFluid(FluidTags.WATER) && !entity.level.getBlockState(new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN) && super.isActive(entity);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(new TranslatableComponent("tooltip.elementalcraft.salmon").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }

}
