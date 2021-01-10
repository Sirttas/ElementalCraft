package sirttas.elementalcraft.data.predicate.block.shrine;

import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;


public interface IShrinePredicate extends IBlockPosPredicate {

	boolean test(TileShrine shrine);

	default Predicate<TileShrine> asShrinePredicate() {
		return this::test;
	}

	@Override
	default boolean test(IWorldReader world, BlockPos pos) {
		return TileEntityHelper.getTileEntityAs(world, pos, TileShrine.class).map(this::test).orElse(false);
	}

}
