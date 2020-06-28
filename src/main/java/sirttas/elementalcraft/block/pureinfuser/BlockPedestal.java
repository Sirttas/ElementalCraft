package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.BlockECContainer;

public class BlockPedestal extends BlockECContainer {

	public static final String NAME = "pedestal";

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePedestal();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TilePedestal pedestal = (TilePedestal) world.getTileEntity(pos);

		if (pedestal != null) {
			return this.onSlotActivated(pedestal, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}
}
