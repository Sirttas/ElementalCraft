package sirttas.elementalcraft.block.pipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementPipeBlock extends AbstractECEntityBlock {

	public static final String NAME = "elementpipe";
	public static final String NAME_IMPAIRED = NAME + "_impaired";
	public static final String NAME_IMPROVED = NAME + "_improved";
	public static final String NAME_CREATIVE = NAME + "_creative";

	public static final EnumProperty<CoverType> COVER = EnumProperty.create("cover", CoverType.class);

	private final PipeType type;

	public ElementPipeBlock(PipeType type) {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().randomTicks());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(COVER, CoverType.NONE));
		this.type = type;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(COVER);
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ElementPipeBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECTicker(level, type, ECBlockEntityTypes.PIPE, level.isClientSide ? ElementPipeBlockEntity::commonTick : ElementPipeBlockEntity::serverTick);
	}

	@Override
	@Deprecated
	public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
		if (level.getBlockEntity(pos) instanceof ElementPipeBlockEntity pipe) {
			pipe.refresh();
		}
	}

	@Override
	@Deprecated
	public void tick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
		if (level.getBlockEntity(pos) instanceof ElementPipeBlockEntity pipe) {
			pipe.refresh();
		}
	}

	public static boolean showCover(BlockState state, Player player) {
		return isCovered(state) && (player == null || EntityHelper.handStream(player).noneMatch(stack -> !stack.isEmpty() && stack.is(ECTags.Items.PIPE_COVER_HIDING)));
	}

	private static boolean isCovered(BlockState state) {
		return state.getValue(COVER) == CoverType.COVERED;
	}

	private VoxelShape getCurrentShape(BlockState state, ElementPipeBlockEntity entity, Player player) {
		if (showCover(state, entity != null ? player : null)) {
			return Shapes.block();
		} else if (entity == null) {
			return Shapes.empty();
		}
		return entity.getShape();
	}

	@Override
	@Deprecated
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		var player = getPlayer(context);
		var blockEntity = getBlockEntity(blockGetter, pos);

		return blockGetter instanceof Level level && level.isClientSide ? getShapeAndFace(state, pos, blockEntity, Minecraft.getInstance().hitResult, player).getFirst() : getCurrentShape(state, blockEntity, player);
	}

	private Player getPlayer(CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof Player player) {
			return player;
		}
		return null;
	}

	public Pair<VoxelShape, Direction> getShapeAndFace(BlockState state, BlockPos pos, ElementPipeBlockEntity pipe, HitResult result, Player player) {
		if (!showCover(state, player) && result instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK && blockHitResult.getBlockPos().equals(pos)) {
			var hit = blockHitResult.getLocation();

			for (Direction face : Direction.values()) {
				var shape = pipe.getShape(face);

				if (ShapeHelper.vectorCollideWithShape(shape, pos, hit)) {
					return Pair.of(shape, face);
				}
			}
			if (ShapeHelper.vectorCollideWithShape(ElementPipeShapes.BASE_SHAPE, pos, hit)) {
				return Pair.of(ElementPipeShapes.BASE_SHAPE, blockHitResult.getDirection());
			} else if (ShapeHelper.vectorCollideWithShape(ElementPipeShapes.FRAME_SHAPE, pos, hit)) {
				return Pair.of(ElementPipeShapes.FRAME_SHAPE, null);
			}
		}
		return Pair.of(getCurrentShape(state, pipe, player), null);
	}

	@Override
	@Deprecated
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return getCurrentShape(state, getBlockEntity(world, pos), null);
	}

	@Override
	@Deprecated
	public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		final ElementPipeBlockEntity pipe = (ElementPipeBlockEntity) level.getBlockEntity(pos);

		if (pipe != null) {
			var pair = getShapeAndFace(state, pos, getBlockEntity(level, pos), hit, player);
			var shape = pair.getFirst();

			if (shape == ElementPipeShapes.FRAME_SHAPE || state.getValue(COVER) == CoverType.FRAME) {
				return pipe.setCover(player, hand);
			} else {
				var face = pair.getSecond();
				var value = onShapeActivated(face, pipe, player, hand, hit);
	
				if (!value.consumesAction()) {
					player.displayClientMessage(pipe.getConnectionMessage(face), true);
					level.updateNeighborsAt(pos, this);
				}
				return value;
			}
		}
		return InteractionResult.PASS;
	}

	private InteractionResult onShapeActivated(Direction face, ElementPipeBlockEntity pipe, Player player, InteractionHand hand, BlockHitResult hit) {
		if (face != null) {
			ItemStack stack = player.getItemInHand(hand);

			if (!stack.isEmpty() && stack.getItem() instanceof IPipeInteractingItem item) {
				return item.useOnPipe(pipe, new UseOnContext(player, hand, new BlockHitResult(hit.getLocation(), face, hit.getBlockPos(), hit.isInside())));
			}
			return pipe.activatePipe(player, face);
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			var te = level.getBlockEntity(pos);

			if (te instanceof ElementPipeBlockEntity pipe) {
				if (isCovered(state)) {
					Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(pipe.getCoverState().getBlock()));
				}
				pipe.removeAllUpgrades();
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	private static ElementPipeBlockEntity getBlockEntity(BlockGetter world, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(world, pos, ElementPipeBlockEntity.class).orElse(null);
	}

	public PipeType getType() {
		return type;
	}

	public enum CoverType implements StringRepresentable {
		NONE("none"), FRAME("frame"), COVERED("covered");

		public static final Codec<CoverType> CODEC = StringRepresentable.fromEnum(CoverType::values);

		private final String name;

		CoverType(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static CoverType byName(String name) {
			for (CoverType bonusType : values()) {
				if (bonusType.name.equals(name)) {
					return bonusType;
				}
			}
			return NONE;
		}
	}

	public enum PipeType {
		IMPAIRED, STANDARD, IMPROVED, CREATIVE;
	}

}
