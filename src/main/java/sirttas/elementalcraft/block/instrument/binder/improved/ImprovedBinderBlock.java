package sirttas.elementalcraft.block.instrument.binder.improved;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImprovedBinderBlock extends BinderBlock {

	public static final String NAME = "binder_improved";
	public static final MapCodec<ImprovedBinderBlock> CODEC = simpleCodec(ImprovedBinderBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape BASE_2 = Block.box(2D, 2D, 2D, 14D, 5D, 14D);
	private static final VoxelShape PLATE = Block.box(2D, 12D, 2D, 14D, 13D, 14D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 2D, 1D, 3D, 14D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 2D, 1D, 15D, 14D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 2D, 13D, 3D, 14D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 2D, 13D, 15D, 14D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PLATE, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public ImprovedBinderBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<ImprovedBinderBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ImprovedBinderBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.BINDER_IMPROVED);
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
