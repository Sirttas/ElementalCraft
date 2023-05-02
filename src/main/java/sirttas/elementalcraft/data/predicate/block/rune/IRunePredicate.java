package sirttas.elementalcraft.data.predicate.block.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;


public interface IRunePredicate extends IBlockPosPredicate {

	boolean test(IRuneHandler handler);

	default Predicate<IRuneHandler> asRunePredicate() {
		return this::test;
	}

	@Override
	default boolean test(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nullable Direction direction) {
		return test(BlockEntityHelper.getRuneHandlerAt(level, pos, direction));
	}

}
