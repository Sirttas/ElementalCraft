package sirttas.elementalcraft.block.instrument.mill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class BlockAirMill extends AbstractBlockECContainer {

	public static final String NAME = "air_mill";

	private static final VoxelShape OVEN_SLAB = Block.makeCuboidShape(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.makeCuboidShape(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape GRINDSTONE = Block.makeCuboidShape(4D, 5D, 4D, 12D, 8D, 12D);
	private static final VoxelShape SHAPE = VoxelShapes.or(OVEN_SLAB, OVEN_SLAB_2, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4, GRINDSTONE);

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public BlockAirMill() {
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HALF, DoubleBlockHalf.LOWER));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return isLower(state);
	}

	private boolean isLower(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return isLower(state) ? new TileAirMill() : null;
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (isLower(state)) {
			final TileAirMill airMill = (TileAirMill) world.getTileEntity(pos);
			IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
			ItemStack heldItem = player.getHeldItem(hand);
	
			if (airMill != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
				if (!airMill.getInventory().getStackInSlot(1).isEmpty()) {
					return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
				}
				return this.onSlotActivated(inv, player, heldItem, 0);
			}
		}
		return ActionResultType.PASS;
	}
	
	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
	
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
		}
	
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return isLower(state) ? SHAPE : VoxelShapes.empty();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}
	
	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}
