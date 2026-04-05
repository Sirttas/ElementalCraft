package sirttas.elementalcraft.block.pipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementPipeBlock extends AbstractECEntityBlock {

	public static final String NAME = "elementpipe";
	public static final String NAME_RUDIMENTARY = NAME + "_rudimentary";
	public static final String NAME_IMPROVED = NAME + "_improved";
	public static final String NAME_CREATIVE = NAME + "_creative";

	public static final MapCodec<ElementPipeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			PipeType.CODEC.fieldOf("pipe_type").forGetter(p -> p.type),
			propertiesCodec()
	).apply(instance, ElementPipeBlock::new));

	private final PipeType type;

	public ElementPipeBlock(PipeType type, BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(CoverType.PROPERTY, CoverType.NONE));
		this.type = type;
	}

	@Override
	protected @NotNull MapCodec<ElementPipeBlock> codec() {
		return CODEC;
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<@NotNull Block, @NotNull BlockState> container) {
		container.add(CoverType.PROPERTY);
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ElementPipeBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<@NotNull T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<@NotNull T> type) {
		return createECTicker(level, type, ECBlockEntityTypes.PIPE, level.isClientSide() ? ElementPipeBlockEntity::commonTick : ElementPipeBlockEntity::serverTick);
	}

	@Override
	public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
		if (level.getBlockEntity(pos) instanceof ElementPipeBlockEntity pipe) {
			pipe.refresh();
		}
	}

	private VoxelShape getCurrentShape(ElementPipeBlockEntity entity, Player player) {
		if (entity.showCover(player)) {
			return Shapes.block();
		}
		return entity.getShape();
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		var player = getPlayer(context);
		var blockEntity = getBlockEntity(blockGetter, pos);

        if (blockEntity == null) {
            return Shapes.empty();
        }
		return blockGetter instanceof Level level && level.isClientSide() ? getShapeAndFace(pos, blockEntity, Minecraft.getInstance().hitResult, player).getFirst() : getCurrentShape(blockEntity, player);
	}

	private Player getPlayer(CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext && entityContext.getEntity() instanceof Player player) {
			return player;
		}
		return null;
	}

	public Pair<VoxelShape, Direction> getShapeAndFace(BlockPos pos, ElementPipeBlockEntity pipe, HitResult result, Player player) {
        if (!pipe.showCover(player) && result instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK && blockHitResult.getBlockPos().equals(pos)) {
			var hit = blockHitResult.getLocation();

			for (Direction face : Direction.values()) {
				var shape = pipe.getShape(face);

				if (ShapeHelper.vectorCollideWithShape(shape, pos, hit)) {
					return Pair.of(shape, face);
				}
			}
			if (ShapeHelper.vectorCollideWithShape(ElementPipeShapes.BASE_SHAPE, pos, hit)) {
				return Pair.of(ElementPipeShapes.BASE_SHAPE, blockHitResult.getDirection());
			} else if (ShapeHelper.vectorCollideWithShape(ECShapes.COVER_FRAME_SHAPE, pos, hit)) {
				return Pair.of(ECShapes.COVER_FRAME_SHAPE, null);
			}
		}
		return Pair.of(getCurrentShape(pipe, player), null);
	}

    @Override
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var blockEntity = getBlockEntity(level, pos);

        if (blockEntity == null) {
            return Shapes.empty();
        }
		return getCurrentShape(blockEntity, null);
	}

	@Override
	protected @NotNull InteractionResult useItemOn(@Nonnull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (stack.is(ECItems.COVER_FRAME.get()) && !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

		final ElementPipeBlockEntity pipe = (ElementPipeBlockEntity) level.getBlockEntity(pos);

		if (pipe != null) {
			var pair = getShapeAndFace(pos, getBlockEntity(level, pos), hit, player);
			var shape = pair.getFirst();

			if (shape == ECShapes.COVER_FRAME_SHAPE || state.getValue(CoverType.PROPERTY) == CoverType.FRAME) {
				return pipe.putCover(player, hand);
			} else if (shape == ElementPipeShapes.BASE_SHAPE) {
				var value = upgrade(state, level, pos, player, hand);

				if (value.consumesAction()) {
					return value;
				}
			}

			var face = pair.getSecond();
			var value = onShapeActivated(face, pipe, player, hand, hit);

			if (!value.consumesAction()) {
				player.displayClientMessage(pipe.getConnectionMessage(face), true);
				level.updateNeighborsAt(pos, this);
			}
			return value;
		}
		return InteractionResult.PASS;
	}

	private InteractionResult upgrade(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand) {
		if (!state.is(this)) {
			return InteractionResult.PASS;
		}

		var stack = player.getItemInHand(hand);

		if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem item) || !(item.getBlock() instanceof ElementPipeBlock block) || block.type.getTiers() <= type.getTiers()) {
			return InteractionResult.PASS;
		}

		var oldBlockEntity = getBlockEntity(level, pos);

		level.setBlockAndUpdate(pos, block.defaultBlockState().setValue(CoverType.PROPERTY, state.getValue(CoverType.PROPERTY)));

		var newBlockEntity = getBlockEntity(level, pos);

		if (oldBlockEntity != null && newBlockEntity != null) {
			oldBlockEntity.copyTo(newBlockEntity);
			newBlockEntity.refresh();
		}

		if (!player.getAbilities().instabuild) {
			stack.shrink(1);
			EntityHelper.dropAtFeet(level, player, new ItemStack(state.getBlock()));
		}
		level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
		for (Direction face : Direction.values()) { // TODO delay by 5 ticks
			var p = pos.relative(face);

			this.upgrade(level.getBlockState(p), level, p, player, hand);
		}
		return InteractionResult.SUCCESS;
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
	
	private static ElementPipeBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
		return BlockEntityHelper.getBlockEntityAs(level, pos, ElementPipeBlockEntity.class).orElse(null);
	}

	public PipeType getType() {
		return type;
	}

    public enum PipeType implements StringRepresentable {
		RUDIMENTARY("rudimentary", 0),
		STANDARD("standard", 1),
		IMPROVED("improved", 2),
		CREATIVE("creative", 3);

		private static final Codec<PipeType> CODEC = StringRepresentable.fromEnum(PipeType::values);

		private final int tiers;
		private final String name;

		PipeType(String name, int tiers) {
			this.name = name;
			this.tiers = tiers;
		}

		@Override
		public @NotNull String getSerializedName() {
			return name;
		}

		public int getTiers() {
			return tiers;
		}
	}

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        var pipe = getBlockEntity(level, pos);

        for (PipeUpgrade upgrade : pipe.getUpgrades().values()) {
            upgrade.animateTick(level, pos, rand);
        }
    }
}
