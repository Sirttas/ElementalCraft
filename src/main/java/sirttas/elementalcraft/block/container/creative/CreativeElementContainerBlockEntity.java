package sirttas.elementalcraft.block.container.creative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;

public class CreativeElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + CreativeElementContainerBlock.NAME) public static final BlockEntityType<CreativeElementContainerBlockEntity> TYPE = null;

	public CreativeElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, CreativeElementStorage::new);
	}

	@Override
	public boolean isSmall() {
		return false;
	}
}
