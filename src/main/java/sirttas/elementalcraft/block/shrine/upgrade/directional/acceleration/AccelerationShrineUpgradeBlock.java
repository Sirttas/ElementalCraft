package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;

public class AccelerationShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_acceleration";

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new AccelerationShrineUpgradeBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return CapacityShrineUpgradeBlock.getShape(state);
	}

}
