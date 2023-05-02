package sirttas.elementalcraft.data.predicate.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPipePredicate extends IBlockPosPredicate {

    boolean test(@Nonnull ElementPipeBlockEntity pipe, @Nullable Direction direction);

    @Override
    default boolean test(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction) {
        return BlockEntityHelper.getBlockEntityAs(level, pos, ElementPipeBlockEntity.class).map(p -> test(p, direction)).orElse(false);
    }

}
