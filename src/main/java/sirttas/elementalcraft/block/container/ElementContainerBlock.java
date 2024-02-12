package sirttas.elementalcraft.block.container;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;

public class ElementContainerBlock extends AbstractConnectedElementContainerBlock {

	public static final String NAME = "container";
	public static final MapCodec<ElementContainerBlock> CODEC = simpleCodec(ElementContainerBlock::new);

	private static final VoxelShape BASE = Block.box(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape GLASS = Block.box(2D, 2D, 2D, 14D, 15D, 14D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 2D, 1D, 3D, 16D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 2D, 1D, 15D, 16D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 2D, 13D, 3D, 16D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 2D, 13D, 15D, 16D, 15D);

	private static final VoxelShape CONNECTOR = Block.box(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SHAPE = Shapes.or(BASE, GLASS, PIPE_1, PIPE_2, PIPE_3, PIPE_4, CONNECTOR);

	public ElementContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false));
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return Shapes.or(SHAPE, super.getShape(state, level, pos, context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(NORTH, SOUTH, EAST, WEST);
	}

	@Override
	public int getDefaultCapacity() {
		return ECConfig.COMMON.tankCapacity.get();
	}

	@Override
	protected @NotNull MapCodec<? extends ElementContainerBlock> codec() {
		return CODEC;
	}
}
