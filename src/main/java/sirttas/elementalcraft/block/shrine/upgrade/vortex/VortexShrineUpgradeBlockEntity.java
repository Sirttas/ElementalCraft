package sirttas.elementalcraft.block.shrine.upgrade.vortex;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

public class VortexShrineUpgradeBlockEntity extends AbstractECBlockEntity {

	public VortexShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.VORTEX_SHRINE_UPGRADE, pos, state);
	}

}
