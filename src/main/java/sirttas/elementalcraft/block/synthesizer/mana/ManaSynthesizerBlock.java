package sirttas.elementalcraft.block.synthesizer.mana;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlock;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.botania.ManaSynthesizerBlockInteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManaSynthesizerBlock extends SolarSynthesizerBlock {

	public static final String NAME = "mana_synthesizer";
	public static final MapCodec<ManaSynthesizerBlock> CODEC = simpleCodec(ManaSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Shapes.join(Block.box(2D, 3D, 2D, 14D, 5D, 14D), Block.box(6D, 4D, 6D, 10D, 5D, 10D), BooleanOp.ONLY_FIRST);
	private static final VoxelShape BASE_3 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 0D, 1D, 3D, 6D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 0D, 1D, 15D, 6D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 0D, 13D, 3D, 6D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 0D, 13D, 15D, 6D, 15D);

	private static final VoxelShape PIPE_5 = Block.box(7D, 5D, 3D, 9D, 8D, 5D);
	private static final VoxelShape PIPE_6 = Block.box(7D, 5D, 11D, 9D, 8D, 13D);
	private static final VoxelShape PIPE_7 = Block.box(3D, 5D, 7D, 5D, 8D, 9D);
	private static final VoxelShape PIPE_8 = Block.box(11D, 5D, 7D, 13D, 8D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5, PIPE_6, PIPE_7, PIPE_8);

	public ManaSynthesizerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<ManaSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		if (ECinteractions.isBotaniaActive()) {
			return ManaSynthesizerBlockInteractions.newBlockEntity(pos, state);
		}
		return null;
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		if (ECinteractions.isBotaniaActive()) {
			return ManaSynthesizerBlockInteractions.getTicker(level, type);
		}
		return null;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (ECinteractions.isBotaniaActive()) {
			ManaSynthesizerBlockInteractions.animateTick(level, pos, rand);
		}
	}
}
