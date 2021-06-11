package sirttas.elementalcraft.block.spelldesk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.property.ECProperties;

public class SpellDeskBlock extends HorizontalBlock {

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

	private static final VoxelShape MAIN_SHAPE = VoxelShapes.or(BASE_1, BASE_2, PILAR);

	private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_NORTH_1, PLATE_NORTH_2, PLATE_NORTH_3);
	private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_SOUTH_1, PLATE_SOUTH_2, PLATE_SOUTH_3);
	private static final VoxelShape WEST_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_WEST_1, PLATE_WEST_2, PLATE_WEST_3);
	private static final VoxelShape EAST_SHAPE = VoxelShapes.or(MAIN_SHAPE, PLATE_EAST_1, PLATE_EAST_2, PLATE_EAST_3);

	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public SpellDeskBlock() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		}
		player.openMenu(new ContainerProvider(world, pos));
		return ActionResultType.CONSUME;
	}
	
	private class ContainerProvider implements INamedContainerProvider {

		private final World world;
		private final BlockPos pos;
		
		public ContainerProvider(World world, BlockPos pos) {
			this.world = world;
			this.pos = pos;
		}

		@Override
		public Container createMenu(int id, PlayerInventory inventory, PlayerEntity palyer) {
			return SpellDeskContainer.create(id, inventory, IWorldPosCallable.create(world, pos));
		}

		@Override
		public ITextComponent getDisplayName() {
			return new TranslationTextComponent(getDescriptionId());
		}
	}
}
