package sirttas.elementalcraft.block.synthesizer.cracking;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrackingSynthesizerBlock extends AbstractCrackingSynthesizerBlock {

	public static final String NAME = "cracking_earth_synthesizer";
	public static final MapCodec<CrackingSynthesizerBlock> CODEC = simpleCodec(CrackingSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape BASE_2 = Block.box(7D, 2D, 7D, 9D, 5D, 9D);
	private static final VoxelShape HEAD_1 = Block.box(6D, 5D, 6D, 10D, 11D, 10D);
	private static final VoxelShape HEAD_2 = Block.box(0D, 6D, 0D, 16D, 10D, 16D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, HEAD_1, HEAD_2);

	public CrackingSynthesizerBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<? extends CrackingSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new CrackingSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.CRACKING_SYNTHESIZER, CrackingSynthesizerBlockEntity::serverTick);
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
