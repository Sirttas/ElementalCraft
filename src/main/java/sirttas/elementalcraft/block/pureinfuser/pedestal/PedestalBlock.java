package sirttas.elementalcraft.block.pureinfuser.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.pipe.IPipeConnectedBlock;

public class PedestalBlock extends AbstractECContainerBlock implements IElementTypeProvider, IPipeConnectedBlock {

	private static final VoxelShape BASE_1 = Block.box(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.box(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.box(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape CONNECTOR_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 2D);
	private static final VoxelShape CONNECTOR_SOUTH = Block.box(7D, 7D, 14D, 9D, 9D, 16D);
	private static final VoxelShape CONNECTOR_WEST = Block.box(0D, 7D, 7D, 2D, 9D, 9D);
	private static final VoxelShape CONNECTOR_EAST = Block.box(14D, 7D, 7D, 16D, 9D, 9D);

	private static final VoxelShape BASE = Shapes.or(BASE_1, BASE_2, BASE_3);
	private static final VoxelShape AIR = Shapes.or(Block.box(5D, 0D, 5D, 11D, 3D, 11D), BASE_2, BASE_3);
	private static final VoxelShape EARTH = Shapes.or(BASE, Block.box(4D, 3D, 0D, 12D, 8D, 16D), Block.box(0D, 3D, 4D, 16D, 8D, 12D));

	public static final String NAME = "pedestal";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private ElementType elementType;

	public PedestalBlock(ElementType type) {
		elementType = type;
		this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
	}

	@Override
	public PedestalBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return this.getElementType() != ElementType.NONE ? new PedestalBlockEntity(pos, state) : null;
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		VoxelShape shape = getBaseShape();

		if (Boolean.TRUE.equals(state.getValue(NORTH))) {
			shape = Shapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.getValue(SOUTH))) {
			shape = Shapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.getValue(EAST))) {
			shape = Shapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.getValue(WEST))) {
			shape = Shapes.or(shape, CONNECTOR_WEST);
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(NORTH, SOUTH, EAST, WEST);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return doGetStateForPlacement(context.getLevel(), context.getClickedPos());
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return doUpdatePostPlacement(stateIn, worldIn, currentPos, facing);
	}
}
