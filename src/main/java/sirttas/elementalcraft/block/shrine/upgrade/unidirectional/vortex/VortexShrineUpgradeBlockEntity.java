package sirttas.elementalcraft.block.shrine.upgrade.unidirectional.vortex;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;

public class VortexShrineUpgradeBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + VortexShrineUpgradeBlock.NAME) public static final BlockEntityType<VortexShrineUpgradeBlockEntity> TYPE = null;

	public VortexShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

}
