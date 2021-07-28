package sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

public class AccelerationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + AccelerationShrineUpgradeBlock.NAME) public static final BlockEntityType<AccelerationShrineUpgradeBlockEntity> TYPE = null;

	public AccelerationShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

}
