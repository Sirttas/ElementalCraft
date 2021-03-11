package sirttas.elementalcraft.block.tank.creative;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.tank.BlockTank;

public class BlockTankCreative extends BlockTank {

	public static final String NAME = "tank_creative"; // TODO 1.17 rename "container_creative"

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileTankCreative();
	}
	
	@Override
	protected int getDefaultCapacity() {
		return 1000000;
	}
}
