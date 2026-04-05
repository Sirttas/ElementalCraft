package sirttas.elementalcraft.block.synthesizer.combustion;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CombustionSynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "combustion_fire_synthesizer";
	public static final MapCodec<CombustionSynthesizerBlock> CODEC = simpleCodec(CombustionSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(5D, 1D, 5D, 11D, 2D, 11D);
	private static final VoxelShape BASE_3 = Block.box(2D, 2D, 2D, 14D, 4D, 14D);
	private static final VoxelShape BOWL = Shapes.join(
			Block.box(3D, 4D, 3D, 13D, 7D, 13D),
			Block.box(5D, 4D, 5D, 11D, 7D, 11D),
			BooleanOp.ONLY_FIRST);
	private static final VoxelShape PILLAR_1 = Block.box(4D, 4D, 4D, 6D, 9D, 6D);
	private static final VoxelShape PILLAR_2 = Block.box(10D, 4D, 4D, 12D, 9D, 6D);
	private static final VoxelShape PILLAR_3 = Block.box(4D, 4D, 10D, 6D, 9D, 12D);
	private static final VoxelShape PILLAR_4 = Block.box(10D, 4D, 10D, 12D, 9D, 12D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, BOWL, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4);

	public CombustionSynthesizerBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<? extends CombustionSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new CombustionSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<@NotNull T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<@NotNull T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.COMBUSTION_SYNTHESIZER, CombustionSynthesizerBlockEntity::serverTick);
	}
	
	@Nonnull
    @Override
	protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @NotNull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (stack.isEmpty() || stack.getBurnTime(RecipeType.SMELTING, level.fuelValues()) > 0) {
			return onSingleSlotActivated(stack, level, pos, player, hand);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		AbstractSynthesizerBlockEntity.renderElementFlow(level, pos, rand);
	}
	
	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		return ElementContainer.isValidContainer(state, level, pos.below());
	}
}
