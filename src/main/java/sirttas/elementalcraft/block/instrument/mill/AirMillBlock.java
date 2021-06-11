package sirttas.elementalcraft.block.instrument.mill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class AirMillBlock extends AbstractECContainerBlock {

	public static final String NAME = "air_mill";

	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.box(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.box(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.box(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.box(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape GRINDSTONE = Block.box(4D, 5D, 4D, 12D, 8D, 12D);
	private static final VoxelShape SHAPE = VoxelShapes.or(OVEN_SLAB, OVEN_SLAB_2, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4, GRINDSTONE);

	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public AirMillBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return isLower(state);
	}

	private boolean isLower(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return isLower(state) ? new AirMillBlockEntity() : null;
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (isLower(state)) {
			final AirMillBlockEntity airMill = (AirMillBlockEntity) world.getBlockEntity(pos);
			IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
			ItemStack heldItem = player.getItemInHand(hand);
	
			if (airMill != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
				if (!airMill.getInventory().getItem(1).isEmpty()) {
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
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		AbstractPylonShrineBlock.doubeHalfHarvest(this, worldIn, pos, state, player);
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return isLower(state) ? SHAPE : VoxelShapes.empty();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
