package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.sound.ECSounds;

public class ElementContainerBlockEntity extends AbstractElementContainerBlockEntity {


	public ElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.CONTAINER, pos, state, self -> new ElementContainerElementStorage((ElementContainerBlockEntity) self, state.getBlock() == ECBlocks.SMALL_CONTAINER.get() ? ECConfig.COMMON.tankSmallCapacity.get() : ECConfig.COMMON.tankCapacity.get()));
	}

	@Override
	public boolean isSmall() {
		return this.getBlockState().getBlock() == ECBlocks.SMALL_CONTAINER.get();
	}

	public void onWrongElementInserted() {
		if (level != null && !level.isClientSide && level.getGameTime() % 20 == 0) {
			level.playSound(null, worldPosition, ECSounds.ELEMENT_CRACKLING.get(), SoundSource.BLOCKS, 0.5F, 0.5F + level.random.nextFloat());
		}
	}
}
