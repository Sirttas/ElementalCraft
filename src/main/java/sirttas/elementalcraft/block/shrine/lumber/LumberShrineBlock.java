package sirttas.elementalcraft.block.shrine.lumber;

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

import javax.annotation.Nonnull;

public class LumberShrineBlock extends AbstractShrineBlock<LumberShrineBlockEntity> {

	public static final String NAME = "lumbershrine";

	private static final VoxelShape PIPE_1 = Block.box(1D, 7D, 1D, 3D, 14D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 7D, 1D, 15D, 14D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 7D, 13D, 3D,  14D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 7D, 13D, 15D, 14D, 15D);


	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public LumberShrineBlock() {
		super(ElementType.EARTH);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
