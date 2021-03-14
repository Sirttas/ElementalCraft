package sirttas.elementalcraft.block.shrine.grove;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;

public class BlockGroveShrine extends AbstractBlockShrine {

	public static final String NAME = "groveshrine";

	private static final VoxelShape BASE = Block.makeCuboidShape(5D, 12D, 5D, 11D, 14D, 11D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(2D, 7D, 2D, 4D, 16D, 4D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(12D, 7D, 2D, 14D, 16D, 4D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(2D, 7D, 12D, 4D, 16D, 14D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(12D, 7D, 12D, 14D, 16D, 14D);
	private static final VoxelShape PIPE_CENTER = Block.makeCuboidShape(7D, 15D, 7D, 9D, 16D, 9D);

	private static final VoxelShape PLATE = Block.makeCuboidShape(3D, 14D, 3D, 13D, 15D, 13D);

	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, BASE, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_CENTER, PLATE);

	public BlockGroveShrine() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileGroveShrine();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}