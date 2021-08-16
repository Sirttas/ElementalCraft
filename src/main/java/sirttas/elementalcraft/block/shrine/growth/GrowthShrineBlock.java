package sirttas.elementalcraft.block.shrine.growth;

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

public class GrowthShrineBlock extends AbstractShrineBlock<GrowthShrineBlockEntity> {

	public static final String NAME = "growthshrine";

	private static final VoxelShape PIPE_UP_N = Block.box(7D, 12D, 4D, 9D, 15D, 6D);
	private static final VoxelShape PIPE_UP_S = Block.box(7D, 12D, 10D, 9D, 15D, 12D);
	private static final VoxelShape PIPE_UP_E = Block.box(10D, 12D, 7D, 12D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.box(4D, 12D, 7D, 6D, 15D, 9D);


	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W);

	public GrowthShrineBlock() {
		super(ElementType.WATER);
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}