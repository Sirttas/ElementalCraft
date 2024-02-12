package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FireBlastFurnaceBlock extends AbstractFireFurnaceBlock {

	public static final String NAME = "fireblastfurnace";
	public static final MapCodec<FireBlastFurnaceBlock> CODEC = simpleCodec(FireBlastFurnaceBlock::new);

	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape OVEN_GLASS = Block.box(2D, 4D, 2D, 14D, 10D, 14D);
	private static final VoxelShape TOP_BOWL = Block.box(3D, 11D, 3D, 13D, 12D, 13D);
	private static final VoxelShape OVEN = Shapes.join(Shapes.or(OVEN_SLAB, OVEN_SLAB_2, OVEN_GLASS), TOP_BOWL, BooleanOp.ONLY_FIRST);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.box(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.box(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.box(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.box(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape SHAPE = Shapes.or(OVEN, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	public FireBlastFurnaceBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<FireBlastFurnaceBlock> codec() {
		return CODEC;
	}
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new FireBlastFurnaceBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.FIRE_BLAST_FURNACE);
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
