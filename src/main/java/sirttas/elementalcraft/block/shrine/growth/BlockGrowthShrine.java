package sirttas.elementalcraft.block.shrine.growth;

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

public class BlockGrowthShrine extends AbstractBlockShrine {

	public static final String NAME = "growthshrine";

	private static final VoxelShape PIPE_UP_N = Block.makeCuboidShape(7D, 12D, 4D, 9D, 15D, 6D);
	private static final VoxelShape PIPE_UP_S = Block.makeCuboidShape(7D, 12D, 10D, 9D, 15D, 12D);
	private static final VoxelShape PIPE_UP_E = Block.makeCuboidShape(10D, 12D, 7D, 12D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.makeCuboidShape(4D, 12D, 7D, 6D, 15D, 9D);


	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W);

	public BlockGrowthShrine() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileGrowthShrine();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}