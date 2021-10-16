package sirttas.elementalcraft.block.spelldesk;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.property.ECProperties;

public class SpellDeskBlock extends HorizontalDirectionalBlock {

	public static final String NAME = "spell_desk";

	private static final VoxelShape BASE_1 = Block.box(4D, 0D, 4D, 12D, 2D, 12D);
	private static final VoxelShape BASE_2 = Block.box(5D, 2D, 5D, 11D, 3D, 11D);
	private static final VoxelShape PILAR = Block.box(6D, 3D, 6D, 10D, 11D, 10D);

	private static final VoxelShape PLATE_WEST_1 = Block.box(1D, 8D, 2D, 6D, 10D, 14D);
	private static final VoxelShape PLATE_WEST_2 = Block.box(4D, 10D, 2D, 11D, 12D, 14D);
	private static final VoxelShape PLATE_WEST_3 = Block.box(9D, 12D, 2D, 15D, 14D, 14D);

	private static final VoxelShape PLATE_EAST_1 = Block.box(10D, 8D, 2D, 15D, 10D, 14D);
	private static final VoxelShape PLATE_EAST_2 = Block.box(5D, 10D, 2D, 12D, 12D, 14D);
	private static final VoxelShape PLATE_EAST_3 = Block.box(1D, 12D, 2D, 7D, 14D, 14D);

	private static final VoxelShape PLATE_NORTH_1 = Block.box(2D, 8D, 1D, 14D, 10D, 6D);
	private static final VoxelShape PLATE_NORTH_2 = Block.box(2D, 10D, 4D, 14D, 12D, 11D);
	private static final VoxelShape PLATE_NORTH_3 = Block.box(2D, 12D, 9D, 14D, 14D, 15D);

	private static final VoxelShape PLATE_SOUTH_1 = Block.box(2D, 8D, 10D, 14D, 10D, 15D);
	private static final VoxelShape PLATE_SOUTH_2 = Block.box(2D, 10D, 5D, 14D, 12D, 12D);
	private static final VoxelShape PLATE_SOUTH_3 = Block.box(2D, 12D, 1D, 14D, 14D, 7D);

	private static final VoxelShape MAIN_SHAPE = Shapes.or(BASE_1, BASE_2, PILAR);

	private static final VoxelShape NORTH_SHAPE = Shapes.or(MAIN_SHAPE, PLATE_NORTH_1, PLATE_NORTH_2, PLATE_NORTH_3);
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(MAIN_SHAPE, PLATE_SOUTH_1, PLATE_SOUTH_2, PLATE_SOUTH_3);
	private static final VoxelShape WEST_SHAPE = Shapes.or(MAIN_SHAPE, PLATE_WEST_1, PLATE_WEST_2, PLATE_WEST_3);
	private static final VoxelShape EAST_SHAPE = Shapes.or(MAIN_SHAPE, PLATE_EAST_1, PLATE_EAST_2, PLATE_EAST_3);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public SpellDeskBlock() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(FACING);
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
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new ContainerProvider(world, pos));
		return InteractionResult.CONSUME;
	}
	
	private class ContainerProvider implements MenuProvider {

		private final Level world;
		private final BlockPos pos;
		
		public ContainerProvider(Level world, BlockPos pos) {
			this.world = world;
			this.pos = pos;
		}

		@Override
		public AbstractContainerMenu createMenu(int id, Inventory inventory, Player palyer) {
			return SpellDeskMenu.create(id, inventory, ContainerLevelAccess.create(world, pos));
		}

		@Override
		public Component getDisplayName() {
			return new TranslatableComponent(getDescriptionId());
		}
	}
}
