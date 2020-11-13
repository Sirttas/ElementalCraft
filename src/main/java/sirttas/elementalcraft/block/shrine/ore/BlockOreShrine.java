package sirttas.elementalcraft.block.shrine.ore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.BlockShrine;

public class BlockOreShrine extends BlockShrine {

	public static final String NAME = "oreshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(5D, 12D, 5D, 11D, 16D, 11D);

	private static final VoxelShape PIPE_UP_N = Block.makeCuboidShape(7D, 13D, 2D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_UP_S = Block.makeCuboidShape(7D, 13D, 11D, 9D, 15D, 14D);
	private static final VoxelShape PIPE_UP_E = Block.makeCuboidShape(11D, 13D, 7D, 14D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.makeCuboidShape(2D, 13D, 7D, 5D, 15D, 9D);

	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST);

	public BlockOreShrine() {
		super(ElementType.EARTH);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileOreShrine();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}