package sirttas.elementalcraft.block.pureinfuser.pedestal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.tag.ECTags;

public class BlockPedestal extends AbstractBlockECContainer implements IElementTypeProvider {

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape CONNECTOR_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 2D);
	private static final VoxelShape CONNECTOR_SOUTH = Block.makeCuboidShape(7D, 7D, 14D, 9D, 9D, 16D);
	private static final VoxelShape CONNECTOR_WEST = Block.makeCuboidShape(0D, 7D, 7D, 2D, 9D, 9D);
	private static final VoxelShape CONNECTOR_EAST = Block.makeCuboidShape(14D, 7D, 7D, 16D, 9D, 9D);

	private static final VoxelShape BASE = VoxelShapes.or(BASE_1, BASE_2, BASE_3);
	private static final VoxelShape AIR = VoxelShapes.or(Block.makeCuboidShape(5D, 0D, 5D, 11D, 3D, 11D), BASE_2, BASE_3);
	private static final VoxelShape EARTH = VoxelShapes.or(BASE, Block.makeCuboidShape(4D, 3D, 0D, 12D, 8D, 16D), Block.makeCuboidShape(0D, 3D, 4D, 16D, 8D, 12D));

	public static final String NAME = "pedestal";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;

	private ElementType elementType;

	public BlockPedestal(ElementType type) {
		elementType = type;
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return this.getElementType() != ElementType.NONE;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		switch (this.getElementType()) {
		case AIR:
			return TilePedestal.createAir();
		case EARTH:
			return TilePedestal.createEarth();
		case FIRE:
			return TilePedestal.createFire();
		case WATER:
			return TilePedestal.createWater();
		default:
			return null;
		}
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape shape = getBaseShape();

		if (Boolean.TRUE.equals(state.get(NORTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.get(SOUTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.get(EAST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.get(WEST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_WEST);
		}
		return shape;
	}

	private VoxelShape getBaseShape() {
		if (elementType == ElementType.AIR) {
			return AIR;
		} else if (elementType == ElementType.EARTH) {
			return EARTH;
		}
		return BASE;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(NORTH, SOUTH, EAST, WEST);
	}

	private boolean isConnected(IWorld worldIn, BlockPos pos, Direction facing, BooleanProperty opposite) {
		BlockState state = worldIn.getBlockState(pos.offset(facing));
		
		return ECTags.Blocks.PIPES.contains(state.getBlock()) && Boolean.TRUE.equals(state.get(opposite));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState()
				.with(NORTH, isConnected(context.getWorld(), context.getPos(), Direction.NORTH, SOUTH))
				.with(SOUTH, isConnected(context.getWorld(), context.getPos(), Direction.SOUTH, NORTH))
				.with(EAST, isConnected(context.getWorld(), context.getPos(), Direction.EAST, EAST))
				.with(WEST, isConnected(context.getWorld(), context.getPos(), Direction.WEST, WEST));
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		switch (facing) {
		case NORTH:
			return stateIn.with(NORTH, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(SOUTH)));
		case SOUTH:
			return stateIn.with(SOUTH, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(NORTH)));
		case EAST:
			return stateIn.with(EAST, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(WEST)));
		case WEST:
			return stateIn.with(WEST, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(EAST)));
		default:
			break;
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}
