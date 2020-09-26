package sirttas.elementalcraft.block.shrine.sweet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.shrine.BlockShrine;

public class BlockSweetShrine extends BlockShrine {

	public static final String NAME = "sweetshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(5D, 12D, 5D, 11D, 14D, 11D);

	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);
	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 14D, 7D, 9D, 16D, 9D);

	private static final VoxelShape IRON_NORTH_1 = Block.makeCuboidShape(4D, 9D, 1D, 6D, 11D, 3D);
	private static final VoxelShape IRON_NORTH_2 = Block.makeCuboidShape(10D, 9D, 1D, 12D, 11D, 3D);
	private static final VoxelShape IRON_SOUTH_1 = Block.makeCuboidShape(4D, 9D, 13D, 6D, 11D, 15D);
	private static final VoxelShape IRON_SOUTH_2 = Block.makeCuboidShape(10D, 9D, 13D, 12D, 11D, 15D);
	private static final VoxelShape IRON_WEST_1 = Block.makeCuboidShape(1D, 9D, 4D, 3D, 11D, 6D);
	private static final VoxelShape IRON_WEST_2 = Block.makeCuboidShape(1D, 9D, 10D, 3D, 11D, 12D);
	private static final VoxelShape IRON_EAST_1 = Block.makeCuboidShape(13D, 9D, 4D, 15D, 11D, 6D);
	private static final VoxelShape IRON_EAST_2 = Block.makeCuboidShape(13D, 9D, 10D, 15D, 11D, 12D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST, PIPE_UP, IRON_NORTH_1, IRON_NORTH_2, IRON_SOUTH_1,
			IRON_SOUTH_2, IRON_EAST_1, IRON_EAST_2, IRON_WEST_1, IRON_WEST_2);

	public BlockSweetShrine() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileSweetShrine();
	}


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}