package sirttas.elementalcraft.data.predicate.block.rune;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;


public interface IRunePredicate extends IBlockPosPredicate {

	boolean test(IRuneHandler handler);

	default Predicate<IRuneHandler> asRunePredicate() {
		return this::test;
	}

	@Override
	default boolean test(LevelReader world, BlockPos pos) {
		return test(BlockEntityHelper.getRuneHandlerAt(world, pos));
	}

}
