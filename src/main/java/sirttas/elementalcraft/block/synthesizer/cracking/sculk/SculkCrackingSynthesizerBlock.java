package sirttas.elementalcraft.block.synthesizer.cracking.sculk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.cracking.AbstractCrackingSynthesizerBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SculkCrackingSynthesizerBlock extends AbstractCrackingSynthesizerBlock {

	public static final String NAME = "sculk_cracking_earth_synthesizer";
	public static final MapCodec<SculkCrackingSynthesizerBlock> CODEC = simpleCodec(SculkCrackingSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_3 = Block.box(7D, 3D, 7D, 9D, 5D, 9D);

	private static final VoxelShape SIDE_PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 4D, 3D);
	private static final VoxelShape SIDE_PILLAR_2 = SIDE_PILLAR_1.move(12D / 16, 0D, 0D);
	private static final VoxelShape SIDE_PILLAR_3 = SIDE_PILLAR_1.move(0D, 0D, 12D / 16);
	private static final VoxelShape SIDE_PILLAR_4 = SIDE_PILLAR_1.move(12D / 16, 0D, 12D / 16);

	private static final VoxelShape HEAD_1 = Block.box(6D, 5D, 6D, 10D, 11D, 10D);
	private static final VoxelShape HEAD_2 = Block.box(0D, 6D, 0D, 16D, 10D, 16D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, SIDE_PILLAR_1, SIDE_PILLAR_2, SIDE_PILLAR_3, SIDE_PILLAR_4, HEAD_1, HEAD_2);

	public SculkCrackingSynthesizerBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<? extends SculkCrackingSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new SculkCrackingSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.SCULK_CRACKING_SYNTHESIZER, SculkCrackingSynthesizerBlockEntity::serverTick);
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
