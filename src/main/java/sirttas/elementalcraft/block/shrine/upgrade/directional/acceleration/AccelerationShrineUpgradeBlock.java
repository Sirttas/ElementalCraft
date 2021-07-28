package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;

public class AccelerationShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock implements EntityBlock {

	public static final String NAME = "shrine_upgrade_acceleration";

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AccelerationShrineUpgradeBlockEntity(pos, state);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return CapacityShrineUpgradeBlock.getShape(state);
	}

}
