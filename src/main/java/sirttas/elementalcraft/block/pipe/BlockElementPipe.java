package sirttas.elementalcraft.block.pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.block.tile.element.IElementSender;

public class BlockElementPipe extends BlockECTileProvider {

	public static final String NAME = "elementpipe";

	private static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(6.5D, 6.5D, 6.5D, 9.5D, 9.5D, 9.5D);
	private static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0, 7D, 7D, 6.5D, 9D, 9D);
	private static final VoxelShape DOWN_SHAPE = Block.makeCuboidShape(7D, 0, 7D, 9D, 6.5D, 9D);
	private static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(7D, 7D, 0, 9D, 9D, 6.5D);
	private static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(9.5D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape UP_SHAPE = Block.makeCuboidShape(7D, 9.5D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(7D, 7D, 9.5D, 9D, 9D, 16D);

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

	public static final BooleanProperty NORTH_EXTRACT = BooleanProperty.create("north_extract");
	public static final BooleanProperty EAST_EXTRACT = BooleanProperty.create("east_extract");
	public static final BooleanProperty SOUTH_EXTRACT = BooleanProperty.create("south_extract");
	public static final BooleanProperty WEST_EXTRACT = BooleanProperty.create("west_extract");
	public static final BooleanProperty UP_EXTRACT = BooleanProperty.create("up_extract");
	public static final BooleanProperty DOWN_EXTRACT = BooleanProperty.create("down_extract");

	private final List<VoxelShape> boxes;

	private static boolean doesVectorColide(AxisAlignedBB bb, Vector3d vec) {
		return vec.x >= bb.minX && vec.y >= bb.minY && vec.z >= bb.minZ && vec.x <= bb.maxX && vec.y <= bb.maxY && vec.z <= bb.maxZ;
	}

	public BlockElementPipe() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid().tickRandomly());
		boxes = new ArrayList<>(7);
		boxes.add(BASE_SHAPE);
		boxes.add(EAST_SHAPE);
		boxes.add(DOWN_SHAPE);
		boxes.add(NORTH_SHAPE);
		boxes.add(WEST_SHAPE);
		boxes.add(UP_SHAPE);
		boxes.add(SOUTH_SHAPE);
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false).with(NORTH_EXTRACT, false)
				.with(EAST_EXTRACT, false).with(SOUTH_EXTRACT, false).with(WEST_EXTRACT, false).with(UP_EXTRACT, false).with(DOWN_EXTRACT, false));
	}

	public boolean canConnectTo(IBlockReader worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos).getBlock();
		TileEntity entity = worldIn.getTileEntity(pos);

		return block.equals(this) || entity instanceof IElementSender || entity instanceof IElementReceiver;
	}


	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, NORTH_EXTRACT, SOUTH_EXTRACT, EAST_EXTRACT, WEST_EXTRACT, UP_EXTRACT, DOWN_EXTRACT);
	}


	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileElementPipe();
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 1);
		((TileElementPipe) worldIn.getTileEntity(pos)).refresh();
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		((TileElementPipe) worldIn.getTileEntity(pos)).refresh();
	}

	protected final void addShape(VoxelShape box) {
		boxes.add(box);
	}

	private boolean compareShapes(VoxelShape shape1, VoxelShape shape2) {
		return shape1.getBoundingBox().equals(shape2.getBoundingBox());
	}

	private Direction getFace(VoxelShape box) {
		if (compareShapes(box, DOWN_SHAPE)) {
			return Direction.DOWN;
		} else if (compareShapes(box, UP_SHAPE)) {
			return Direction.UP;
		} else if (compareShapes(box, NORTH_SHAPE)) {
			return Direction.NORTH;
		} else if (compareShapes(box, SOUTH_SHAPE)) {
			return Direction.SOUTH;
		} else if (compareShapes(box, WEST_SHAPE)) {
			return Direction.WEST;
		} else if (compareShapes(box, EAST_SHAPE)) {
			return Direction.EAST;
		}
		return null;
	}

	boolean isRendered(VoxelShape box, BlockState state) {
		if (state.getBlock() == this) {
			if (compareShapes(box, BASE_SHAPE)) {
				return true;
			} else if (compareShapes(box, DOWN_SHAPE) && Boolean.TRUE.equals(state.get(DOWN))) { // NOSONAR
				return true;
			} else if (compareShapes(box, UP_SHAPE) && Boolean.TRUE.equals(state.get(UP))) { // NOSONAR
				return true;
			} else if (compareShapes(box, NORTH_SHAPE) && Boolean.TRUE.equals(state.get(NORTH))) {// NOSONAR
				return true;
			} else if (compareShapes(box, SOUTH_SHAPE) && Boolean.TRUE.equals(state.get(SOUTH))) {// NOSONAR
				return true;
			} else if (compareShapes(box, WEST_SHAPE) && Boolean.TRUE.equals(state.get(WEST))) {// NOSONAR
				return true;
			} else if (compareShapes(box, EAST_SHAPE) && Boolean.TRUE.equals(state.get(EAST))) {// NOSONAR
				return true;
			}
		}
		return false;
	}

	private VoxelShape getCurentShape(BlockState state) {
		VoxelShape result = VoxelShapes.empty();

		for (final VoxelShape box : boxes) {
			if (isRendered(box, state)) {
				result = VoxelShapes.or(result, box);
			}
		}
		return result;
	}

	@SuppressWarnings("resource")
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		final RayTraceResult result = Minecraft.getInstance().objectMouseOver;

		if (result != null && result.getType() == RayTraceResult.Type.BLOCK && ((BlockRayTraceResult) result).getPos().equals(pos)) {
			final Vector3d hit = result.getHitVec();

			for (final VoxelShape box : boxes) {
				if (doesVectorColide(box.getBoundingBox().offset(pos), hit) && isRendered(box, state)) {
					return box;
				}
			}
		}
		return getCurentShape(state);
	}


	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCurentShape(state);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileElementPipe pipe = (TileElementPipe) world.getTileEntity(pos);

		if (pipe != null) {
			final VoxelShape shape = state.getShape(world, pos, ISelectionContext.forEntity(player));

			return onShapeActivated(shape, pipe, hit);
		}
		return ActionResultType.PASS;
	}

	private ActionResultType onShapeActivated(VoxelShape shape, TileElementPipe pipe, BlockRayTraceResult hit) {
		Direction face = getFace(shape);

		if (face != null) {
			return pipe.activatePipe(face);
		} else if (shape == BASE_SHAPE) {
			return pipe.activatePipe(hit.getFace());
		}
		return ActionResultType.PASS;
	}

}
