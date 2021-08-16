package sirttas.elementalcraft.block.shrine.ore;

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

public class OreShrineBlock extends AbstractShrineBlock<OreShrineBlockEntity> {

	public static final String NAME = "oreshrine";

	private static final VoxelShape BASE = Block.box(5D, 12D, 5D, 11D, 16D, 11D);

	private static final VoxelShape PIPE_UP_N = Block.box(7D, 13D, 2D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_UP_S = Block.box(7D, 13D, 11D, 9D, 15D, 14D);
	private static final VoxelShape PIPE_UP_E = Block.box(11D, 13D, 7D, 14D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.box(2D, 13D, 7D, 5D, 15D, 9D);


	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W);

	public OreShrineBlock() {
		super(ElementType.EARTH);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}