package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NectarShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_nectar";

	private static final VoxelShape CORE_1_NORTH = Block.box(3D, 6D, 2D, 13D, 12D, 4D);
	private static final VoxelShape CORE_2_NORTH = Block.box(5D, 7D, 4D, 11D, 11D, 5D);
	private static final VoxelShape PIPE_1_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 2D);
	private static final VoxelShape PIPE_2_NORTH = Block.box(4D, 9D, -1D, 6D, 11D, 2D);
	private static final VoxelShape PIPE_3_NORTH = Block.box(10D, 9D, -1D, 12D, 11D, 2D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(CORE_1_NORTH, CORE_2_NORTH, PIPE_1_NORTH, PIPE_2_NORTH, PIPE_3_NORTH);

	private static final VoxelShape CORE_1_SOUTH = Block.box(3D, 6D, 12D, 13D, 12D, 14D);
	private static final VoxelShape CORE_2_SOUTH = Block.box(5D, 7D, 11D, 11D, 11D, 12D);
	private static final VoxelShape PIPE_1_SOUTH = Block.box(7D, 7D, 14D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_2_SOUTH = Block.box(4D, 9D, 14D, 6D, 11D, 17D);
	private static final VoxelShape PIPE_3_SOUTH = Block.box(10D, 9D, 14D, 12D, 11D, 17D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(CORE_1_SOUTH, CORE_2_SOUTH, PIPE_1_SOUTH, PIPE_2_SOUTH, PIPE_3_SOUTH);

	private static final VoxelShape CORE_1_WEST = Block.box(2D, 6D, 3D, 4D, 12D, 13D);
	private static final VoxelShape CORE_2_WEST = Block.box(4D, 7D, 5D, 5D, 11D, 11D);
	private static final VoxelShape PIPE_1_WEST = Block.box(0D, 7D, 7D, 2D, 9D, 9D);
	private static final VoxelShape PIPE_2_WEST = Block.box(-1D, 9D, 4D, 2D, 11D, 6D);
	private static final VoxelShape PIPE_3_WEST = Block.box(-1D, 9D, 10D, 2D, 11D, 12D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(CORE_1_WEST, CORE_2_WEST, PIPE_1_WEST, PIPE_2_WEST, PIPE_3_WEST);

	private static final VoxelShape CORE_1_EAST = Block.box(12D, 6D, 3D, 14D, 12D, 13D);
	private static final VoxelShape CORE_2_EAST = Block.box(11D, 7D, 5D, 12D, 11D, 11D);
	private static final VoxelShape PIPE_1_EAST = Block.box(14D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_2_EAST = Block.box(14D, 9D, 4D, 17D, 11D, 6D);
	private static final VoxelShape PIPE_3_EAST = Block.box(14D, 9D, 10D, 17D, 11D, 12D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(CORE_1_EAST, CORE_2_EAST, PIPE_1_EAST, PIPE_2_EAST, PIPE_3_EAST);


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(FACING)) {
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		default:
			return SHAPE_NORTH;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.nectar").withStyle(TextFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

}
