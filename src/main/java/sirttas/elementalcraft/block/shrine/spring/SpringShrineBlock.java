package sirttas.elementalcraft.block.shrine.spring;

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
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

public class SpringShrineBlock extends AbstractShrineBlock {

	public static final String NAME = "springshrine";

	private static final VoxelShape PIPE_NORTH = Block.box(7D, 12D, 4D, 9D, 16D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 12D, 10D, 9D, 16D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(4D, 12D, 7D, 6D, 16D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 12D, 7D, 12D, 16D, 9D);
	
	private static final VoxelShape PIPE_NORTH_WEST = Block.box(4D, 12D, 4D, 6D, 16D, 6D);
	private static final VoxelShape PIPE_NORTH_EAST = Block.box(10D, 12D, 4D, 12D, 16D, 6D);
	private static final VoxelShape PIPE_SOUTH_WEST = Block.box(4D, 12D, 10D, 6D, 16D, 12D);
	private static final VoxelShape PIPE_SOUTH_EAST = Block.box(10D, 12D, 10D, 12D, 16D, 12D);
	
	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST, PIPE_NORTH_WEST, PIPE_NORTH_EAST, PIPE_SOUTH_WEST, PIPE_SOUTH_EAST);

	public SpringShrineBlock() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SpringShrineBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}