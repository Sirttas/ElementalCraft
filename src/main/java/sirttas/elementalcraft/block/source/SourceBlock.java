package sirttas.elementalcraft.block.source;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class SourceBlock extends AbstractECEntityBlock {

	private static final VoxelShape SHAPE = Block.box(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";

	public SourceBlock() {
		super(BlockBehaviour.Properties.of()
				.replaceable()
				.pushReaction(PushReaction.DESTROY)
				.strength(-1.0F, 3600000.0F)
				.lightLevel(s -> 7)
				.noOcclusion()
				.noLootTable());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(ElementType.STATE_PROPERTY, ElementType.NONE));
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new SourceBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.SOURCE, SourceBlockEntity::serverTick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(ElementType.STATE_PROPERTY);
	}

	@Override
	@Deprecated
	public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return showShape(state, context) ? SHAPE : Shapes.empty();
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return Shapes.empty();
	}

	private boolean showShape(BlockState state, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof LivingEntity e) {
			return Stream.of(e.getItemInHand(InteractionHand.MAIN_HAND), e.getItemInHand(InteractionHand.MAIN_HAND))
					.anyMatch(s -> s.getItem() instanceof ISourceInteractable sourceInteractable && sourceInteractable.canInteractWithSource(s, state));
		}
		return false;
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	@Deprecated
	public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
		if (ElementType.getElementType(state) == ElementType.NONE) {
			level.setBlockAndUpdate(pos, state.setValue(ElementType.STATE_PROPERTY, ElementType.random()));
		}
	}

	@Override
	@Deprecated
	public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
		return super.canBeReplaced(state, context) && BlockEntityHelper.getBlockEntityAs(context.getLevel(), context.getClickedPos(), SourceBlockEntity.class)
				.map(s -> !s.isStabilized())
				.orElse(true);
	}

	@Nonnull
	@Override
	@Deprecated
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
