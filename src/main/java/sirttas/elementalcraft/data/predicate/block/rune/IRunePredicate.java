package sirttas.elementalcraft.data.predicate.block.rune;

import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.rune.capability.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.capability.IRuneHandler;


public interface IRunePredicate extends IBlockPosPredicate {

	boolean test(IRuneHandler handler);

	default Predicate<IRuneHandler> asRunePredicate() {
		return this::test;
	}

	@Override
	default boolean test(IWorldReader world, BlockPos pos) {
		return test(CapabilityRuneHandler.getRuneHandlerAt(world, pos));
	}

}
