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
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;

public class BlockOreShrine extends AbstractBlockShrine {

	public static final String NAME = "oreshrine";

	private static final VoxelShape BASE = Block.makeCuboidShape(5D, 12D, 5D, 11D, 16D, 11D);

	private static final VoxelShape PIPE_UP_N = Block.makeCuboidShape(7D, 13D, 2D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_UP_S = Block.makeCuboidShape(7D, 13D, 11D, 9D, 15D, 14D);
	private static final VoxelShape PIPE_UP_E = Block.makeCuboidShape(11D, 13D, 7D, 14D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.makeCuboidShape(2D, 13D, 7D, 5D, 15D, 9D);


	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, BASE, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W);

	public BlockOreShrine() {
		super(ElementType.EARTH);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileOreShrine();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}