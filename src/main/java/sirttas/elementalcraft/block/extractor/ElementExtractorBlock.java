package sirttas.elementalcraft.block.extractor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.shape.ShapeHelper;

import javax.annotation.Nonnull;

public class ElementExtractorBlock extends AbstractElementExtractorBlock {

	public static final String NAME = "element_extractor";
	public static final MapCodec<ElementExtractorBlock> CODEC = simpleCodec(ElementExtractorBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 5D, 16D, 3D, 11D);
	private static final VoxelShape BASE_2 = Block.box(0D, 1D, 0D, 4D, 3D, 16D);
	private static final VoxelShape BASE_3 = Block.box(12D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_4 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PILLAR_1 = Shapes.or(
			Block.box(2D, 3D, 6D, 6D, 4D, 10D),
			Block.box(3D, 4D, 7D, 5D, 13D, 9D),
			Block.box(2D, 13D, 6D, 6D, 16D, 10D)
	);
	private static final VoxelShape PILLAR_2 = PILLAR_1.move(8D / 16, 0, 0);

	private static final VoxelShape SIDE_PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 4D, 3D);
	private static final VoxelShape SIDE_PILLAR_2 = SIDE_PILLAR_1.move(12D / 16, 0D, 0D);
	private static final VoxelShape SIDE_PILLAR_3 = SIDE_PILLAR_1.move(0D, 0D, 12D / 16);
	private static final VoxelShape SIDE_PILLAR_4 = SIDE_PILLAR_1.move(12D / 16, 0D, 12D / 16);

	private static final VoxelShape X_SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PILLAR_1, PILLAR_2, SIDE_PILLAR_1, SIDE_PILLAR_2, SIDE_PILLAR_3, SIDE_PILLAR_4);
	private static final VoxelShape Z_SHAPE = ShapeHelper.rotateShape(Direction.NORTH, Direction.EAST, X_SHAPE);

	public ElementExtractorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X)
				.setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	protected @NotNull MapCodec<? extends ElementExtractorBlock> codec() {
		return CODEC;
	}
	
	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_AXIS, context.getHorizontalDirection().getAxis()).setValue(BlockStateProperties.WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
	}

	@Nonnull
	@Override
	public BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return switch (rot) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.getValue(BlockStateProperties.HORIZONTAL_AXIS)) {
                case Z -> state.setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X);
                case X -> state.setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z);
                default -> state;
            };
            default -> state;
        };
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_AXIS);
	}
}
