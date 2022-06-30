package sirttas.elementalcraft.jewel.defence;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ArcticHaresJewel extends DefenceJewel {

    public static final String NAME = "arctic_hares";

    public ArcticHaresJewel() {
        super(ElementType.WATER, 10);
        this.ticking = true;
    }

    private boolean isOnSnow(Entity entity) {
        BlockPos blockpos = entity.getOnPos();
        BlockState blockstate = entity.level.getBlockState(blockpos);

        return blockstate.is(Blocks.POWDER_SNOW);
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return (entity.isInPowderSnow || entity.wasInPowderSnow || isOnSnow(entity)) && super.isActive(entity, elementStorage);
    }

    @Override
    public float onHurt(Entity entity, DamageSource source, float amount) {
        return source == DamageSource.FREEZE ? 0 : super.onHurt(entity, source, amount);
    }

    @Override
    public void appendHoverText(List<Component> tooltip) {
        tooltip.add(Component.translatable("tooltip.elementalcraft.arctic_hares").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(tooltip);
    }
}
