package sirttas.elementalcraft.data.predicate.block.shrine;

import java.util.function.Predicate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;


public interface IShrinePredicate extends IBlockPosPredicate {

	boolean test(AbstractTileShrine shrine);

	default Predicate<AbstractTileShrine> asShrinePredicate() {
		return this::test;
	}

	@Override
	default boolean test(IWorldReader world, BlockPos pos) {
		return TileEntityHelper.getTileEntityAs(world, pos, AbstractTileShrine.class).map(this::test).orElse(false);
	}

}
