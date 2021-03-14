package sirttas.elementalcraft.block.shrine.breeding;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;

public class BlockBreedingShrine extends AbstractBlockShrine {

	public static final String NAME = "breedingshrine";

	private static final VoxelShape BASE_CORE = VoxelShapes.or(Shapes.SHRINE_SHAPE, Block.makeCuboidShape(5D, 12D, 5D, 11D, 14D, 11D));

	private static final VoxelShape CORE_NORTH_1 = Block.makeCuboidShape(3D, 3D, 0D, 13D, 12D, 3D);
	private static final VoxelShape CORE_NORTH_2 = Block.makeCuboidShape(5D, 12D, 0D, 11D, 14D, 5D);
	private static final VoxelShape CORE_SOUTH_1 = Block.makeCuboidShape(3D, 3D, 13D, 13D, 12D, 16D);
	private static final VoxelShape CORE_SOUTH_2 = Block.makeCuboidShape(5D, 12D, 11D, 11D, 14D, 16D);
	private static final VoxelShape CORE_WEST_1 = Block.makeCuboidShape(0D, 3D, 3D, 3D, 12D, 13D);
	private static final VoxelShape CORE_WEST_2 = Block.makeCuboidShape(0D, 12D, 5D, 5D, 14D, 11D);
	private static final VoxelShape CORE_EAST_1 = Block.makeCuboidShape(13D, 3D, 3D, 16D, 12D, 13D);
	private static final VoxelShape CORE_EAST_2 = Block.makeCuboidShape(11D, 12D, 5D, 16D, 14D, 11D);

	private static final VoxelShape CORE_NORTH = VoxelShapes.or(BASE_CORE, CORE_NORTH_1, CORE_NORTH_2);
	private static final VoxelShape CORE_SOUTH = VoxelShapes.or(BASE_CORE, CORE_SOUTH_1, CORE_SOUTH_2);
	private static final VoxelShape CORE_WEST = VoxelShapes.or(BASE_CORE, CORE_WEST_1, CORE_WEST_2);
	private static final VoxelShape CORE_EAST = VoxelShapes.or(BASE_CORE, CORE_EAST_1, CORE_EAST_2);

	private static final VoxelShape BASE_BOWL_FULL = Block.makeCuboidShape(3D, 0D, 3D, 13D, 7D, 13D);
	private static final VoxelShape BASE_BOWL_VOID = Block.makeCuboidShape(5D, 1D, 5D, 11D, 7D, 11D);
	private static final VoxelShape BASE_BOWL = VoxelShapes.combineAndSimplify(BASE_BOWL_FULL, BASE_BOWL_VOID, IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape BOWL_NORTH_1 = Block.makeCuboidShape(5D, 7D, 11D, 11D, 12D, 13D);
	private static final VoxelShape BOWL_NORTH_2 = Block.makeCuboidShape(5D, 12D, 11D, 11D, 14D, 16D);
	private static final VoxelShape BOWL_NORTH_3 = Block.makeCuboidShape(3D, 0D, 13D, 13D, 12D, 16D);
	private static final VoxelShape BOWL_NORTH_TAPE = Block.makeCuboidShape(7D, 10D, 8D, 9D, 12D, 11D);
	private static final VoxelShape BOWL_SOUTH_1 = Block.makeCuboidShape(5D, 7D, 3D, 11D, 12D, 5D);
	private static final VoxelShape BOWL_SOUTH_2 = Block.makeCuboidShape(5D, 12D, 0D, 11D, 14D, 5D);
	private static final VoxelShape BOWL_SOUTH_3 = Block.makeCuboidShape(3D, 0D, 0D, 13D, 12D, 3D);
	private static final VoxelShape BOWL_SOUTH_TAPE = Block.makeCuboidShape(7D, 10D, 5D, 9D, 12D, 8D);
	private static final VoxelShape BOWL_WEST_1 = Block.makeCuboidShape(11D, 7D, 5D, 13D, 12D, 11D);
	private static final VoxelShape BOWL_WEST_2 = Block.makeCuboidShape(11D, 12D, 5D, 16D, 14D, 11D);
	private static final VoxelShape BOWL_WEST_3 = Block.makeCuboidShape(13D, 0D, 3D, 16D, 12D, 13D);
	private static final VoxelShape BOWL_WEST_TAPE = Block.makeCuboidShape(8D, 10D, 7D, 11D, 12D, 9D);
	private static final VoxelShape BOWL_EAST_1 = Block.makeCuboidShape(3D, 7D, 5D, 5D, 12D, 11D);
	private static final VoxelShape BOWL_EAST_2 = Block.makeCuboidShape(0D, 12D, 5D, 5D, 14D, 11D);
	private static final VoxelShape BOWL_EAST_3 = Block.makeCuboidShape(0D, 0D, 3D, 3D, 12D, 13D);
	private static final VoxelShape BOWL_EAST_TAPE = Block.makeCuboidShape(5D, 10D, 7D, 8D, 12D, 9D);

	private static final VoxelShape BOWL_NORTH = VoxelShapes.or(BASE_BOWL, BOWL_NORTH_1, BOWL_NORTH_2, BOWL_NORTH_3, BOWL_NORTH_TAPE);
	private static final VoxelShape BOWL_SOUTH = VoxelShapes.or(BASE_BOWL, BOWL_SOUTH_1, BOWL_SOUTH_2, BOWL_SOUTH_3, BOWL_SOUTH_TAPE);
	private static final VoxelShape BOWL_WEST = VoxelShapes.or(BASE_BOWL, BOWL_WEST_1, BOWL_WEST_2, BOWL_WEST_3, BOWL_WEST_TAPE);
	private static final VoxelShape BOWL_EAST = VoxelShapes.or(BASE_BOWL, BOWL_EAST_1, BOWL_EAST_2, BOWL_EAST_3, BOWL_EAST_TAPE);

	public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockBreedingShrine() {
		super(ElementType.EARTH);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(PART, Part.CORE));
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.offset(state.get(FACING)), state.with(PART, Part.BOWL), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		Part part = state.get(PART);
		BlockPos blockpos = pos.offset(part == Part.CORE ? state.get(FACING) : state.get(FACING).getOpposite());
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if (blockstate.getBlock() == this && blockstate.get(PART) != part) {
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING, PART);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.get(PART) == Part.CORE;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return hasTileEntity(state) ? new TileBreedingShrine() : null;
	}


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(PART) == Part.CORE) {
			switch (state.get(FACING)) {
			case NORTH:
				return CORE_NORTH;
			case SOUTH:
				return CORE_SOUTH;
			case WEST:
				return CORE_WEST;
			case EAST:
				return CORE_EAST;
			default:
				return BASE_CORE;
			}
		}
		switch (state.get(FACING)) {
		case NORTH:
			return BOWL_NORTH;
		case SOUTH:
			return BOWL_SOUTH;
		case WEST:
			return BOWL_WEST;
		case EAST:
			return BOWL_EAST;
		default:
			return BASE_BOWL;
		}
	}

	public enum Part implements IStringSerializable {
		CORE("core"), BOWL("bowl");

		private final String name;

		private Part(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public String getString() {
			return this.name;
		}
	}
}