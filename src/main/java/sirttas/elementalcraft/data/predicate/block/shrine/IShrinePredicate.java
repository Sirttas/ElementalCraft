package sirttas.elementalcraft.data.predicate.block.shrine;

import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;


public interface IShrinePredicate extends IBlockPosPredicate {

	boolean test(AbstractShrineBlockEntity shrine);

	default Predicate<AbstractShrineBlockEntity> asShrinePredicate() {
		return this::test;
	}

	@Override
	default boolean test(IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.getTileEntityAs(world, pos, AbstractShrineBlockEntity.class).map(this::test).orElse(false);
	}

}
