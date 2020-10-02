package sirttas.elementalcraft.block.tile;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TileEntityHelper {

	public static Optional<TileEntity> getTileEntity(@Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return Optional.ofNullable(world.getTileEntity(pos));
	}

	public static <T> Optional<T> getTileEntityAs(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull Class<T> clazz) {
		return getTileEntity(world, pos).filter(clazz::isInstance).map(clazz::cast);
	}
}
