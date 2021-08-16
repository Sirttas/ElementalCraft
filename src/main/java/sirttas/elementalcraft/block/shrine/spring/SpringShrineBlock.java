package sirttas.elementalcraft.block.shrine.spring;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

public class SpringShrineBlock extends AbstractShrineBlock<SpringShrineBlockEntity> {

	public static final String NAME = "springshrine";

	private static final VoxelShape PIPE_NORTH = Block.box(7D, 12D, 4D, 9D, 16D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 12D, 10D, 9D, 16D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(4D, 12D, 7D, 6D, 16D, 9D);
	private static final VoxelShape PIPE_EAST = Block.box(10D, 12D, 7D, 12D, 16D, 9D);
	
	private static final VoxelShape PIPE_NORTH_WEST = Block.box(4D, 12D, 4D, 6D, 16D, 6D);
	private static final VoxelShape PIPE_NORTH_EAST = Block.box(10D, 12D, 4D, 12D, 16D, 6D);
	private static final VoxelShape PIPE_SOUTH_WEST = Block.box(4D, 12D, 10D, 6D, 16D, 12D);
	private static final VoxelShape PIPE_SOUTH_EAST = Block.box(10D, 12D, 10D, 12D, 16D, 12D);
	
	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST, PIPE_NORTH_WEST, PIPE_NORTH_EAST, PIPE_SOUTH_WEST, PIPE_SOUTH_EAST);

	public SpringShrineBlock() {
		super(ElementType.WATER);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}