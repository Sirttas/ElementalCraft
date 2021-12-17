package sirttas.elementalcraft.block.shrine.breeding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class BreedingShrineBlock extends AbstractShrineBlock<BreedingShrineBlockEntity> {

	public static final String NAME = "breedingshrine";

	private static final VoxelShape BASE_CORE = Shapes.or(ECShapes.SHRINE_SHAPE, Block.box(5D, 12D, 5D, 11D, 14D, 11D));

	private static final VoxelShape CORE_NORTH_1 = Block.box(3D, 3D, 0D, 13D, 12D, 3D);
	private static final VoxelShape CORE_NORTH_2 = Block.box(5D, 12D, 0D, 11D, 14D, 5D);
	private static final VoxelShape CORE_SOUTH_1 = Block.box(3D, 3D, 13D, 13D, 12D, 16D);
	private static final VoxelShape CORE_SOUTH_2 = Block.box(5D, 12D, 11D, 11D, 14D, 16D);
	private static final VoxelShape CORE_WEST_1 = Block.box(0D, 3D, 3D, 3D, 12D, 13D);
	private static final VoxelShape CORE_WEST_2 = Block.box(0D, 12D, 5D, 5D, 14D, 11D);
	private static final VoxelShape CORE_EAST_1 = Block.box(13D, 3D, 3D, 16D, 12D, 13D);
	private static final VoxelShape CORE_EAST_2 = Block.box(11D, 12D, 5D, 16D, 14D, 11D);

	private static final VoxelShape CORE_NORTH = Shapes.or(BASE_CORE, CORE_NORTH_1, CORE_NORTH_2);
	private static final VoxelShape CORE_SOUTH = Shapes.or(BASE_CORE, CORE_SOUTH_1, CORE_SOUTH_2);
	private static final VoxelShape CORE_WEST = Shapes.or(BASE_CORE, CORE_WEST_1, CORE_WEST_2);
	private static final VoxelShape CORE_EAST = Shapes.or(BASE_CORE, CORE_EAST_1, CORE_EAST_2);

	private static final VoxelShape BASE_BOWL_FULL = Block.box(3D, 0D, 3D, 13D, 7D, 13D);
	private static final VoxelShape BASE_BOWL_VOID = Block.box(5D, 1D, 5D, 11D, 7D, 11D);
	private static final VoxelShape BASE_BOWL = Shapes.join(BASE_BOWL_FULL, BASE_BOWL_VOID, BooleanOp.ONLY_FIRST);

	private static final VoxelShape BOWL_NORTH_1 = Block.box(5D, 7D, 11D, 11D, 12D, 13D);
	private static final VoxelShape BOWL_NORTH_2 = Block.box(5D, 12D, 11D, 11D, 14D, 16D);
	private static final VoxelShape BOWL_NORTH_3 = Block.box(3D, 0D, 13D, 13D, 12D, 16D);
	private static final VoxelShape BOWL_NORTH_TAPE = Block.box(7D, 10D, 8D, 9D, 12D, 11D);
	private static final VoxelShape BOWL_SOUTH_1 = Block.box(5D, 7D, 3D, 11D, 12D, 5D);
	private static final VoxelShape BOWL_SOUTH_2 = Block.box(5D, 12D, 0D, 11D, 14D, 5D);
	private static final VoxelShape BOWL_SOUTH_3 = Block.box(3D, 0D, 0D, 13D, 12D, 3D);
	private static final VoxelShape BOWL_SOUTH_TAPE = Block.box(7D, 10D, 5D, 9D, 12D, 8D);
	private static final VoxelShape BOWL_WEST_1 = Block.box(11D, 7D, 5D, 13D, 12D, 11D);
	private static final VoxelShape BOWL_WEST_2 = Block.box(11D, 12D, 5D, 16D, 14D, 11D);
	private static final VoxelShape BOWL_WEST_3 = Block.box(13D, 0D, 3D, 16D, 12D, 13D);
	private static final VoxelShape BOWL_WEST_TAPE = Block.box(8D, 10D, 7D, 11D, 12D, 9D);
	private static final VoxelShape BOWL_EAST_1 = Block.box(3D, 7D, 5D, 5D, 12D, 11D);
	private static final VoxelShape BOWL_EAST_2 = Block.box(0D, 12D, 5D, 5D, 14D, 11D);
	private static final VoxelShape BOWL_EAST_3 = Block.box(0D, 0D, 3D, 3D, 12D, 13D);
	private static final VoxelShape BOWL_EAST_TAPE = Block.box(5D, 10D, 7D, 8D, 12D, 9D);

	private static final VoxelShape BOWL_NORTH = Shapes.or(BASE_BOWL, BOWL_NORTH_1, BOWL_NORTH_2, BOWL_NORTH_3, BOWL_NORTH_TAPE);
	private static final VoxelShape BOWL_SOUTH = Shapes.or(BASE_BOWL, BOWL_SOUTH_1, BOWL_SOUTH_2, BOWL_SOUTH_3, BOWL_SOUTH_TAPE);
	private static final VoxelShape BOWL_WEST = Shapes.or(BASE_BOWL, BOWL_WEST_1, BOWL_WEST_2, BOWL_WEST_3, BOWL_WEST_TAPE);
	private static final VoxelShape BOWL_EAST = Shapes.or(BASE_BOWL, BOWL_EAST_1, BOWL_EAST_2, BOWL_EAST_3, BOWL_EAST_TAPE);

	public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public BreedingShrineBlock() {
		super(ElementType.EARTH);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, Part.CORE).setValue(WATERLOGGED, false));
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		level.setBlock(pos.relative(state.getValue(FACING)), state.setValue(PART, Part.BOWL), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, @Nonnull Player player) {
		Part part = state.getValue(PART);
		BlockPos blockpos = pos.relative(part == Part.CORE ? state.getValue(FACING) : state.getValue(FACING).getOpposite());
		BlockState blockstate = worldIn.getBlockState(blockpos);

		if (blockstate.getBlock() == this && blockstate.getValue(PART) != part) {
			worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
			worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
		}

		super.playerWillDestroy(worldIn, pos, state, player);
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(WATERLOGGED, FACING, PART);
	}

	@Override
	public BreedingShrineBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return state.getValue(PART) == Part.CORE ? super.newBlockEntity(pos, state) : null;
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		if (state.getValue(PART) == Part.CORE) {
			return switch (state.getValue(FACING)) {
				case NORTH -> CORE_NORTH;
				case SOUTH -> CORE_SOUTH;
				case WEST -> CORE_WEST;
				case EAST -> CORE_EAST;
				default -> BASE_CORE;
			};
		}
		return switch (state.getValue(FACING)) {
			case NORTH -> BOWL_NORTH;
			case SOUTH -> BOWL_SOUTH;
			case WEST -> BOWL_WEST;
			case EAST -> BOWL_EAST;
			default -> BASE_BOWL;
		};
	}

	public enum Part implements StringRepresentable {
		CORE("core"), BOWL("bowl");

		private final String name;

		Part(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Nonnull
		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}
