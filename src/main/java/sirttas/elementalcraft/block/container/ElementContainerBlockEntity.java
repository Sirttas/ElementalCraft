package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;

public class ElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + ElementContainerBlock.NAME) public static final BlockEntityType<ElementContainerBlockEntity> TYPE = null;

	public ElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, sync -> new ElementContainerElementStorage(state.getBlock() == ECBlocks.SMALL_CONTAINER ? ECConfig.COMMON.tankSmallCapacity.get() : ECConfig.COMMON.tankCapacity.get(), sync));
	}

	@Override
	public boolean isSmall() {
		return this.getBlockState().getBlock() == ECBlocks.SMALL_CONTAINER;
	}
}
