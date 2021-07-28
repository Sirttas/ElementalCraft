package sirttas.elementalcraft.block.instrument.purifier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class PurifierBlock extends AbstractECContainerBlock {

	public static final String NAME = "purifier";

	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.box(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.box(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.box(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.box(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape MAIN_SHAPE = Shapes.or(OVEN_SLAB, OVEN_SLAB_2, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	private static final VoxelShape NORTH_EMPTY_SPACE = Block.box(6D, 4D, 0D, 10D, 8D, 4D);
	private static final VoxelShape NORTH_OVEN_BLOCK = Block.box(4D, 4D, 0D, 12D, 10D, 7D);
	private static final VoxelShape NORTH_OVEN_BLOCK_2 = Block.box(6D, 4D, 7D, 10D, 8D, 11D);
	private static final VoxelShape NORTH_OVEN = Shapes.join(Shapes.or(NORTH_OVEN_BLOCK, NORTH_OVEN_BLOCK_2), NORTH_EMPTY_SPACE, BooleanOp.ONLY_FIRST);

	private static final VoxelShape SOUTH_EMPTY_SPACE = Block.box(6D, 4D, 12D, 10D, 8D, 16D);
	private static final VoxelShape SOUTH_OVEN_BLOCK = Block.box(4D, 4D, 9D, 12D, 10D, 16D);
	private static final VoxelShape SOUTH_OVEN_BLOCK_2 = Block.box(6D, 4D, 5D, 10D, 8D, 9D);
	private static final VoxelShape SOUTH_OVEN = Shapes.join(Shapes.or(SOUTH_OVEN_BLOCK, SOUTH_OVEN_BLOCK_2), SOUTH_EMPTY_SPACE, BooleanOp.ONLY_FIRST);

	private static final VoxelShape WEST_EMPTY_SPACE = Block.box(0D, 4D, 6D, 4D, 8D, 10D);
	private static final VoxelShape WEST_OVEN_BLOCK = Block.box(0D, 4D, 4D, 7D, 10D, 12D);
	private static final VoxelShape WEST_OVEN_BLOCK_2 = Block.box(7D, 4D, 6D, 11D, 8D, 10D);
	private static final VoxelShape WEST_OVEN = Shapes.join(Shapes.or(WEST_OVEN_BLOCK, WEST_OVEN_BLOCK_2), WEST_EMPTY_SPACE, BooleanOp.ONLY_FIRST);

	private static final VoxelShape EAST_EMPTY_SPACE = Block.box(12D, 4D, 6D, 16D, 8D, 10D);
	private static final VoxelShape EAST_OVEN_BLOCK = Block.box(9D, 4D, 4D, 16D, 10D, 12D);
	private static final VoxelShape EAST_OVEN_BLOCK_2 = Block.box(5D, 4D, 6D, 9D, 8D, 10D);
	private static final VoxelShape EAST_OVEN = Shapes.join(Shapes.or(EAST_OVEN_BLOCK, EAST_OVEN_BLOCK_2), EAST_EMPTY_SPACE, BooleanOp.ONLY_FIRST);

	private static final VoxelShape NORTH_SHAPE = Shapes.or(MAIN_SHAPE, NORTH_OVEN);
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(MAIN_SHAPE, SOUTH_OVEN);
	private static final VoxelShape EAST_SHAPE = Shapes.or(MAIN_SHAPE, EAST_OVEN);
	private static final VoxelShape WEST_SHAPE = Shapes.or(MAIN_SHAPE, WEST_OVEN);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public PurifierBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PurifierBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, PurifierBlockEntity.TYPE, AbstractInstrumentBlockEntity::tick);
	}
	
	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final PurifierBlockEntity purifier = (PurifierBlockEntity) world.getBlockEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getItemInHand(hand);

		if (purifier != null && (hand == InteractionHand.MAIN_HAND || !heldItem.isEmpty())) {
			if (!purifier.getInventory().getItem(1).isEmpty()) {
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			} else if (heldItem.isEmpty() || ElementalCraft.PURE_ORE_MANAGER.isValidOre(heldItem)) {
				return this.onSlotActivated(inv, player, heldItem, 0);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return MAIN_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
