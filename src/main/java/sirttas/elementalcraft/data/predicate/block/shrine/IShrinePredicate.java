package sirttas.elementalcraft.data.predicate.block.shrine;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;


public interface IShrinePredicate extends IBlockPosPredicate {

	boolean test(AbstractShrineBlockEntity shrine);

	default Predicate<AbstractShrineBlockEntity> asShrinePredicate() {
		return this::test;
	}

	@Override
	default boolean test(LevelReader world, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(world, pos, AbstractShrineBlockEntity.class).map(this::test).orElse(false);
	}

}
