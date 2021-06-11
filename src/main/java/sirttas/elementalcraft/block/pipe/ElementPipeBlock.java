package sirttas.elementalcraft.block.pipe;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.AbstractECBlockEntityProviderBlock;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.tag.ECTags;

public class ElementPipeBlock extends AbstractECBlockEntityProviderBlock {

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

	private static final VoxelShape FRAME_SHAPE = VoxelShapes.join(VoxelShapes.block(),
			VoxelShapes.or(Block.box(0D, 1D, 1D, 16D, 15D, 15D), Block.box(1D, 0D, 1D, 15D, 16D, 15D), Block.box(1D, 1D, 0D, 15D, 15D, 16D)),
			IBooleanFunction.ONLY_FIRST);
	
	private static final List<VoxelShape> SHAPES = ImmutableList.of(EAST_SHAPE, NORTH_SHAPE, WEST_SHAPE, SOUTH_SHAPE, UP_SHAPE, DOWN_SHAPE, BASE_SHAPE, FRAME_SHAPE);

	public static final EnumProperty<CoverType> COVER = EnumProperty.create("cover", CoverType.class);
	
	public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
	public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
	public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
	public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
	public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
	public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);

	private int maxTransferAmount;

	public ElementPipeBlock(int maxTransferAmount) {
		super(AbstractBlock.Properties.of(Material.METAL).strength(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).noOcclusion().randomTicks());
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(COVER, CoverType.NONE)
				.setValue(NORTH, ConnectionType.NONE)
				.setValue(EAST, ConnectionType.NONE)
				.setValue(SOUTH, ConnectionType.NONE)
				.setValue(WEST, ConnectionType.NONE)
				.setValue(UP, ConnectionType.NONE)
				.setValue(DOWN, ConnectionType.NONE));
		this.maxTransferAmount = maxTransferAmount;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(COVER, NORTH, SOUTH, EAST, WEST, UP, DOWN);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ElementPipeBlockEntity(maxTransferAmount);
	}

	@Override
	@Deprecated
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		((ElementPipeBlockEntity) worldIn.getBlockEntity(pos)).refresh();
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		((ElementPipeBlockEntity) worldIn.getBlockEntity(pos)).refresh();
	}

	private boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return shape1.bounds().equals(shape2.bounds());
	}

	private Direction getFace(VoxelShape shape, BlockRayTraceResult hit) {
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
	
	public static boolean showCover(BlockState state, PlayerEntity player) {
		return isCovered(state)
				&& (player == null || !player.getMainHandItem().isEmpty() && EntityHelper.handStream(player).noneMatch(stack -> ECTags.Items.PIPE_COVER_HIDING.contains(stack.getItem())));
	}

	private static boolean isCovered(BlockState state) {
		return state.getValue(COVER) == CoverType.COVERED;
	}

	private boolean isRendered(VoxelShape shape, BlockState state) {
		return (state.is(this)) && (compareShapes(shape, BASE_SHAPE) 
				|| (compareShapes(shape, DOWN_SHAPE) && state.getValue(DOWN).isConnected())
				|| (compareShapes(shape, UP_SHAPE) && state.getValue(UP).isConnected()) 
				|| (compareShapes(shape, NORTH_SHAPE) && state.getValue(NORTH).isConnected())
				|| (compareShapes(shape, SOUTH_SHAPE) && state.getValue(SOUTH).isConnected()) 
				|| (compareShapes(shape, WEST_SHAPE) && state.getValue(WEST).isConnected())
				|| (compareShapes(shape, EAST_SHAPE) && state.getValue(EAST).isConnected())
				|| (compareShapes(shape, FRAME_SHAPE) && state.getValue(COVER) == CoverType.FRAME));
	}

	private VoxelShape getCurentShape(BlockState state, PlayerEntity player) {
		VoxelShape result = VoxelShapes.empty();

		if (showCover(state, player)) {
			return VoxelShapes.block();
		}
		for (final VoxelShape shape : SHAPES) {
			if (isRendered(shape, state)) {
				result = VoxelShapes.or(result, shape);
			}
		}
		return result;
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderShape(BlockState state) {
		if (isCovered(state)) {
			return BlockRenderType.ENTITYBLOCK_ANIMATED;
		}
		return super.getRenderShape(state);
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		PlayerEntity player = getPlayer(context);

		return worldIn instanceof World && ((World) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult, player) : getCurentShape(state, player);
	}

	private PlayerEntity getPlayer(ISelectionContext context) {
		Entity entity = context.getEntity();
		
		return entity instanceof PlayerEntity ? (PlayerEntity) entity : ElementalCraft.PROXY.getDefaultPlayer();
	}

	public VoxelShape getShape(BlockState state, BlockPos pos, RayTraceResult result, PlayerEntity player) {
		if (!showCover(state, player) && result != null && result.getType() == RayTraceResult.Type.BLOCK && ((BlockRayTraceResult) result).getBlockPos().equals(pos)) {
			final Vector3d hit = result.getLocation();

			for (final VoxelShape shape : SHAPES) {
				if (isRendered(shape, state) && ShapeHelper.vectorCollideWithShape(shape, pos, hit)) {
					return shape;
				}
			}
		}
		return getCurentShape(state, player);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCurentShape(state, null);
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final ElementPipeBlockEntity pipe = (ElementPipeBlockEntity) world.getBlockEntity(pos);

		if (pipe != null) {
			final VoxelShape shape = getShape(state, pos, hit, player);
			
			if (compareShapes(shape, FRAME_SHAPE) || state.getValue(COVER) == CoverType.FRAME) {
				return pipe.setCover(player, hand);
			} else {
				Direction face = getFace(shape, hit);
				ActionResultType value = onShapeActivated(face, pipe);
	
				if (value != ActionResultType.PASS) {
					player.displayClientMessage(pipe.getConnectionMessage(face), true);
				}
				return value;
			}
		}
		return ActionResultType.PASS;
	}

	private ActionResultType onShapeActivated(Direction face, ElementPipeBlockEntity pipe) {
		if (face != null) {
			return pipe.activatePipe(face);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	@Deprecated
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			if (isCovered(state)) {
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(((ElementPipeBlockEntity) worldIn.getBlockEntity(pos)).getCoverState().getBlock()));
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	public static boolean isConnected(BlockState state, EnumProperty<ConnectionType> prop) {
		return ECTags.Blocks.PIPES.contains(state.getBlock()) && state.getValue(prop).isConnected();
	}

	public enum ConnectionType implements IStringSerializable {
		NONE("none"), CONNECTED("connected"), EXTRACT("extract");

		public static final Codec<ConnectionType> CODEC = IStringSerializable.fromEnum(ConnectionType::values, ConnectionType::byName);

		private final String name;

		private ConnectionType(String name) {
			this.name = name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static ConnectionType byName(String name) {
			for (ConnectionType bonusType : values()) {
				if (bonusType.name.equals(name)) {
					return bonusType;
				}
			}
			return NONE;
		}
		
		public boolean isConnected() {
			return this != NONE;
		}
	}
	
	public enum CoverType implements IStringSerializable {
		NONE("none"), FRAME("frame"), COVERED("covered");

		public static final Codec<CoverType> CODEC = IStringSerializable.fromEnum(CoverType::values, CoverType::byName);

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
