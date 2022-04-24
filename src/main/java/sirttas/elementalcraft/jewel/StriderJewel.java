package sirttas.elementalcraft.jewel;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StriderJewel extends Jewel {

    private final TagKey<Fluid> tag;

    public StriderJewel(ElementType elementType, int consumption, TagKey<Fluid> tag) {
        super(elementType, consumption);
        this.tag = tag;
    }

    private boolean isOnFluid(Entity entity) {
        BlockPos blockpos = entity.getOnPos();
        BlockState blockstate = entity.level.getBlockState(blockpos);

        return blockstate.getFluidState().is(tag);
    }

    @Override
    public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
        return isOnFluid(entity) && super.isActive(entity, elementStorage);
    }

    public TagKey<Fluid> getTag() {
        return tag;
    }
}
