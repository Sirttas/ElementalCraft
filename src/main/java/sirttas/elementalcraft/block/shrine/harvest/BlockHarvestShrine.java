package sirttas.elementalcraft.block.shrine.harvest;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BoneMealItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.shrine.TileShrine;

public class BlockHarvestShrine extends BlockECTileProvider {

	public static final String NAME = "harvestshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 13D, 0D, 16D, 16D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 9D, 1D, 15D, 13D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 4D, 3D, 13D, 9D, 13D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 2D, 1D, 3D, 9D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 2D, 1D, 15D, 9D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 2D, 13D, 3D, 9D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 2D, 13D, 15D, 9D, 15D);

	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileHarvestShrine();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileShrine shrine = (TileShrine) world.getTileEntity(pos);

		if (shrine != null && shrine.isRunning()) {
			BoneMealItem.spawnBonemealParticles(world, pos, 1);
		}
	}
}