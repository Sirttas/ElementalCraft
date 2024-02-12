package sirttas.elementalcraft.block.shrine.overload;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class OverloadShrineBlock extends AbstractShrineBlock<OverloadShrineBlockEntity> {

	public static final String NAME = "overloadshrine";
	public static final MapCodec<OverloadShrineBlock> CODEC = simpleCodec(OverloadShrineBlock::new);

	private static final VoxelShape BASE = Shapes.or(ECShapes.SHRINE_SHAPE, Block.box(5D, 12D, 5D, 11D, 13D, 11D));

	private static final VoxelShape UP_SHAPE = Shapes.or(BASE, Block.box(0D, 13D, 0D, 16D, 16D, 16D));
	private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 0D, 16D, 16D, 3D));
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 13D, 16D, 16D, 16D));
	private static final VoxelShape WEST_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 0D, 3D, 16D, 16D));
	private static final VoxelShape EAST_SHAPE = Shapes.or(BASE, Block.box(13D, 3D, 0D, 16D, 16D, 16D));

	public static final DirectionProperty FACING = DirectionProperty.create("facing", d -> d != Direction.DOWN);

	public OverloadShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.AIR, properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.UP)
				.setValue(WATERLOGGED, false));
	}

	@Override
	protected @NotNull MapCodec<OverloadShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case UP -> UP_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> BASE;
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(WATERLOGGED, FACING);
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		Direction direction = context.getClickedFace().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.UP : direction);
	}
}
