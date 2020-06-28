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

public class BlockPureInfuser extends BlockECContainer {

	public static final String NAME = "pureinfuser";

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePureInfuser();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TilePureInfuser pureInfuser = (TilePureInfuser) world.getTileEntity(pos);

		if (pureInfuser != null) {
			return this.onSlotActivated(pureInfuser, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}
}