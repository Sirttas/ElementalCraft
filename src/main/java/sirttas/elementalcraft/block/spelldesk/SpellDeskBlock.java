package sirttas.elementalcraft.block.spelldesk;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SpellDeskBlock extends HorizontalDirectionalBlock {

	public static final String NAME = "spell_desk";
	public static final MapCodec<SpellDeskBlock> CODEC = simpleCodec(SpellDeskBlock::new);

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

	public SpellDeskBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected @NotNull MapCodec<SpellDeskBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> MAIN_SHAPE;
		};
	}

	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new ContainerProvider());
		return InteractionResult.CONSUME;
	}
	
	private class ContainerProvider implements MenuProvider {

		@Override
		public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
			return new SpellDeskMenu(id, inventory);
		}

		@Nonnull
        @Override
		public Component getDisplayName() {
			return Component.translatable(getDescriptionId());
		}
	}
}
