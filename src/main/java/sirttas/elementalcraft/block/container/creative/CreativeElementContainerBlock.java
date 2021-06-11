package sirttas.elementalcraft.block.container.creative;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.container.ElementContainerBlock;

public class CreativeElementContainerBlock extends ElementContainerBlock {

	public static final String NAME = "tank_creative"; // TODO 1.17 rename "container_creative"

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CreativeElementContainerBlockEntity();
	}
	
	@Override
	protected int getDefaultCapacity() {
		return 1000000;
	}
}
