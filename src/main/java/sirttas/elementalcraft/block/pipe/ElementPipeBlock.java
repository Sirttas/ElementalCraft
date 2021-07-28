package sirttas.elementalcraft.block.pipe;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class ElementPipeBlock extends AbstractECEntityBlock {

	public static final String NAME = "elementpipe";
	public static final String NAME_IMPAIRED = NAME + "_impaired";
	public static final String NAME_IMPROVED = NAME + "_improved";

	private static final VoxelShape BASE_SHAPE = Block.box(6.5D, 6.5D, 6.5D, 9.5D, 9.5D, 9.5D);
	private static final VoxelShape WEST_SHAPE = Block.box(0, 7D, 7D, 6.5D, 9D, 9D);
	private static final VoxelShape DOWN_SHAPE = Block.box(7D, 0, 7D, 9D, 6.5D, 9D);
	private static final VoxelShape NORTH_SHAPE = Block.box(7D, 7D, 0, 9D, 9D, 6.5D);
	private static final VoxelShape EAST_SHAPE = Block.box(9.5D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape UP_SHAPE = Block.box(7D, 9.5D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SOUTH_SHAPE = Block.box(7D, 7D, 9.5D, 9D, 9D, 16D);

	private static final VoxelShape FRAME_SHAPE = Shapes.join(Shapes.block(),
			Shapes.or(Block.box(0D, 1D, 1D, 16D, 15D, 15D), Block.box(1D, 0D, 1D, 15D, 16D, 15D), Block.box(1D, 1D, 0D, 15D, 15D, 16D)),
			BooleanOp.ONLY_FIRST);
	
	private static final List<VoxelShape> SHAPES = ImmutableList.of(EAST_SHAPE, NORTH_SHAPE, WEST_SHAPE, SOUTH_SHAPE, UP_SHAPE, DOWN_SHAPE, BASE_SHAPE, FRAME_SHAPE);

	public static final EnumProperty<CoverType> COVER = EnumProperty.create("cover", CoverType.class);

	private final int maxTransferAmount;

	public ElementPipeBlock(int maxTransferAmount) {
		super(BlockBehaviour.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).noOcclusion().randomTicks());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(COVER, CoverType.NONE));
		this.maxTransferAmount = maxTransferAmount;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(COVER);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ElementPipeBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, ElementPipeBlockEntity.TYPE, level.isClientSide ? ElementPipeBlockEntity::commonTick : ElementPipeBlockEntity::serverTick);
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		((ElementPipeBlockEntity) worldIn.getBlockEntity(pos)).refresh();
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		((ElementPipeBlockEntity) worldIn.getBlockEntity(pos)).refresh();
	}

	private boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return shape1.bounds().equals(shape2.bounds());
	}

	private Direction getFace(VoxelShape shape, BlockHitResult hit) {
		if (compareShapes(shape, DOWN_SHAPE)) {
			return Direction.DOWN;
		} else if (compareShapes(shape, UP_SHAPE)) {
			return Direction.UP;
		} else if (compareShapes(shape, NORTH_SHAPE)) {
			return Direction.NORTH;
		} else if (compareShapes(shape, SOUTH_SHAPE)) {
			return Direction.SOUTH;
		} else if (compareShapes(shape, WEST_SHAPE)) {
			return Direction.WEST;
		} else if (compareShapes(shape, EAST_SHAPE)) {
			return Direction.EAST;
		} else if (shape == BASE_SHAPE) {
			return hit.getDirection();
		}
		return null;
	}
	
	public static boolean showCover(BlockState state, Player player) {
		return isCovered(state)
				&& (player == null || !player.getMainHandItem().isEmpty() && EntityHelper.handStream(player).noneMatch(stack -> ECTags.Items.PIPE_COVER_HIDING.contains(stack.getItem())));
	}

	private static boolean isCovered(BlockState state) {
		return state.getValue(COVER) == CoverType.COVERED;
	}

	private boolean isRendered(VoxelShape shape, ElementPipeBlockEntity entity, BlockState state) {
		return state.is(this) && entity != null && (compareShapes(shape, BASE_SHAPE) 
				|| (compareShapes(shape, DOWN_SHAPE) && entity.getConection(Direction.DOWN).isConnected())
				|| (compareShapes(shape, UP_SHAPE) && entity.getConection(Direction.UP).isConnected()) 
				|| (compareShapes(shape, NORTH_SHAPE) && entity.getConection(Direction.NORTH).isConnected())
				|| (compareShapes(shape, SOUTH_SHAPE) && entity.getConection(Direction.SOUTH).isConnected()) 
				|| (compareShapes(shape, WEST_SHAPE) && entity.getConection(Direction.WEST).isConnected())
				|| (compareShapes(shape, EAST_SHAPE) && entity.getConection(Direction.EAST).isConnected())
				|| (compareShapes(shape, FRAME_SHAPE) && state.getValue(COVER) == CoverType.FRAME));
	}

	private VoxelShape getCurentShape(BlockState state, ElementPipeBlockEntity entity, Player player) {
		VoxelShape result = Shapes.empty();

		if (showCover(state, player)) {
			return Shapes.block();
		}
		for (final VoxelShape shape : SHAPES) {
			if (isRendered(shape, entity, state)) {
				result = Shapes.or(result, shape);
			}
		}
		return result;
	}

	@Override
	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		if (isCovered(state)) {
			return RenderShape.ENTITYBLOCK_ANIMATED;
		}
		return super.getRenderShape(state);
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Player player = getPlayer(context);
		ElementPipeBlockEntity blockEntity = getBlockEntity(world, pos);

		return world instanceof Level && ((Level) world).isClientSide ? getShape(state, pos, blockEntity, Minecraft.getInstance().hitResult, player)
				: getCurentShape(state, blockEntity, player);
	}

	private Player getPlayer(CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext) {
			var opt = entityContext.getEntity()
					.filter(Player.class::isInstance)
					.map(Player.class::cast);
		
			if (opt.isPresent()) {
				return opt.get();
			}
		}
		return ElementalCraft.PROXY.getDefaultPlayer();
	}

	public VoxelShape getShape(BlockState state, BlockPos pos, ElementPipeBlockEntity blockEntity, HitResult result, Player player) {
		if (!showCover(state, player) && result != null && result.getType() == HitResult.Type.BLOCK && ((BlockHitResult) result).getBlockPos().equals(pos)) {
			final Vec3 hit = result.getLocation();

			for (final VoxelShape shape : SHAPES) {
				if (isRendered(shape, blockEntity, state) && ShapeHelper.vectorCollideWithShape(shape, pos, hit)) {
					return shape;
				}
			}
		}
		return getCurentShape(state, blockEntity, player);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getCurentShape(state, getBlockEntity(world, pos), null);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final ElementPipeBlockEntity pipe = (ElementPipeBlockEntity) world.getBlockEntity(pos);

		if (pipe != null) {
			final VoxelShape shape = getShape(state, pos, getBlockEntity(world, pos), hit, player);
			
			if (compareShapes(shape, FRAME_SHAPE) || state.getValue(COVER) == CoverType.FRAME) {
				return pipe.setCover(player, hand);
			} else {
				Direction face = getFace(shape, hit);
				InteractionResult value = onShapeActivated(face, pipe, player, hand);
	
				if (value != InteractionResult.PASS) {
					player.displayClientMessage(pipe.getConnectionMessage(face), true);
				}
				return value;
			}
		}
		return InteractionResult.PASS;
	}

	private InteractionResult onShapeActivated(Direction face, ElementPipeBlockEntity pipe, Player player, InteractionHand hand) {
		if (face != null) {
			ItemStack stack = player.getItemInHand(hand);
			
			if (!stack.isEmpty() && stack.getItem() == ECItems.PIPE_PRIORITY) {
				return pipe.activatePriority(face, player, hand);
			}
			return pipe.activatePipe(face);
		}
		return InteractionResult.PASS;
	}
	
	@Override
	@Deprecated
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			ElementPipeBlockEntity te = (ElementPipeBlockEntity) worldIn.getBlockEntity(pos);
			
			if (isCovered(state)) {
				Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack((te).getCoverState().getBlock()));
			}
			te.dropAllPriorities();
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
	
	private static ElementPipeBlockEntity getBlockEntity(BlockGetter world, BlockPos pos) {
		return BlockEntityHelper.getTileEntityAs(world, pos, ElementPipeBlockEntity.class).orElse(null);
	}
	
	public int getMaxTransferAmount() {
		return maxTransferAmount;
	}

	public enum CoverType implements StringRepresentable {
		NONE("none"), FRAME("frame"), COVERED("covered");

		public static final Codec<CoverType> CODEC = StringRepresentable.fromEnum(CoverType::values, CoverType::byName);

		private final String name;

		private CoverType(String name) {
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
}
