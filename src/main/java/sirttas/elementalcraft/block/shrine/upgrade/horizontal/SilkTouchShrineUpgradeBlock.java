package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SilkTouchShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_silk_touch";

	private static final VoxelShape CORE_NORTH = Block.box(6D, 6D, 4D, 10D, 16D, 8D);
	private static final VoxelShape PILAR_NORTH = Block.box(7D, 2D, 5D, 9D, 6D, 7D);
	private static final VoxelShape BASE_1_NORTH = Block.box(5D, 0D, 3D, 11D, 1D, 9D);
	private static final VoxelShape BASE_2_NORTH = Block.box(6D, 1D, 4D, 10D, 2D, 8D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape ATTACH_NORTH = Block.box(7D, 13D, -2D, 9D, 15D, 4D);

	private static final VoxelShape CORE_SOUTH = Block.box(6D, 6D, 8D, 10D, 16D, 12D);
	private static final VoxelShape PILAR_SOUTH = Block.box(7D, 2D, 9D, 9D, 6D, 11D);
	private static final VoxelShape BASE_1_SOUTH = Block.box(5D, 0D, 7D, 11D, 1D, 13D);
	private static final VoxelShape BASE_2_SOUTH = Block.box(6D, 1D, 8D, 10D, 2D, 12D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape ATTACH_SOUTH = Block.box(7D, 13D, 12D, 9D, 15D, 18D);

	private static final VoxelShape CORE_WEST = Block.box(4D, 6D, 6D, 8D, 16D, 10D);
	private static final VoxelShape PILAR_WEST = Block.box(5D, 2D, 7D, 7D, 6D, 9D);
	private static final VoxelShape BASE_1_WEST = Block.box(3D, 0D, 5D, 9D, 1D, 11D);
	private static final VoxelShape BASE_2_WEST = Block.box(4D, 1D, 6D, 8D, 2D, 10D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape ATTACH_WEST = Block.box(-2D, 13D, 7D, 4D, 15D, 9D);

	private static final VoxelShape CORE_EAST = Block.box(8D, 6D, 6D, 12D, 16D, 10D);
	private static final VoxelShape PILAR_EAST = Block.box(9D, 2D, 7D, 11D, 6D, 9D);
	private static final VoxelShape BASE_1_EAST = Block.box(7D, 0D, 5D, 13D, 1D, 11D);
	private static final VoxelShape BASE_2_EAST = Block.box(8D, 1D, 6D, 12D, 2D, 10D);
	private static final VoxelShape PIPE_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape ATTACH_EAST = Block.box(12D, 13D, 7D, 18D, 15D, 9D);

	private static final VoxelShape SHAPE_NORTH = Shapes.or(CORE_NORTH, PILAR_NORTH, BASE_1_NORTH, BASE_2_NORTH, PIPE_NORTH);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(CORE_SOUTH, PILAR_SOUTH, BASE_1_SOUTH, BASE_2_SOUTH, PIPE_SOUTH);
	private static final VoxelShape SHAPE_WEST = Shapes.or(CORE_WEST, PILAR_WEST, BASE_1_WEST, BASE_2_WEST, PIPE_WEST);
	private static final VoxelShape SHAPE_EAST = Shapes.or(CORE_EAST, PILAR_EAST, BASE_1_EAST, BASE_2_EAST, PIPE_EAST);

	private static final VoxelShape SHAPE_NORTH_ATTACHED = Shapes.or(SHAPE_NORTH, ATTACH_NORTH);
	private static final VoxelShape SHAPE_SOUTH_ATTACHED = Shapes.or(SHAPE_SOUTH, ATTACH_SOUTH);
	private static final VoxelShape SHAPE_WEST_ATTACHED = Shapes.or(SHAPE_WEST, ATTACH_WEST);
	private static final VoxelShape SHAPE_EAST_ATTACHED = Shapes.or(SHAPE_EAST, ATTACH_EAST);


	public SilkTouchShrineUpgradeBlock() {
		super(ShrineUpgrades.SILK_TOUCH);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.ATTACHED, true).setValue(WATERLOGGED, false));
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		if (state.getValue(BlockStateProperties.ATTACHED)) {
			return switch (state.getValue(FACING)) {
				case SOUTH -> SHAPE_SOUTH_ATTACHED;
				case WEST -> SHAPE_WEST_ATTACHED;
				case EAST -> SHAPE_EAST_ATTACHED;
				default -> SHAPE_NORTH_ATTACHED;
			};
		}
		return switch (state.getValue(FACING)) {
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			case EAST -> SHAPE_EAST;
			default -> SHAPE_NORTH;
		};
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("enchantment.minecraft.silk_touch").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flag);
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		var state = super.getStateForPlacement(context);
		var facing = state.getValue(FACING);

		return state.setValue(BlockStateProperties.ATTACHED, context.getLevel().getBlockState(context.getClickedPos().relative(facing)).is(ECTags.Blocks.SILK_TOUCH_SHRINE_UPGRADE_ATTACHED));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		super.createBlockStateDefinition(container);
		container.add(BlockStateProperties.ATTACHED);
	}
}
