package sirttas.elementalcraft.block.shrine.sweet;

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

public class SweetShrineBlock extends AbstractShrineBlock<SweetShrineBlockEntity> {

	public static final String NAME = "sweetshrine";

	private static final VoxelShape BASE = Block.box(5D, 12D, 5D, 11D, 14D, 11D);

	private static final VoxelShape IRON_NORTH_1 = Block.box(4D, 9D, 1D, 6D, 11D, 3D);
	private static final VoxelShape IRON_NORTH_2 = Block.box(10D, 9D, 1D, 12D, 11D, 3D);
	private static final VoxelShape IRON_SOUTH_1 = Block.box(4D, 9D, 13D, 6D, 11D, 15D);
	private static final VoxelShape IRON_SOUTH_2 = Block.box(10D, 9D, 13D, 12D, 11D, 15D);
	private static final VoxelShape IRON_WEST_1 = Block.box(1D, 9D, 4D, 3D, 11D, 6D);
	private static final VoxelShape IRON_WEST_2 = Block.box(1D, 9D, 10D, 3D, 11D, 12D);
	private static final VoxelShape IRON_EAST_1 = Block.box(13D, 9D, 4D, 15D, 11D, 6D);
	private static final VoxelShape IRON_EAST_2 = Block.box(13D, 9D, 10D, 15D, 11D, 12D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE, IRON_NORTH_1, IRON_NORTH_2, IRON_SOUTH_1,
			IRON_SOUTH_2, IRON_EAST_1, IRON_EAST_2, IRON_WEST_1, IRON_WEST_2);

	public SweetShrineBlock() {
		super(ElementType.WATER);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
